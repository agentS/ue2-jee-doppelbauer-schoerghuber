package eu.nighttrains.booking.businesslogic;

import eu.nighttrains.booking.dto.RailwayStationConnectionDto;

import java.util.List;

public interface DestinationManager {
    List<RailwayStationConnectionDto> getConnections(long fromId, long toId);
}
