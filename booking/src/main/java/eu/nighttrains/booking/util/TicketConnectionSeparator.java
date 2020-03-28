package eu.nighttrains.booking.util;

import eu.nighttrains.booking.domain.RailwayStationConnection;

import java.util.ArrayList;
import java.util.List;

public class TicketConnectionSeparator {
    public List<List<RailwayStationConnection>> separateConnections(List<RailwayStationConnection> connections){
        List<List<RailwayStationConnection>> separatedConnections = new ArrayList<>();
        String trainCode = null;
        List<RailwayStationConnection> ticketConnections = new ArrayList<>();
        for(RailwayStationConnection connection : connections){
            String currentTrainCode = connection.getTrainConnection().getCode();
            if(trainCode == null){
                trainCode = currentTrainCode;
            } else if(!trainCode.equals(currentTrainCode)) {
                separatedConnections.add(ticketConnections);
                ticketConnections = new ArrayList<>();
                trainCode = currentTrainCode;
            }
            ticketConnections.add(connection);
        }
        separatedConnections.add(ticketConnections);
        return separatedConnections;
    }
}
