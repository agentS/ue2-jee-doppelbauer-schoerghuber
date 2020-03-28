package eu.nighttrains.booking.businesslogic;

import eu.nighttrains.booking.domain.RailwayStationConnection;

import java.util.List;

public interface DestinationManager {
    List<RailwayStationConnection> getConnections(long fromId, long toId);
}
