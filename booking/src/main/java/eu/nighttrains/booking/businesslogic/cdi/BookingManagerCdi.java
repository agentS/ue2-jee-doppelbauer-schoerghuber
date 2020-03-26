package eu.nighttrains.booking.businesslogic.cdi;

import eu.nighttrains.booking.businesslogic.BookingManager;
import eu.nighttrains.booking.businesslogic.DestinationManager;
import eu.nighttrains.booking.businesslogic.TrainConnectionManager;
import eu.nighttrains.booking.businesslogic.exception.BookingNotPossible;
import eu.nighttrains.booking.businesslogic.exception.NoConnectionsAvailable;
import eu.nighttrains.booking.businesslogic.exception.NoTrainCarAvailable;
import eu.nighttrains.booking.dal.BookingDao;
import eu.nighttrains.booking.dal.TicketDao;
import eu.nighttrains.booking.domain.*;
import eu.nighttrains.booking.dto.BookingConnectionDto;
import eu.nighttrains.booking.dto.BookingRequestDto;
import eu.nighttrains.booking.logging.Logger;
import eu.nighttrains.booking.logging.LoggerQualifier;
import eu.nighttrains.booking.logging.LoggerType;
import eu.nighttrains.booking.model.Booking;
import eu.nighttrains.booking.model.Ticket;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
@Transactional
public class BookingManagerCdi implements BookingManager {
    private BookingDao bookingDao;
    private TicketDao ticketDao;
    private DestinationManager destinationManager;
    private TrainConnectionManager trainConnectionManager;
    private Logger logger;

    @Inject
    public BookingManagerCdi(BookingDao bookingDao,
                             TicketDao ticketDao,
                             DestinationManager destinationManager,
                             TrainConnectionManager trainConnectionManager,
                             @LoggerQualifier(type = LoggerType.CONSOLE) Logger logger) {
        this.bookingDao = bookingDao;
        this.ticketDao = ticketDao;
        this.destinationManager = destinationManager;
        this.trainConnectionManager = trainConnectionManager;
        this.logger = logger;
    }

    @Override
    public Long book(BookingRequestDto bookingRequest){
        try{
            return addBooking(bookingRequest);
        } catch(NoConnectionsAvailable ex) {
            throw new BookingNotPossible("NoConnectionsAvailable");
        } catch(NoTrainCarAvailable ex) {
            throw new BookingNotPossible("NoTrainCarAvailable",
                    ex.getConnection());
        }
    }


    public Long addBooking(BookingRequestDto bookingRequest) {
        long originId = bookingRequest.getOriginId();
        long destinationId = bookingRequest.getDestinationId();
        LocalDate ticketDate = bookingRequest.getJourneyStartDate();
        List<Ticket> tickets = new ArrayList<>();

        RailwayStationConnection prevConnection = null;
        for(BookingConnectionDto bookingConnection : bookingRequest.getBookingConnections()){
            long connectionOriginId = bookingConnection.getOriginId();
            long connectionDestinationId = bookingConnection.getDestinationId();
            List<RailwayStationConnection> rsConnections =
                    destinationManager.getConnections(connectionOriginId, connectionDestinationId);
            if(rsConnections.isEmpty()){
                throw new NoConnectionsAvailable();
            }
            if(isTicketForToday(ticketDate) && isTimeOk(rsConnections.get(0))){
                throw new NoConnectionsAvailable();
            }

            TrainCarType trainCarType = bookingConnection.getTrainCarType();
            for(RailwayStationConnection rsConnection : rsConnections){
                ticketDate = calcTicketDate(prevConnection, rsConnection, ticketDate);
                TrainCar trainCar = findAvailableTrainCarForBooking(
                        trainCarType, rsConnection, ticketDate);
                if(trainCar != null){
                    Ticket ticket = createTicket(rsConnection, trainCar, ticketDate);
                    tickets.add(ticket);
                } else {
                    throw new NoTrainCarAvailable(rsConnection);
                }
                prevConnection = rsConnection;
            }
        }
        Booking booking = createBooking(originId, destinationId, tickets);
        return booking.getId();
    }

    private boolean isTicketForToday(LocalDate ticketDate){
        return ticketDate.equals(LocalDate.now());
    }

    private boolean isTimeOk(RailwayStationConnection connection) {
        return connection.getDepartureTime().isBefore(LocalTime.now());
    }

    private LocalDate calcTicketDate(RailwayStationConnection prevConnection,
                                     RailwayStationConnection connection,
                                     LocalDate ticketDate) {
        if(prevConnection != null && prevConnection.getDepartureTime().isAfter(connection.getDepartureTime())){
            ticketDate = ticketDate.plusDays(1);
        }
        return ticketDate;
    }

    private TrainCar findAvailableTrainCarForBooking(TrainCarType trainCarType,
                                                     RailwayStationConnection connection,
                                                     LocalDate ticketDate) {
        String trainCode = connection.getTrainConnection().getCode();
        List<TrainCar> requestedTrainCars = getTrainCarsByCodeAndType(trainCode, trainCarType);
        if(requestedTrainCars.isEmpty()){
            throw new NoTrainCarAvailable(connection);
        }

        TrainCar trainCar = findTrainCarWithEnoughCapacity(connection, requestedTrainCars, ticketDate);
        return trainCar;
    }

    private TrainCar findTrainCarWithEnoughCapacity(RailwayStationConnection connection,
                                                    List<TrainCar> requestedTrainCars,
                                                    LocalDate ticketDate) {
        for(TrainCar trainCar : requestedTrainCars){
            long usedCapacity = ticketDao.getCntTickets(
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

    private Ticket createTicket(RailwayStationConnection connection,
                                TrainCar trainCar,
                                LocalDate ticketDate) {
        Ticket ticket = new Ticket();
        ticket.setOriginId(connection.getDepartureStation().getId());
        ticket.setDestinationId(connection.getArrivalStation().getId());
        ticket.setTrainCode(connection.getTrainConnection().getCode());
        ticket.setTrainCarId(trainCar.getId());
        ticket.setBookingDate(ticketDate);
        return ticket;
    }

    private Booking createBooking(long originId, long destinationId, List<Ticket> tickets) {
        List<Ticket> mergedTickets = ticketDao.bulkMerge(tickets);
        Booking booking = new Booking();
        booking.setOriginId(originId);
        booking.setDestinationId(destinationId);
        booking.setTickets(mergedTickets);
        booking = bookingDao.merge(booking);
        return booking;
    }

    private class TrainCarComparator implements Comparator<TrainCar> {
        @Override
        public int compare(TrainCar o1, TrainCar o2) {
            return o1.getNumber() - o2.getNumber();
        }
    }
}
