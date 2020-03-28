package eu.nighttrains.booking.unittests;

import eu.nighttrains.booking.domain.RailwayStationConnection;
import eu.nighttrains.booking.domain.TrainConnection;
import eu.nighttrains.booking.util.TicketConnectionSeparator;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TicketSeparatorTests {
    @Test
    public void testTicketSeparator(){
        // Arrange
        TicketConnectionSeparator separator = new TicketConnectionSeparator();
        List<RailwayStationConnection> connections = getConnections();

        // Act
        List<List<RailwayStationConnection>> sepCon = separator.separateConnections(connections);

        // Assert
        Assert.assertEquals(2, sepCon.size());
        Assert.assertEquals(2, sepCon.get(0).size());
        Assert.assertEquals(2, sepCon.get(1).size());
    }

    private List<RailwayStationConnection> getConnections() {
        List<RailwayStationConnection> connections = new ArrayList<>();
        RailwayStationConnection con1 = new RailwayStationConnection();
        con1.setTrainConnection(new TrainConnection(1, "TRAIN1", null));
        RailwayStationConnection con2 = new RailwayStationConnection();
        con2.setTrainConnection(new TrainConnection(1, "TRAIN1", null));
        RailwayStationConnection con3 = new RailwayStationConnection();
        con3.setTrainConnection(new TrainConnection(2, "TRAIN2", null));
        RailwayStationConnection con4 = new RailwayStationConnection();
        con4.setTrainConnection(new TrainConnection(2, "TRAIN2", null));

        connections.add(con1);
        connections.add(con2);
        connections.add(con3);
        connections.add(con4);

        return connections;
    }
}
