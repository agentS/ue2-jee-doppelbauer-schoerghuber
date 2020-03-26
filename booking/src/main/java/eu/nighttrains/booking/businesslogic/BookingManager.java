package eu.nighttrains.booking.businesslogic;

import eu.nighttrains.booking.dto.BookingDto;
import eu.nighttrains.booking.dto.BookingRequestDto;

public interface BookingManager {
    Long book(BookingRequestDto bookingRequest);
    BookingDto findBookingById(Long id);
}
