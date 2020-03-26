package eu.nighttrains.timetable.test;

import eu.nighttrains.timetable.businesslogic.IdNotFoundException;
import eu.nighttrains.timetable.businesslogic.RailwayStationManager;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.util.Objects;

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

    @Test
    public void testSearchRailwayStations() {
        var railwayStations = this.railwayStationManager.searchByName("Amst");
        assertNotNull(railwayStations);
        assertTrue(railwayStations.size() > 0);
        assertTrue(railwayStations.stream().anyMatch(station -> Objects.equals("Amstetten", station.getName())));
    }
}
