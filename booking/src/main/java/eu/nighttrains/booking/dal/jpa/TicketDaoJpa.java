package eu.nighttrains.booking.dal.jpa;

import eu.nighttrains.booking.dal.TicketDao;
import eu.nighttrains.booking.model.Ticket;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
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

    @Override
    public long getCntTickets(long fromId, long toId, LocalDate date, String trainCode, long trainCarId) {
        EntityManager entityManager = getEntityManager();
        TypedQuery<Long> query = entityManager
                .createQuery("SELECT COUNT(T) FROM Ticket AS T " +
                        "WHERE T.originId = :originId " +
                        "AND T.destinationId = :destinationId " +
                        "AND T.bookingDate = :date " +
                        "AND T.trainCode = :trainCode " +
                        "AND T.trainCarId = :trainCarId", Long.class);
        query.setParameter("originId", fromId);
        query.setParameter("destinationId", toId);
        query.setParameter("date", date);
        query.setParameter("trainCode", trainCode);
        query.setParameter("trainCarId", trainCarId);

        Long cnt = query.getSingleResult();
        return cnt;
    }
}
