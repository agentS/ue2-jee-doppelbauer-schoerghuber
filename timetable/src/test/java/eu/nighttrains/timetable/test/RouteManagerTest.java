package eu.nighttrains.timetable.test;

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
    public void testFindAllConnectionsFrom() {
        var connections = this.routeManager.findAllConnectionsFrom(0L);
        assertNotNull(connections);
        assertTrue(connections.size() > 0);
    }
}
