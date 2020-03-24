package eu.nighttrains.booking.rest.resource;

import eu.nighttrains.booking.model.Ticket;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("hello")
public class HelloResource {
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
}
