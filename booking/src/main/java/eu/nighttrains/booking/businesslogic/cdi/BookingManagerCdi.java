package eu.nighttrains.booking.businesslogic.cdi;

import eu.nighttrains.booking.businesslogic.BookingManager;
import eu.nighttrains.booking.businesslogic.DestinationManager;
import eu.nighttrains.booking.businesslogic.RailwayStationManager;
import eu.nighttrains.booking.businesslogic.TrainConnectionManager;
import eu.nighttrains.booking.businesslogic.exception.BookingNotFound;
import eu.nighttrains.booking.businesslogic.exception.BookingNotPossible;
import eu.nighttrains.booking.businesslogic.exception.NoConnectionsAvailable;
import eu.nighttrains.booking.businesslogic.exception.NoTrainCarAvailable;
import eu.nighttrains.booking.dal.BookingDao;
import eu.nighttrains.booking.dal.ReservationDao;
import eu.nighttrains.booking.dal.TicketDao;
import eu.nighttrains.booking.domain.RailwayStation;
import eu.nighttrains.booking.domain.RailwayStationConnection;
import eu.nighttrains.booking.domain.TrainCar;
import eu.nighttrains.booking.domain.TrainCarType;
import eu.nighttrains.booking.dto.*;
import eu.nighttrains.booking.logging.Logger;
import eu.nighttrains.booking.logging.LoggerQualifier;
import eu.nighttrains.booking.logging.LoggerType;
import eu.nighttrains.booking.model.Booking;
import eu.nighttrains.booking.model.Reservation;
import eu.nighttrains.booking.model.Ticket;
import eu.nighttrains.booking.util.ConnectionDateCalculator;
import eu.nighttrains.booking.util.TicketConnectionSeparator;
import org.eclipse.microprofile.opentracing.Traced;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequestScoped
@Transactional
@Traced
public class BookingManagerCdi implements BookingManager {
    private BookingDao bookingDao;
    private ReservationDao reservationDao;
    private TicketDao ticketDao;
    private DestinationManager destinationManager;
    private TrainConnectionManager trainConnectionManager;
    private RailwayStationManager railwayStationManager;
    private Logger logger;

    @Inject
    public BookingManagerCdi(BookingDao bookingDao,
                             ReservationDao reservationDao,
                             TicketDao ticketDao,
                             DestinationManager destinationManager,
                             TrainConnectionManager trainConnectionManager,
                             RailwayStationManager railwayStationManager,
                             @LoggerQualifier(type = LoggerType.CONSOLE) Logger logger) {
        this.bookingDao = bookingDao;
        this.reservationDao = reservationDao;
        this.ticketDao = ticketDao;
        this.destinationManager = destinationManager;
        this.trainConnectionManager = trainConnectionManager;
        this.railwayStationManager = railwayStationManager;
        this.logger = logger;
    }

    @Override
    public Long book(BookingRequestDto2 bookingRequest){
        try{
            return addBooking2(bookingRequest);
        } catch(NoConnectionsAvailable ex) {
            throw new BookingNotPossible("NoConnectionsAvailable");
        } catch(NoTrainCarAvailable ex) {
            throw new BookingNotPossible("NoTrainCarAvailable",
                    ex.getConnection());
        }
    }

    public Long addBooking2(BookingRequestDto2 bookingRequest){
        ConnectionDateCalculator calculator = new ConnectionDateCalculator();
        TicketConnectionSeparator separator = new TicketConnectionSeparator();

        long originId = bookingRequest.getOriginId();
        long destinationId = bookingRequest.getDestinationId();
        LocalDate ticketDate = bookingRequest.getJourneyStartDate();

        List<RailwayStationConnection> connections =
                destinationManager.getConnections(originId, destinationId);
        if(connections.isEmpty()){
            throw new NoConnectionsAvailable();
        }
        /*if(isTicketForToday(ticketDate) && isTimeOk(rsConnections.get(0))){
            throw new NoConnectionsAvailable();
        }*/
        connections = calculator.calculateDates(connections, ticketDate);
        List<List<RailwayStationConnection>> ticketConnections = separator
                .separateConnections(connections);

        Booking booking = book(bookingRequest, ticketConnections);
        booking.setOriginId(originId);
        booking.setDestinationId(destinationId);
        booking.setDepartureDate(bookingRequest.getJourneyStartDate());
        Booking savedBooking = saveBooking(booking);
        return savedBooking.getId();
    }

