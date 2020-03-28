package eu.nighttrains.booking.unittests;

import eu.nighttrains.booking.domain.RailwayStationConnection;
import eu.nighttrains.booking.domain.TrainConnection;
import eu.nighttrains.booking.util.ConnectionDateCalculator;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ConnectionDateCalculatorTests {
    @Test
    public void testCalculateDates(){
        // Arrange
        ConnectionDateCalculator calculator = new ConnectionDateCalculator();
        LocalDate date = LocalDate.of(2020, 4,3);
        List<RailwayStationConnection> connections = getConnections();

        // Act
        calculator.calculateDates(connections, date);

        // Assert
    }

    private List<RailwayStationConnection> getConnections() {
        List<RailwayStationConnection> connections = new ArrayList<>();
        LocalTime time = LocalTime.of(1,0);
        RailwayStationConnection con1 = new RailwayStationConnection();
        con1.setTrainConnection(new TrainConnection(1, "TRAIN1", null));
        con1.setDepartureTime(time);
        RailwayStationConnection con2 = new RailwayStationConnection();
        con2.setTrainConnection(new TrainConnection(1, "TRAIN1", null));
        con2.setDepartureTime(time.plusHours(2));
        RailwayStationConnection con3 = new RailwayStationConnection();
        con3.setTrainConnection(new TrainConnection(2, "TRAIN2", null));
        con3.setDepartureTime(time.plusHours(27));
        RailwayStationConnection con4 = new RailwayStationConnection();
        con4.setTrainConnection(new TrainConnection(2, "TRAIN2", null));
        con4.setDepartureTime(time.plusHours(29));

        connections.add(con1);
        connections.add(con2);
        connections.add(con3);
        connections.add(con4);

        return connections;
    }
}
