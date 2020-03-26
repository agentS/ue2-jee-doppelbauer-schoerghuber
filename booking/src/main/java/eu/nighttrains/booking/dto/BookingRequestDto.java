package eu.nighttrains.booking.dto;

import java.time.LocalDate;
import java.util.List;

public class BookingRequestDto {
    private long originId;
    private long destinationId;
    private LocalDate journeyStartDate;
    private List<BookingConnectionDto> bookingConnections;

    public BookingRequestDto(){
    }

    public BookingRequestDto(LocalDate journeyStartDate) {
        this.journeyStartDate = journeyStartDate;
    }

    public LocalDate getJourneyStartDate() {
        return journeyStartDate;
    }

    public void setJourneyStartDate(LocalDate journeyStartDate) {
        this.journeyStartDate = journeyStartDate;
    }

    public List<BookingConnectionDto> getBookingConnections() {
        return bookingConnections;
    }

    public void setBookingConnections(List<BookingConnectionDto> bookingConnections) {
        this.bookingConnections = bookingConnections;
    }

    public long getOriginId() {
        return originId;
    }

    public void setOriginId(long originId) {
        this.originId = originId;
    }

    public long getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(long destinationId) {
        this.destinationId = destinationId;
    }
}
