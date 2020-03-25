package eu.nighttrains.booking.dal.jpa;

import eu.nighttrains.booking.dal.TicketDao;
import eu.nighttrains.booking.model.Ticket;

import javax.enterprise.context.RequestScoped;
import javax.transaction.Transactional;

@RequestScoped
@Transactional
public class TicketDaoJpa extends AbstractDaoBean<Ticket, Long> implements TicketDao {
}
