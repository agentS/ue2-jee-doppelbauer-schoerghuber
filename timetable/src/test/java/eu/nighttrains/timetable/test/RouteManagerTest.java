package eu.nighttrains.timetable.test;

import eu.nighttrains.timetable.businesslogic.IdNotFoundException;
import eu.nighttrains.timetable.businesslogic.NoRouteException;
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
    public void testFindAllStopsBetween() throws NoRouteException {
        var connections = this.routeManager.findAllStopsBetween(0L, 14L);
        assertNotNull(connections);
        assertTrue(connections.size() > 0);
        assertEquals(14, connections.size());
    }

    @Test
    public void testFindAllStopsBetweenSelf() throws NoRouteException {
        var connections = this.routeManager.findAllStopsBetween(0L, 0L);
        assertNotNull(connections);
        assertEquals(0, connections.size());
    }

    @Test
    public void testFindRouteForNonExistingStop() {
        assertThrows(
                NoRouteException.class,
                () -> this.routeManager.findAllStopsBetween(0L, -1L)
        );
    }

    @Test
    public void testFindRouteWithConnection() throws NoRouteException {
        var connections = this.routeManager.findAllStopsBetween(43L, 14L);
        assertNotNull(connections);
        assertTrue(connections.size() > 0);
        assertEquals(29, connections.size());
    }
}
