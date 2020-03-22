package eu.nighttrains.timetable.businesslogic;

import eu.nighttrains.timetable.dto.TrainCarDto;
import eu.nighttrains.timetable.dto.TrainConnectionDto;
import eu.nighttrains.timetable.model.TrainConnection;

import java.util.List;

public interface TrainConnectionManager {
    TrainConnectionDto findById(Long id) throws IdNotFoundException;
    TrainConnectionDto findByCode(String code) throws IdNotFoundException;
    TrainConnection findDomainObjectByCode(String code) throws IdNotFoundException;
    List<TrainCarDto> findAllCarsFor(Long id) throws IdNotFoundException;
    List<TrainCarDto> findAllCarsFor(String code) throws IdNotFoundException;
    List<TrainCarDto> findAllCarsFor(TrainConnection connection);
    List<TrainConnectionDto> findAll();
}