    private Booking saveBooking(Booking booking) {
        for(Ticket ticket : booking.getTickets()){
            List<Reservation> mergedReservations = null;
            mergedReservations = reservationDao.bulkMerge(ticket.getReservations());
            ticket.setReservations(mergedReservations);
        }
        List<Ticket> mergedTickets = ticketDao.bulkMerge(booking.getTickets());
        booking.setTickets(mergedTickets);
        Booking mergedBooking = bookingDao.merge(booking);
        return mergedBooking;
    }

    public Booking book(BookingRequestDto2 bookingRequest, List<List<RailwayStationConnection>> ticketConnections){
        Booking booking = new Booking();
        List<Ticket> ticketList = new ArrayList<>();
        for(List<RailwayStationConnection> connections : ticketConnections){
            Ticket ticket = createTicket(bookingRequest, connections);
            ticketList.add(ticket);
        }
        booking.setTickets(ticketList);
        return booking;
    }

    private Ticket createTicket(BookingRequestDto2 bookingRequest, List<RailwayStationConnection> connections) {
        Ticket ticket = new Ticket();
        List<Reservation> reservationList = new ArrayList<>();
        for(RailwayStationConnection connection : connections){
            if(ticket.getOriginId() == -1){
                ticket.setOriginId(connection.getDepartureStation().getId());
            }
            Reservation reservation = makeReservation(bookingRequest, connection);
            reservationList.add(reservation);
            ticket.setDestinationId(connection.getArrivalStation().getId());
        }
        ticket.setReservations(reservationList);
        return ticket;
    }

    private Reservation makeReservation(BookingRequestDto2 bookingRequest, RailwayStationConnection connection) {
        TrainCarType trainCarType = bookingRequest.getTrainCarType();
        TrainCar trainCar = findAvailableTrainCar(trainCarType, connection);
        if(trainCar != null){
            Reservation reservation = createReservation(connection, trainCar, connection.getDate());
            return reservation;
        } else {
            throw new NoTrainCarAvailable(connection);
        }
    }

    private TrainCar findAvailableTrainCar(TrainCarType type, RailwayStationConnection connection) {
        String trainCode = connection.getTrainConnection().getCode();
        List<TrainCar> requestedTrainCars = getTrainCarsByCodeAndType(trainCode, type);
        if(requestedTrainCars.isEmpty()){
            throw new NoTrainCarAvailable(connection);
        }
        TrainCar trainCar = findTrainCarWithEnoughCapacity(connection,
                requestedTrainCars, connection.getDate());
        return trainCar;
    }

    private boolean isTicketForToday(LocalDate ticketDate){
        return ticketDate.equals(LocalDate.now());
    }

    private boolean isTimeOk(RailwayStationConnection connection) {
        return connection.getDepartureTime().isBefore(LocalTime.now());
    }

    private TrainCar findTrainCarWithEnoughCapacity(RailwayStationConnection connection,
                                                    List<TrainCar> requestedTrainCars,
                                                    LocalDate ticketDate) {
        for(TrainCar trainCar : requestedTrainCars){
            long usedCapacity = reservationDao.getCntTickets(
                    connection.getDepartureStation().getId(),
                    connection.getArrivalStation().getId(),
                    ticketDate,
                    connection.getTrainConnection().getCode(),
                    trainCar.getId()
            );
            if((trainCar.getCapacity() - usedCapacity) > 0){
                return trainCar;
            }
        }
        return null;
    }

    private List<TrainCar> getTrainCarsByCodeAndType(String trainCode, TrainCarType type){
        List<TrainCar> requestedTrainCars = trainConnectionManager
                .findAllTrainCarsByCode(trainCode)
                .stream()
                .filter(trainCar -> trainCar.getType() == type)
                .sorted(new TrainCarComparator())
                .collect(Collectors.toList());
        return requestedTrainCars;
    }

