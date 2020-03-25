package eu.nighttrains.booking.dal.jpa;

import eu.nighttrains.booking.dal.BookingDao;
import eu.nighttrains.booking.model.Booking;

import javax.enterprise.context.RequestScoped;
import javax.transaction.Transactional;

@RequestScoped
@Transactional
public class BookingDaoJpa extends AbstractDaoBean<Booking, Long> implements BookingDao {
}
