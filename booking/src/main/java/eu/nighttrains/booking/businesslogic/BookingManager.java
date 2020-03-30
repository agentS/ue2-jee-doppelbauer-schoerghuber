package eu.nighttrains.booking.businesslogic;

import eu.nighttrains.booking.dto.BookingDto;
import eu.nighttrains.booking.dto.BookingRequestDto2;

public interface BookingManager {
    Long book(BookingRequestDto2 bookingRequest);
    BookingDto findBookingById(Long id);
}
