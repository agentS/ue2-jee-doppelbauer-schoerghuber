package eu.nighttrains.booking.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Reservation {
    @Id
    @GeneratedValue
    private Long id;

    private long originId;
    private long destinationId;
    private String trainCode;
    private long trainCarId;
    private LocalDate date;
    private LocalTime departureTime;
    private LocalTime arrivalTime;

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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
}
