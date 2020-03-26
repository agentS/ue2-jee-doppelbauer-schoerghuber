package eu.nighttrains.timetable.dal;

import eu.nighttrains.timetable.model.RailwayStation;

import java.util.List;

public interface RailwayStationDao extends Dao<RailwayStation, Long> {
    List<RailwayStation> findByNameLike(String searchTerm);
}
