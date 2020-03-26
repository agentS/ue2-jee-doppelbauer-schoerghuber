package eu.nighttrains.booking.dal.jpa;

import eu.nighttrains.booking.dal.ReservationDao;
import eu.nighttrains.booking.model.Reservation;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationDaoJpa extends AbstractDaoBean<Reservation, Long> implements ReservationDao {
    @Override
    public List<Reservation> bulkMerge(List<Reservation> reservations) {
        EntityManager entityManager = getEntityManager();
        List<Reservation> mergedReservations = new ArrayList<>();
        for(Reservation reservation : reservations){
            Reservation mergedReservation = entityManager.merge(reservation);
            mergedReservations.add(mergedReservation);
        }
        return mergedReservations;
    }

    @Override
    public long getCntTickets(long fromId, long toId, LocalDate date, String trainCode, long trainCarId) {
        EntityManager entityManager = getEntityManager();
        TypedQuery<Long> query = entityManager
                .createQuery("SELECT COUNT(T) FROM Reservation AS T " +
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
