package eu.nighttrains.booking.businesslogic;

import eu.nighttrains.booking.dto.RailwayStationDto;

import java.util.List;

public interface RailwayStationManager {
    List<RailwayStationDto> findAllRailwayStations();
    RailwayStationDto findRailwayStationById(long id);
}
