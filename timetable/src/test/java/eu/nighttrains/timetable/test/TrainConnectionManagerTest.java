package eu.nighttrains.timetable.test;

import eu.nighttrains.timetable.businesslogic.IdNotFoundException;
import eu.nighttrains.timetable.businesslogic.TrainConnectionManager;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class TrainConnectionManagerTest {
    @Inject
    private TrainConnectionManager trainConnectionManager;

    @Test
    public void testFindAllTrainConnections() {
        var trainConnections = this.trainConnectionManager.findAll();
        assertNotNull(trainConnections);
        assertTrue(trainConnections.size() > 0);
    }

    @Test
    public void testFindTrainConnectionById() throws IdNotFoundException {
        var connection = this.trainConnectionManager.findById(1L);
        assertNotNull(connection);
        assertEquals(1, connection.getId());
        assertEquals("NJ 467", connection.getCode());
        assertNotNull(connection.getTrainCars());
        assertTrue(connection.getTrainCars().size() > 0);
    }

    @Test
    public void testFindByCode() throws IdNotFoundException {
        var connection = this.trainConnectionManager.findByCode("NJ 466");
        assertNotNull(connection);
        assertEquals(0, connection.getId());
        assertEquals("NJ 466", connection.getCode());
        assertNotNull(connection.getTrainCars());
        assertTrue(connection.getTrainCars().size() > 0);
    }

    @Test
    public void testSearchForInvalidCode() {
        assertThrows(
                IdNotFoundException.class,
                () -> this.trainConnectionManager.findByCode("NJ 468")
        );
    }

    @Test
    public void testFindAllCarsForId() throws IdNotFoundException {
        var trainCars = this.trainConnectionManager.findAllCarsFor(0L);
        assertNotNull(trainCars);
        assertTrue(trainCars.size() > 0);
    }

    @Test
    public void testFindAllCarsForCode() throws IdNotFoundException {
        var trainCars = this.trainConnectionManager.findAllCarsFor("NJ 466");
        assertNotNull(trainCars);
        assertTrue(trainCars.size() > 0);
    }
}
