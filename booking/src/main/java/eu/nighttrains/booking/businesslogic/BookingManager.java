package eu.nighttrains.booking.businesslogic;

import eu.nighttrains.booking.dto.BookingRequestDto;

public interface BookingManager {
    Long book(BookingRequestDto bookingRequest);
}
