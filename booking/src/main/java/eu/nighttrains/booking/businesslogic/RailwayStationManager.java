package eu.nighttrains.booking.businesslogic;

import eu.nighttrains.booking.domain.RailwayStation;

import java.util.List;

public interface RailwayStationManager {
    List<RailwayStation> findAllRailwayStations();
    RailwayStation findRailwayStationById(long id);
}
