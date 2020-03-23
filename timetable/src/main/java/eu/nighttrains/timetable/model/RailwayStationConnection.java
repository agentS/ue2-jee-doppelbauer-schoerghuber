package eu.nighttrains.timetable.model;

import com.vladmihalcea.hibernate.type.array.LongArrayType;
import eu.nighttrains.timetable.route.SearchGraph;
import eu.nighttrains.timetable.route.SearchGraphNode;

import javax.persistence.*;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@SqlResultSetMapping(
        name = "RailwayStationConnectionWithSearchPath",
        entities = {
                @EntityResult(
                        entityClass = RailwayStation.class,
                        fields = {
                                @FieldResult(name = "id", column = "departurestation_id"),
                                @FieldResult(name = "name", column = "departurestation_name"),
                        }
                ),
                @EntityResult(
                        entityClass = RailwayStation.class,
                        fields = {
                                @FieldResult(name = "id", column = "arrivalstation_id"),
                                @FieldResult(name = "name", column = "arrivalstation_name"),
                        }
                ),
                @EntityResult(
                        entityClass = TrainConnection.class,
                        fields = {
                                @FieldResult(name = "id", column = "trainconnection_id"),
                                @FieldResult(name = "code", column = "trainconnection_code"),
                        }
                )
        },
        columns = {
                @ColumnResult(name = "departuretime", type = LocalTime.class),
                @ColumnResult(name = "arrivaltime", type = LocalTime.class),
                @ColumnResult(name = "searchpath", type = LongArrayType.class)
        }
)
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

    @Transient
    private long[] searchPath;

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

    public long[] getSearchPath() {
        return this.searchPath;
    }

    public void setSearchPath(long[] searchPath) {
        this.searchPath = searchPath;
    }
}
