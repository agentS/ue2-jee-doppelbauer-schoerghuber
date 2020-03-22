package eu.nighttrains.timetable.dal;

import eu.nighttrains.timetable.model.TrainCar;
import eu.nighttrains.timetable.model.TrainConnection;

import java.util.List;

public interface TrainConnectionDao extends Dao<TrainConnection, Long> {
    TrainConnection findByCode(String code);
}
