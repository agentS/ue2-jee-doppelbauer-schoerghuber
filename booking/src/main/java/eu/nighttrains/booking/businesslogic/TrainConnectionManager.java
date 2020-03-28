package eu.nighttrains.booking.businesslogic;

import eu.nighttrains.booking.domain.TrainCar;
import eu.nighttrains.booking.domain.TrainConnection;

import java.util.List;

public interface TrainConnectionManager {
    List<TrainConnection> findAllTrainConnections();
    TrainConnection findTrainConnectionById(long id);
    TrainConnection findTrainConnectionByCode(String code);
    List<TrainCar> findAllTrainCarsById(long id);
    List<TrainCar> findAllTrainCarsByCode(String code);
}
