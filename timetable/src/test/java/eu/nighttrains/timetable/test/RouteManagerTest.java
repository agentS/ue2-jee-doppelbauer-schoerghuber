package eu.nighttrains.timetable.test;

import eu.nighttrains.timetable.businesslogic.IdNotFoundException;
import eu.nighttrains.timetable.businesslogic.RouteManager;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class RouteManagerTest {
    @Inject
    private RouteManager routeManager;

    @Test
    public void testFindAllDestinationsFrom() throws IdNotFoundException {
        var connections = this.routeManager.findAllDestinationsFrom(0L);
        assertNotNull(connections);
        assertNotNull(connections.getOrigin());
        assertEquals(0, connections.getOrigin().getId());
        assertEquals("Wien Hauptbahnhof", connections.getOrigin().getName());
        assertNotNull(connections.getDestinations());
        assertTrue(connections.getDestinations().size() > 0);
    }

    @Test
    public void testFindAllConnectionsFrom() {
        var connections = this.routeManager.findAllConnectionsFrom(0L);
        assertNotNull(connections);
        assertTrue(connections.size() > 0);
    }
}
