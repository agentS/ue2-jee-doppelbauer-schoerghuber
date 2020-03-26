package eu.nighttrains.booking.dto;

import eu.nighttrains.booking.domain.RailwayStation;

import java.time.LocalTime;

public class RailwayStationConnectionDto {
    private RailwayStation departureStation;
    private RailwayStation arrivalStation;
    private LocalTime departureTime;
    private LocalTime arrivalTime;

    public RailwayStationConnectionDto() {}

    public RailwayStationConnectionDto(RailwayStation departureStation, RailwayStation arrivalStation, LocalTime departureTime, LocalTime arrivalTime) {
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
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
}
