package eu.nighttrains.booking.rest.resource;

import eu.nighttrains.booking.model.Ticket;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.UserTransaction;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Path("/")
public class HelloWorldResource {
    @Resource
    private UserTransaction utx;
    @PersistenceContext(unitName = "prod")
    private EntityManager entityManager;

    @GET
    @Path("hello")
    @Produces(MediaType.APPLICATION_JSON)
    public String sayHello(){
        addTicket();
        return "hello booking service XD " + entityManager.toString();
    }

    @Transactional
    public void addTicket2(){
        entityManager.persist(new Ticket("hello ticket"));
    }

    public void addTicket(){
        if(entityManager != null){
            System.out.println("entitymanager is not null");
            try {
                utx.begin();
                entityManager.persist(new Ticket("hello ticket"));
                utx.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
