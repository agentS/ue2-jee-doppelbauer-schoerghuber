package eu.nighttrains.timetable.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class RailwayStationConnectionKey implements Serializable {
    @Column(nullable = false)
    Long departureStationId;

    @Column(nullable = false)
    Long arrivalStationId;

    @Column(nullable = false)
    Long trainConnectionId;

    public RailwayStationConnectionKey() {}

    public Long getDepartureStationId() {
        return this.departureStationId;
    }

    public void setDepartureStationId(Long departureStationId) {
        this.departureStationId = departureStationId;
    }

    public Long getArrivalStationId() {
        return this.arrivalStationId;
    }

    public void setArrivalStationId(Long arrivalStationId) {
        this.arrivalStationId = arrivalStationId;
    }

    public Long getTrainConnectionId() {
        return this.trainConnectionId;
    }

    public void setTrainConnectionId(Long trainConnectionId) {
        this.trainConnectionId = trainConnectionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RailwayStationConnectionKey that = (RailwayStationConnectionKey) o;
        return departureStationId.equals(that.departureStationId) &&
                arrivalStationId.equals(that.arrivalStationId) &&
                trainConnectionId.equals(that.trainConnectionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(departureStationId, arrivalStationId, trainConnectionId);
    }
}
