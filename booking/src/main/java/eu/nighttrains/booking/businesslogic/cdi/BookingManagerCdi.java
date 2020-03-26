package eu.nighttrains.booking.businesslogic.cdi;

import eu.nighttrains.booking.businesslogic.BookingManager;
import eu.nighttrains.booking.businesslogic.DestinationManager;
import eu.nighttrains.booking.businesslogic.RailwayStationManager;
import eu.nighttrains.booking.businesslogic.TrainConnectionManager;
import eu.nighttrains.booking.businesslogic.exception.BookingNotPossible;
import eu.nighttrains.booking.businesslogic.exception.NoConnectionsAvailable;
import eu.nighttrains.booking.businesslogic.exception.NoTrainCarAvailable;
import eu.nighttrains.booking.dal.BookingDao;
import eu.nighttrains.booking.dal.ReservationDao;
import eu.nighttrains.booking.dal.TicketDao;
import eu.nighttrains.booking.domain.*;
import eu.nighttrains.booking.dto.*;
import eu.nighttrains.booking.logging.Logger;
import eu.nighttrains.booking.logging.LoggerQualifier;
import eu.nighttrains.booking.logging.LoggerType;
import eu.nighttrains.booking.model.Booking;
import eu.nighttrains.booking.model.Reservation;
import eu.nighttrains.booking.model.Ticket;
import eu.nighttrains.booking.util.ConnectionDateCalculator;
import eu.nighttrains.booking.util.TicketConnectionSeparator;

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

        Booking booking = book(ticketConnections);
        booking.setOriginId(originId);
        booking.setDestinationId(destinationId);
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

    public Booking book(List<List<RailwayStationConnection>> ticketConnections){
        Booking booking = new Booking();
        List<Ticket> ticketList = new ArrayList<>();
        for(List<RailwayStationConnection> connections : ticketConnections){
            Ticket ticket = createTicket(connections);
            ticketList.add(ticket);
        }
        booking.setTickets(ticketList);
        return booking;
    }

    private Ticket createTicket(List<RailwayStationConnection> connections) {
        Ticket ticket = new Ticket();
        List<Reservation> reservationList = new ArrayList<>();
        for(RailwayStationConnection connection : connections){
            if(ticket.getOriginId() == -1){
                ticket.setOriginId(connection.getDepartureStation().getId());
            }
            Reservation reservation = makeReservation(connection);
            reservationList.add(reservation);
            ticket.setDestinationId(connection.getArrivalStation().getId());
        }
        ticket.setReservations(reservationList);
        return ticket;
    }

    private Reservation makeReservation(RailwayStationConnection connection) {
        TrainCar trainCar = findAvailableTrainCar(TrainCarType.SLEEPER, connection);
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
        reservation.setBookingDate(ticketDate);
        return reservation;
    }

    private class TrainCarComparator implements Comparator<TrainCar> {
        @Override
        public int compare(TrainCar o1, TrainCar o2) {
            return o1.getNumber() - o2.getNumber();
        }
    }

    // ---------------------------------------------------------------------------------

    @Override
    public BookingDto findBookingById(Long id) {
        Booking booking = bookingDao.findById(id);
        if(booking == null){
            throw new IllegalArgumentException();
        }
        BookingDto bookingDto = createBookingDto(booking);
        return bookingDto;
    }

    private BookingDto createBookingDto(Booking booking) {
        RailwayStation fromStation = railwayStationManager
                .findRailwayStationById(booking.getOriginId());
        RailwayStation toStation = railwayStationManager
                .findRailwayStationById(booking.getDestinationId());

        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setFrom(fromStation.getName());
        bookingDto.setTo(toStation.getName());
        //bookingDto.setTickets(createTicketDtos(booking.getReservations()));
        return bookingDto;
    }

    private List<TicketDto> createTicketDtos(List<Reservation> reservations) {
        List<TicketDto> ticketDtos = new ArrayList<>();
        for(Reservation reservation : reservations){
            List<RailwayStationConnection> connections = destinationManager
                    .getConnections(reservation.getOriginId(), reservation.getDestinationId());

            TicketDto ticketDto = new TicketDto();
            ticketDto.setDate(reservation.getBookingDate());
            ticketDto.setId(reservation.getId());
            if(connections.size() > 0) {
                RailwayStationConnection connection = connections.get(0);
                ticketDto.setRailwayStationConnection(createRailwayStationConnectionDto(connection));

                TrainConnection trainConnection = connection.getTrainConnection();
                Optional<TrainCar> trainCar = trainConnectionManager
                        .findAllTrainCarsByCode(trainConnection.getCode())
                        .stream()
                        .filter(c -> c.getId() == reservation.getTrainCarId())
                        .findFirst();

                TrainCarDto trainCarDto = new TrainCarDto();
                trainCarDto.setId(trainCar.get().getId());
                trainCarDto.setNumber(trainCar.get().getNumber());
                trainCarDto.setType(trainCar.get().getType());

                TrainConnectionDto trainConnectionDto = createTrainConnectionDto(trainConnection);
                trainConnectionDto.setTrainCar(trainCarDto);
                ticketDto.setTrainConnection(trainConnectionDto);
            }
            ticketDtos.add(ticketDto);
        }
        return ticketDtos;
    }

    private TrainConnectionDto createTrainConnectionDto(TrainConnection trainConnection) {
        TrainConnectionDto trainConnectionDto = new TrainConnectionDto();
        trainConnectionDto.setCode(trainConnection.getCode());
        trainConnectionDto.setId(trainConnection.getId());
        return trainConnectionDto;
    }

    private RailwayStationConnectionDto createRailwayStationConnectionDto(RailwayStationConnection connection) {
        RailwayStationConnectionDto railwayStationConnectionDto = new RailwayStationConnectionDto();
        railwayStationConnectionDto.setArrivalStation(connection.getArrivalStation());
        railwayStationConnectionDto.setDepartureStation(connection.getDepartureStation());
        railwayStationConnectionDto.setArrivalTime(connection.getArrivalTime());
        railwayStationConnectionDto.setDepartureTime(connection.getDepartureTime());
        return railwayStationConnectionDto;
    }
}
