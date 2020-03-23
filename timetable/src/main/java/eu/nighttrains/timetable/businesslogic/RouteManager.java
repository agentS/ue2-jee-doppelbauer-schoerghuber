package eu.nighttrains.timetable.businesslogic;

import eu.nighttrains.timetable.dto.RailwayStationConnectionDto;
import eu.nighttrains.timetable.dto.RailwayStationDestinationsDto;

import java.util.List;

public interface RouteManager {
    RailwayStationDestinationsDto findAllDestinationsFrom(Long originId) throws IdNotFoundException;
    List<RailwayStationConnectionDto> findAllStopsBetween(Long originId, Long destinationId) throws NoRouteException;
}
