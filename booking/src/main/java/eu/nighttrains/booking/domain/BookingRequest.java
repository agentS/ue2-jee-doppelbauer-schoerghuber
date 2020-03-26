package eu.nighttrains.booking.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookingRequest {
    private long originId;
    private long destinationId;
    private LocalDate journeyStartDate;
    private List<BookingConnection> bookingConnections = new ArrayList<>();

    public BookingRequest(){
    }

    public BookingRequest(LocalDate journeyStartDate) {
        this.journeyStartDate = journeyStartDate;
    }

    public LocalDate getJourneyStartDate() {
        return journeyStartDate;
    }

    public void setJourneyStartDate(LocalDate journeyStartDate) {
        this.journeyStartDate = journeyStartDate;
    }

    public List<BookingConnection> getBookingConnections() {
        return bookingConnections;
    }

    public void setBookingConnections(List<BookingConnection> bookingConnections) {
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
