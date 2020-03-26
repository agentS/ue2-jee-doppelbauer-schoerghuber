package eu.nighttrains.booking.businesslogic.cdi;

import eu.nighttrains.booking.businesslogic.BookingManager;
import eu.nighttrains.booking.businesslogic.DestinationManager;
import eu.nighttrains.booking.businesslogic.RailwayStationManager;
import eu.nighttrains.booking.businesslogic.TrainConnectionManager;
import eu.nighttrains.booking.dal.BookingDao;
import eu.nighttrains.booking.dal.TicketDao;
import eu.nighttrains.booking.dto.*;
import eu.nighttrains.booking.logging.Logger;
import eu.nighttrains.booking.logging.LoggerQualifier;
import eu.nighttrains.booking.logging.LoggerType;
import eu.nighttrains.booking.model.Booking;
import eu.nighttrains.booking.model.Ticket;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.NotSupportedException;
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
    @Inject
    private BookingDao bookingDao;
    @Inject
    private TicketDao ticketDao;
    @Inject
    private DestinationManager destinationManager;
    @Inject
    private RailwayStationManager railwayStationManager;
    @Inject
    private TrainConnectionManager trainConnectionManager;
    @Inject @LoggerQualifier(type = LoggerType.CONSOLE)
    Logger logger;

    public BookingManagerCdi(){
    }

    @Override
    public Long addBooking(BookingRequestDto bookingRequest) {
        long originId = bookingRequest.getOriginId();
        long destinationId = bookingRequest.getDestinationId();
        LocalDate ticketDate = bookingRequest.getJourneyStartDate();

        /*for(BookingConnectionDto connection : bookingRequest.getBookingConnections()){
            long connectionOriginId = connection.getOriginId();
            long connectionDestinationId = connection.getDestinationId();
            List<RailwayStationConnectionDto> connections =
                    destinationManager.getConnections(connectionOriginId, connectionDestinationId);
            if(connections.isEmpty()){
                throw new NoConnectionsAvailable();
            } else if(connections.size() > 1) {
                // only single connections are allowed
                throw new NoConnectionsAvailable();
            }
            if(isTicketForToday(ticketDate) && isTimeOk(connections.get(0))){
                throw new NoConnectionsAvailable();
            }

            RailwayStationConnectionDto rsConnection = connections.get(0);
        }*/

        List<RailwayStationConnectionDto> connections =
                destinationManager.getConnections(originId, destinationId);
        if(connections.isEmpty()){
            throw new NoConnectionsAvailable();
        }
        if(isTicketForToday(ticketDate) && isTimeOk(connections.get(0))){
            throw new NoConnectionsAvailable();
        }
        List<Ticket> tickets = bookAllConnections(bookingRequest, connections);
        Booking booking = createBooking(bookingRequest, tickets);
        return booking.getId();
    }

    private boolean isTicketForToday(LocalDate ticketDate){
        return ticketDate.equals(LocalDate.now());
    }

    private boolean isTimeOk(RailwayStationConnectionDto connection) {
        return connection.getDepartureTime().isBefore(LocalTime.now());
    }

    public List<Ticket> bookAllConnections(BookingRequestDto bookingRequest,
                                            List<RailwayStationConnectionDto> connections) {
        LocalDate ticketDate = bookingRequest.getJourneyStartDate();
        TrainCarType trainCarType = bookingRequest.getTrainCarType();
        List<Ticket> tickets = new ArrayList<>();
        RailwayStationConnectionDto prevConnection = null;

        for(RailwayStationConnectionDto connection : connections){
            ticketDate = calcTicketDate(prevConnection, connection, ticketDate);
            TrainCarDto trainCar = findAvailableTrainCarForBooking(trainCarType, connection, ticketDate);
            if(trainCar != null){
                Ticket ticket = createTicket(connection, trainCar, ticketDate);
                tickets.add(ticket);
            } else {
                throw new NoTrainCarAvailable(connection);
            }
            prevConnection = connection;
        }
        return tickets;
    }

    private LocalDate calcTicketDate(RailwayStationConnectionDto prevConnection,
                                     RailwayStationConnectionDto connection,
                                     LocalDate ticketDate) {
        if(prevConnection != null && prevConnection.getDepartureTime().isAfter(connection.getDepartureTime())){
            ticketDate = ticketDate.plusDays(1);
        }
        return ticketDate;
    }

    private TrainCarDto findAvailableTrainCarForBooking(TrainCarType trainCarType,
                                                       RailwayStationConnectionDto connection,
                                                       LocalDate ticketDate) {
        String trainCode = connection.getTrainConnection().getCode();
        List<TrainCarDto> requestedTrainCars = getTrainCarsByCodeAndType(trainCode, trainCarType);
        if(requestedTrainCars.isEmpty()){
            throw new NoTrainCarAvailable(connection);
        }

        TrainCarDto trainCar = findTrainCarWithEnoughCapacity(connection, requestedTrainCars, ticketDate);
        return trainCar;
    }

    private TrainCarDto findTrainCarWithEnoughCapacity(RailwayStationConnectionDto connection,
                                                       List<TrainCarDto> requestedTrainCars,
                                                       LocalDate ticketDate) {
        for(TrainCarDto trainCar : requestedTrainCars){
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

    private List<TrainCarDto> getTrainCarsByCodeAndType(String trainCode, TrainCarType type){
        List<TrainCarDto> requestedTrainCars = trainConnectionManager
                .findAllTrainCarsByCode(trainCode)
                .stream()
                .filter(trainCar -> trainCar.getType() == type)
                .sorted(new TrainCarComparator())
                .collect(Collectors.toList());
        return requestedTrainCars;
    }

    private Ticket createTicket(RailwayStationConnectionDto connection,
                                TrainCarDto trainCar,
                                LocalDate ticketDate) {
        Ticket ticket = new Ticket();
        ticket.setOriginId(connection.getDepartureStation().getId());
        ticket.setDestinationId(connection.getArrivalStation().getId());
        ticket.setTrainCode(connection.getTrainConnection().getCode());
        ticket.setTrainCarId(trainCar.getId());
        ticket.setBookingDate(ticketDate);
        return ticket;
    }

    private Booking createBooking(BookingRequestDto bookingRequest, List<Ticket> tickets) {
        List<Ticket> mergedTickets = ticketDao.bulkMerge(tickets);
        Booking booking = new Booking();
        booking.setOriginId(bookingRequest.getOriginId());
        booking.setDestinationId(bookingRequest.getDestinationId());
        booking.setTickets(mergedTickets);
        booking = bookingDao.merge(booking);
        return booking;
    }

    private class TrainCarComparator implements Comparator<TrainCarDto> {
        @Override
        public int compare(TrainCarDto o1, TrainCarDto o2) {
            return o1.getNumber() - o2.getNumber();
        }
    }
}
