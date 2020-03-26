package eu.nighttrains.booking.dal;

import eu.nighttrains.booking.model.Reservation;

import java.time.LocalDate;
import java.util.List;

public interface ReservationDao extends Dao<Reservation, Long> {
    List<Reservation> bulkMerge(List<Reservation> reservations);
    long getCntTickets(long fromId, long toId, LocalDate date, String trainCode, long trainCarId);
}
