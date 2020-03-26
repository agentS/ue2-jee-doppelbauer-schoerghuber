package eu.nighttrains.booking.businesslogic;

import eu.nighttrains.booking.domain.BookingRequest;

public interface BookingManager {
    Long addBooking(BookingRequest bookingRequest);
}
