package eu.nighttrains.timetable.model;

import javax.persistence.*;
import java.time.LocalTime;
import java.time.ZonedDateTime;

@Entity
public class RailwayStationConnection {
    @EmbeddedId
    private RailwayStationConnectionKey id;

    @ManyToOne(optional = false)
    @MapsId("departureStationId")
    private RailwayStation departureStation;

    @ManyToOne(optional = false)
    @MapsId("arrivalStationId")
    private RailwayStation arrivalStation;

    @Column(nullable = false)
    private LocalTime departureTime;

    @Column(nullable = false)
    private LocalTime arrivalTime;

    @ManyToOne(optional = false, cascade = { CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE })
    @MapsId("trainConnectionId")
    private TrainConnection trainConnection;

    public RailwayStationConnection() {}

    public RailwayStationConnectionKey getId() {
        return this.id;
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

    public TrainConnection getTrainConnection() {
        return this.trainConnection;
    }

    public void setTrainConnection(TrainConnection trainConnection) {
        this.trainConnection = trainConnection;
    }
}