    private Reservation createReservation(RailwayStationConnection connection,
                                          TrainCar trainCar,
                                          LocalDate ticketDate) {
        Reservation reservation = new Reservation();
        reservation.setOriginId(connection.getDepartureStation().getId());
        reservation.setDestinationId(connection.getArrivalStation().getId());
        reservation.setTrainCode(connection.getTrainConnection().getCode());
        reservation.setTrainCarId(trainCar.getId());
        reservation.setDate(ticketDate);
        reservation.setDepartureTime(connection.getDepartureTime());
        reservation.setArrivalTime(connection.getArrivalTime());
        return reservation;
    }

    private class TrainCarComparator implements Comparator<TrainCar> {
        @Override
        public int compare(TrainCar o1, TrainCar o2) {
            return o1.getNumber() - o2.getNumber();
        }
    }

    @Override
    public BookingDto findBookingById(Long id) {
        railwayStationManager.findAllRailwayStations(); // cache
        Booking booking = bookingDao.findById(id);
        if(booking == null){
            throw new BookingNotFound();
        }
        BookingDto bookingDto = createBookingDto(booking);
        return bookingDto;
    }

    private BookingDto createBookingDto(Booking booking) {
        BookingDto bookingDto = new BookingDto();
        RailwayStation originStation = railwayStationManager
                .findRailwayStationById(booking.getOriginId());
        RailwayStation destinationStation = railwayStationManager
                .findRailwayStationById(booking.getDestinationId());
        bookingDto.setOriginId(originStation.getId());
        bookingDto.setOriginStationName(originStation.getName());
        bookingDto.setDestinationId(destinationStation.getId());
        bookingDto.setDestinationStationName(destinationStation.getName());
        bookingDto.setId(booking.getId());
        bookingDto.setDepartureDate(booking.getDepartureDate());

        List<TicketDto> ticketDtos = createTicketDtos(booking);
        bookingDto.setTickets(ticketDtos);

        return bookingDto;
    }

    private List<TicketDto> createTicketDtos(Booking booking) {
        List<TicketDto> ticketDtos = new ArrayList<>();
        for(Ticket ticket : booking.getTickets()){
            TicketDto ticketDto = new TicketDto();
            ticketDto.setId(ticket.getId());
            ticketDto.setOriginId(ticket.getOriginId());
            ticketDto.setDestinationId(ticket.getDestinationId());
            ticketDto.setTrainCode(ticket.getReservations().get(0).getTrainCode());

            List<StopDto> stopDtos = createStopDtos(ticket);
            ticketDto.setStops(stopDtos);
            ticketDtos.add(ticketDto);
        }
        return ticketDtos;
    }

    private List<StopDto> createStopDtos(Ticket ticket) {
        List<StopDto> stopDtos = new ArrayList<>();
        for(Reservation reservation : ticket.getReservations()){
            StopDto stopDto = new StopDto();

            RailwayStationConnectionDto connectionDto = new RailwayStationConnectionDto();
            connectionDto.setDepartureTime(reservation.getDepartureTime());
            connectionDto.setArrivalTime(reservation.getArrivalTime());
            connectionDto.setDate(reservation.getDate());

            RailwayStation departureStation = railwayStationManager
                    .findRailwayStationById(reservation.getOriginId());
            RailwayStation arrivalStation = railwayStationManager
                    .findRailwayStationById(reservation.getDestinationId());

            connectionDto.setArrivalStation(arrivalStation);
            connectionDto.setDepartureStation(departureStation);

            stopDto.setConnection(connectionDto);

            ReservationDto reservationDto = new ReservationDto();
            reservationDto.setId(reservation.getId());

            TrainCarDto trainCarDto = new TrainCarDto();
            Optional<TrainCar> trainCar = trainConnectionManager
                    .findAllTrainCarsByCode(reservation.getTrainCode())
                    .stream()
                    .filter(c -> c.getId() == reservation.getTrainCarId())
                    .findFirst();

            if(trainCar.isPresent()){
                trainCarDto.setType(trainCar.get().getType());
                trainCarDto.setNumber(trainCar.get().getNumber());
                trainCarDto.setId(trainCar.get().getId());
                reservationDto.setTrainCar(trainCarDto);
            }

            stopDto.setReservation(reservationDto);
            stopDtos.add(stopDto);
        }
        return stopDtos;
    }
}
