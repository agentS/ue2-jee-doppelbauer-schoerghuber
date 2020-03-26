package eu.nighttrains.booking.businesslogic;

import eu.nighttrains.booking.dto.TrainCarDto;
import eu.nighttrains.booking.dto.TrainConnectionDto;

import java.util.List;

public interface TrainConnectionManager {
    List<TrainConnectionDto> findAllTrainConnections();
    TrainConnectionDto findTrainConnectionById(long id);
    TrainConnectionDto findTrainConnectionByCode(String code);
    List<TrainCarDto> findAllTrainCarsById(long id);
    List<TrainCarDto> findAllTrainCarsByCode(String code);
}
