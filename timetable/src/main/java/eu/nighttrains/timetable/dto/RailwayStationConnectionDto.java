package eu.nighttrains.timetable.dto;

import java.time.LocalTime;

public class RailwayStationConnectionDto {
    private RailwayStationDto departureStation;
    private RailwayStationDto arrivalStation;
    private TrainConnectionDto trainConnection;
    private LocalTime departureTime;
    private LocalTime arrivalTime;

    public RailwayStationConnectionDto() {}

    public RailwayStationConnectionDto(RailwayStationDto departureStation, RailwayStationDto arrivalStation, TrainConnectionDto trainConnection, LocalTime departureTime, LocalTime arrivalTime) {
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
        this.trainConnection = trainConnection;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    public RailwayStationDto getDepartureStation() {
        return this.departureStation;
    }

    public void setDepartureStation(RailwayStationDto departureStation) {
        this.departureStation = departureStation;
    }

    public RailwayStationDto getArrivalStation() {
        return this.arrivalStation;
    }

    public void setArrivalStation(RailwayStationDto arrivalStation) {
        this.arrivalStation = arrivalStation;
    }

    public TrainConnectionDto getTrainConnection() {
        return this.trainConnection;
    }

    public void setTrainConnection(TrainConnectionDto trainConnection) {
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
}
