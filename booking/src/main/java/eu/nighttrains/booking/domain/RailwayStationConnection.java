package eu.nighttrains.booking.domain;

import java.time.LocalDate;
import java.time.LocalTime;

public class RailwayStationConnection {
    private LocalDate date;
    private RailwayStation departureStation;
    private RailwayStation arrivalStation;
    private TrainConnection trainConnection;
    private LocalTime departureTime;
    private LocalTime arrivalTime;

    public RailwayStationConnection() {}

    public RailwayStationConnection(RailwayStation departureStation, RailwayStation arrivalStation, TrainConnection trainConnection, LocalTime departureTime, LocalTime arrivalTime) {
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
        this.trainConnection = trainConnection;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    public RailwayStation getDepartureStation() {
        return this.departureStation;
    }

    public void setDepartureStation(RailwayStation departureStation) {
        this.departureStation = departureStation;
    }

    public RailwayStation getArrivalStation() {
        return this.arrivalStation;
    }

    public void setArrivalStation(RailwayStation arrivalStation) {
        this.arrivalStation = arrivalStation;
    }

    public TrainConnection getTrainConnection() {
        return this.trainConnection;
    }

    public void setTrainConnection(TrainConnection trainConnection) {
        this.trainConnection = trainConnection;
    }

    public LocalTime getDepartureTime() {
        return this.departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalTime getArrivalTime() {
        return this.arrivalTime;
    }

    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
