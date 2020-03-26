package eu.nighttrains.booking.businesslogic.cdi;

import eu.nighttrains.booking.businesslogic.BookingManager;
import eu.nighttrains.booking.businesslogic.DestinationManager;
import eu.nighttrains.booking.businesslogic.RailwayStationManager;
import eu.nighttrains.booking.businesslogic.TrainConnectionManager;
import eu.nighttrains.booking.dal.BookingDao;
import eu.nighttrains.booking.dal.TicketDao;
import eu.nighttrains.booking.dto.BookingRequestDto;
import eu.nighttrains.booking.dto.RailwayStationConnectionDto;
import eu.nighttrains.booking.dto.TrainCarDto;
import eu.nighttrains.booking.dto.TrainCarType;
import eu.nighttrains.booking.logging.Logger;
import eu.nighttrains.booking.logging.LoggerQualifier;
import eu.nighttrains.booking.logging.LoggerType;
import eu.nighttrains.booking.model.Booking;
import eu.nighttrains.booking.model.Ticket;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.awt.print.Book;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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
        List<RailwayStationConnectionDto> connections =
                destinationManager.getConnections(bookingRequest.getOriginId(),
                        bookingRequest.getDestinationId());
        if(connections.isEmpty()){
            throw new BookingNotPossible("there are no connections available.");
        }
        if(bookingRequest.getJourneyStartDate().equals(LocalDate.now())
                && connections.get(0).getDepartureTime().isBefore(LocalTime.now())){
            throw new BookingNotPossible("you have already missed the train.");
        }

        List<Ticket> tickets = bookAllConnections(bookingRequest, connections);
        Booking booking = createBooking(bookingRequest, tickets);
        return booking.getId();
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

    public List<Ticket> bookAllConnections(BookingRequestDto bookingRequest,
                                            List<RailwayStationConnectionDto> connections) {
        LocalDate ticketDate = bookingRequest.getJourneyStartDate();
        List<Ticket> tickets = new ArrayList<>();
        RailwayStationConnectionDto prevConnection = null;

        for(RailwayStationConnectionDto connection : connections){
            ticketDate = calcTicketDate(prevConnection, connection, ticketDate);
            TrainCarDto trainCar = findAvailableTrainCarForBooking(bookingRequest, connection, ticketDate);

            if(trainCar != null){
                Ticket ticket = createTicket(connection, trainCar, ticketDate);
                tickets.add(ticket);
            } else {
                throw new BookingNotPossible("no train-car available for this connection");
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

    private TrainCarDto findAvailableTrainCarForBooking(BookingRequestDto bookingRequest,
                                                       RailwayStationConnectionDto connection,
                                                       LocalDate ticketDate) {
        String trainCode = connection.getTrainConnection().getCode();
        List<TrainCarDto> requestedTrainCars = getTrainCarsByCodeAndType(trainCode,
                bookingRequest.getTrainCarType());
        if(requestedTrainCars.isEmpty()){
            throw new BookingNotPossible("no train-cars are available for this connection");
        }

        TrainCarDto trainCar = findTrainCarWithEnoughCapacity(bookingRequest, requestedTrainCars);
        return trainCar;
    }

    private TrainCarDto findTrainCarWithEnoughCapacity(BookingRequestDto bookingRequest,
                                                       List<TrainCarDto> requestedTrainCars) {
        for(TrainCarDto trainCar : requestedTrainCars){
            // TODO: check for capacity
            return trainCar;
        }
        return null;
    }

    private List<TrainCarDto> getTrainCarsByCodeAndType(String trainCode, TrainCarType type){
        List<TrainCarDto> requestedTrainCars = trainConnectionManager
                .findAllTrainCarsByCode(trainCode)
                .stream()
                .filter(trainCar -> trainCar.getType() == type)
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
}
