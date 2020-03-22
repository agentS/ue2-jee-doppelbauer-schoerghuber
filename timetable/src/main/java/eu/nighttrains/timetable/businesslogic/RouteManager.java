package eu.nighttrains.timetable.businesslogic;

import eu.nighttrains.timetable.dto.RailwayStationConnectionDto;

import java.util.List;

public interface RouteManager {
    List<RailwayStationConnectionDto> findAllConnectionsFrom(Long originId);
    List<RailwayStationConnectionDto> findAllConnectionsBetween(Long originId, Long destinationId);

    List<RailwayStationConnectionDto> findAllStopsBetween(Long originId, Long destinationId, Long trainConnectionId);
}
