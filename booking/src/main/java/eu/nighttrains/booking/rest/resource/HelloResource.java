package eu.nighttrains.booking.rest.resource;

import eu.nighttrains.booking.dto.RailwayStationConnectionDto;
import eu.nighttrains.booking.logging.Logger;
import eu.nighttrains.booking.logging.LoggerQualifier;
import eu.nighttrains.booking.logging.LoggerType;
import eu.nighttrains.booking.model.Ticket;
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
import java.util.List;

@ApplicationScoped
@Path("hello")
public class HelloResource {
    @Inject
    @RestClient
    private DestinationsClient destinationsClient;

    @Inject @LoggerQualifier(type = LoggerType.CONSOLE)
    Logger logger;

    @PersistenceContext
    private EntityManager entityManager;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public String sayHello(){
        entityManager.persist(new Ticket("hallo wildfly"));
        return "Hello from Wildfly";
    }

    @GET
    @Path("/destinations")
    @Produces(MediaType.APPLICATION_JSON)
    public List<RailwayStationConnectionDto> getConnections(){
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
