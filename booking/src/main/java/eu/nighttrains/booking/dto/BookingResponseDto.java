package eu.nighttrains.booking.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

public class BookingResponseDto {
    @PositiveOrZero
    private Long bookingId;

    @NotBlank
    private String tracingId;

    public BookingResponseDto() {}

    public BookingResponseDto(Long bookingId, String tracingId) {
        this.bookingId = bookingId;
        this.tracingId = tracingId;
    }

    public Long getBookingId() {
        return this.bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public String getTracingId() {
        return this.tracingId;
    }

    public void setTracingId(String tracingId) {
        this.tracingId = tracingId;
    }
}
