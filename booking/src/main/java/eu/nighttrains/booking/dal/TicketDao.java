package eu.nighttrains.booking.dal;

import eu.nighttrains.booking.model.Ticket;

import java.time.LocalDate;
import java.util.List;

public interface TicketDao extends Dao<Ticket, Long> {
    List<Ticket> bulkMerge(List<Ticket> tickets);
    int getCntTickets(long fromId, long toId, LocalDate date, String trainCode, long trainCarId);
}
