package eu.nighttrains.timetable.dal;

import eu.nighttrains.timetable.model.RailwayStation;
import eu.nighttrains.timetable.model.RailwayStationConnection;

import java.util.List;

public interface RailwayStationConnectionDao {
    List<RailwayStationConnection> findAllConnectionsFrom(Long originId);
    List<RailwayStation> findAllDestinationsFrom(Long originId);
}
