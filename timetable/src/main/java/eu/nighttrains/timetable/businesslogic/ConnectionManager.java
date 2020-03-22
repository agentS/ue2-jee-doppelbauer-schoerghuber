package eu.nighttrains.timetable.businesslogic;

import eu.nighttrains.timetable.model.RailwayStation;
import eu.nighttrains.timetable.model.RailwayStationConnection;
import eu.nighttrains.timetable.model.TrainConnection;

import java.util.List;

public interface ConnectionManager {
    List<RailwayStationConnection> findAllConnectionsFrom(RailwayStation origin);
    List<RailwayStationConnection> findAllConnectionsTo(RailwayStation origin, RailwayStation destination);

    List<RailwayStation> findAllStopsBetween(RailwayStation origin, RailwayStation destination, TrainConnection connection);
}
