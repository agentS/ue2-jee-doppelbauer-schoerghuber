package eu.nighttrains.timetable.test;

import eu.nighttrains.timetable.businesslogic.IdNotFoundException;
import eu.nighttrains.timetable.businesslogic.RailwayStationManager;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class RailwayStationManagerTest {
    @Inject
    private RailwayStationManager railwayStationManager;

    @Test
    public void testFindAllRailwayStations() {
        var railwayStations = this.railwayStationManager.findAllRailwayStations();
        assertNotNull(railwayStations);
        assertTrue(railwayStations.size() > 0);
    }

    @Test
    public void testFindRailwayStationById() throws IdNotFoundException {
        var railwayStation = this.railwayStationManager.findRailwayStationById(0L);
        assertEquals(0, railwayStation.getId());
        assertEquals("Wien Hauptbahnhof", railwayStation.getName());
    }
}
