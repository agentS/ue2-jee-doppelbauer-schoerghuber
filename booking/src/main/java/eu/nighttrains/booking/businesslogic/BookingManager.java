package eu.nighttrains.booking.businesslogic;

import eu.nighttrains.booking.dto.BookingRequestDto;

public interface BookingManager {
    Long addBooking(BookingRequestDto bookingRequest);
}
