package eu.nighttrains.timetable.businesslogic;

import eu.nighttrains.timetable.dto.RailwayStationDto;

import java.util.List;

public interface RailwayStationManager {
    RailwayStationDto findRailwayStationById(Long id) throws IdNotFoundException;
    List<RailwayStationDto> findAllRailwayStations();

    List<RailwayStationDto> searchByName(String searchTerm);
}
