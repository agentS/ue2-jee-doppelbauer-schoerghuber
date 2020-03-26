package eu.nighttrains.booking.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
public class Reservation {
    @Id
    @GeneratedValue
    private Long id;

    private long originId;
    private long destinationId;
    private String trainCode;
    private long trainCarId;
    private LocalDate bookingDate;

    public Reservation() {
    }

    public Reservation(long originId, long destinationId, String trainCode, long trainCarId) {
        this.originId = originId;
        this.destinationId = destinationId;
        this.trainCode = trainCode;
        this.trainCarId = trainCarId;
    }

    public Long getId() {
        return id;
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

    public String getTrainCode() {
        return trainCode;
    }

    public void setTrainCode(String trainCode) {
        this.trainCode = trainCode;
    }

    public long getTrainCarId() {
        return trainCarId;
    }

    public void setTrainCarId(long trainCarId) {
        this.trainCarId = trainCarId;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }
}
