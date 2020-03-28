package eu.nighttrains.booking.dal.jpa;

import eu.nighttrains.booking.dal.TicketDao;
import eu.nighttrains.booking.model.Ticket;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class TicketDaoJpa extends AbstractDaoBean<Ticket, Long> implements TicketDao {
    @Override
    public List<Ticket> bulkMerge(List<Ticket> tickets) {
        EntityManager entityManager = getEntityManager();
        List<Ticket> mergedTickets = new ArrayList<>();
        for(Ticket ticket : tickets){
            Ticket mergedTicket = entityManager.merge(ticket);
            mergedTickets.add(mergedTicket);
        }
        return mergedTickets;
    }
}
