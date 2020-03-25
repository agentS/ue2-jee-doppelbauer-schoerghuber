package eu.nighttrains.booking.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Ticket {
    @Id
    @GeneratedValue
    private Long id;

    private long originId;
    private long destinationId;
    private String trainCode;
    private long trainCarId;

    @Temporal(TemporalType.DATE)
    private Date bookingDate = new Date();

    public Ticket() {
    }

    public Ticket(long originId, long destinationId, String trainCode, long trainCarId) {
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

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }
}
