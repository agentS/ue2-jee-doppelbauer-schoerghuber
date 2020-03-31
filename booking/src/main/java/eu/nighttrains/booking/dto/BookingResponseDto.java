package eu.nighttrains.booking.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

public class BookingResponseDto {
    @PositiveOrZero
    private Long bookingId;

    public BookingResponseDto() {}

    public BookingResponseDto(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Long getBookingId() {
        return this.bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }
}
