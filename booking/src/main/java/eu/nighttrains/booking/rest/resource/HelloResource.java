package eu.nighttrains.booking.rest.resource;

import eu.nighttrains.booking.businesslogic.BookingManager;
import eu.nighttrains.booking.domain.RailwayStationConnection;
import eu.nighttrains.booking.domain.TrainCarType;
import eu.nighttrains.booking.dto.BookingConnectionDto;
import eu.nighttrains.booking.dto.BookingRequestDto;
import eu.nighttrains.booking.logging.Logger;
import eu.nighttrains.booking.logging.LoggerQualifier;
import eu.nighttrains.booking.logging.LoggerType;
import eu.nighttrains.booking.model.Reservation;
import eu.nighttrains.booking.client.DestinationsClient;
import eu.nighttrains.booking.client.UnknownUriException;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
@Path("hello")
public class HelloResource {
    @Inject
    @RestClient
    private DestinationsClient destinationsClient;

    @Inject
    private BookingManager bookingManager;

    @Inject @LoggerQualifier(type = LoggerType.CONSOLE)
    private Logger logger;

    @PersistenceContext
    private EntityManager entityManager;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public String sayHello(){
        entityManager.persist(new Reservation(1,2,"NIGHTJET",1));
        return "Hello from Wildfly";
    }

    @GET
    @Path("/book")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public String book(){
        BookingRequestDto bookingRequest = new BookingRequestDto();
        bookingRequest.setDestinationId(4);
        bookingRequest.setOriginId(0);
        bookingRequest.setJourneyStartDate(LocalDate.now()); // TODO: check if start is at possible time
        bookingRequest.getBookingConnections().add(new BookingConnectionDto(0,4,TrainCarType.SLEEPER));

        //bookingManager.book(bookingRequest);

        return "Hello from Wildfly";
    }

    @GET
    @Path("/destinations")
    @Produces(MediaType.APPLICATION_JSON)
    public List<RailwayStationConnection> getConnections(){
        try {
            if(destinationsClient != null){
                logger.info("client is not null.");
            } else {
                logger.info("client is null.");
            }
            return destinationsClient.getConnections(0,1);
        } catch (UnknownUriException ex) {
            System.err.println("The given URI is not formatted correctly." + ex.getMessage() + ex.getClass().toString());
        } catch (ProcessingException ex){
            handleProcessingException(ex);
        }
        return null;
    }

    private void handleProcessingException(ProcessingException ex) {
        Throwable rootEx = ex;
        if (rootEx != null && (rootEx instanceof UnknownHostException
                || rootEx instanceof ConnectException)) {
            System.err.println("The specified host is unknown.");
        } else {
            throw ex;
        }
    }
}
