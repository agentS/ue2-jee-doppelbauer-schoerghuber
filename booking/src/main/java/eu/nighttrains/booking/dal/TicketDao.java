package eu.nighttrains.booking.dal;

import eu.nighttrains.booking.model.Ticket;

import java.util.List;

public interface TicketDao extends Dao<Ticket, Long> {
    List<Ticket> bulkMerge(List<Ticket> tickets);
}
