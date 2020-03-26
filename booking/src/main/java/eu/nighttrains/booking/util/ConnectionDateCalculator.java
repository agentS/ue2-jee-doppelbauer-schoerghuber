package eu.nighttrains.booking.util;

import eu.nighttrains.booking.domain.RailwayStationConnection;

import java.time.LocalDate;
import java.util.List;

public class ConnectionDateCalculator {
    public List<RailwayStationConnection> calculateDates(List<RailwayStationConnection> connections, LocalDate date) {
        RailwayStationConnection prevConnection = null;
        LocalDate ticketDate = date;
        for(RailwayStationConnection connection : connections){
            ticketDate = calcTicketDate(prevConnection, connection, ticketDate);
            connection.setDate(ticketDate);
            prevConnection = connection;
        }
        return connections;
    }

    private LocalDate calcTicketDate(RailwayStationConnection prevConnection,
                                     RailwayStationConnection connection,
                                     LocalDate ticketDate) {
        if(prevConnection != null && prevConnection.getDepartureTime().isAfter(connection.getDepartureTime())){
            ticketDate = ticketDate.plusDays(1);
        }
        return ticketDate;
    }
}
