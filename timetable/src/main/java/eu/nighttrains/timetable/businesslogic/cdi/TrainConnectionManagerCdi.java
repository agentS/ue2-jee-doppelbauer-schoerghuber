package eu.nighttrains.timetable.businesslogic.cdi;

import eu.nighttrains.timetable.businesslogic.IdNotFoundException;
import eu.nighttrains.timetable.businesslogic.TrainConnectionManager;
import eu.nighttrains.timetable.dal.TrainConnectionDao;
import eu.nighttrains.timetable.dto.TrainCarDto;
import eu.nighttrains.timetable.dto.TrainConnectionDto;
import eu.nighttrains.timetable.model.TrainCar;
import eu.nighttrains.timetable.model.TrainConnection;
import org.eclipse.microprofile.opentracing.Traced;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
@Transactional
@Traced
public class TrainConnectionManagerCdi implements TrainConnectionManager {
    private final TrainConnectionDao trainConnectionDao;

    @Inject
    public TrainConnectionManagerCdi(TrainConnectionDao trainConnectionDao) {
        this.trainConnectionDao = trainConnectionDao;
    }

    @Override
    public TrainConnectionDto findById(Long id) throws IdNotFoundException {
        TrainConnection connection = this.findDomainObjectById(id);
        return new TrainConnectionDto(
                connection.getId(),
                connection.getCode(),
                connection.getTrainCars().stream()
                        .map(trainCar -> new TrainCarDto(
                                trainCar.getId(),
                                trainCar.getNumber(),
                                trainCar.getType(),
                                trainCar.getCapacity()
                        )).collect(Collectors.toUnmodifiableList())
        );
    }

    @Override
    public TrainConnectionDto findByCode(String code) throws IdNotFoundException {
        TrainConnection connection = this.findDomainObjectByCode(code);
        return new TrainConnectionDto(
                connection.getId(),
                connection.getCode(),
                connection.getTrainCars().stream()
                        .map(trainCar -> new TrainCarDto(
                                trainCar.getId(),
                                trainCar.getNumber(),
                                trainCar.getType(),
                                trainCar.getCapacity()
                        )).collect(Collectors.toUnmodifiableList())
        );
    }

    @Override
    public TrainConnection findDomainObjectByCode(String code) throws IdNotFoundException {
        TrainConnection connection = this.trainConnectionDao.findByCode(code);
        if (connection == null) {
            throw new IdNotFoundException("Train connection with code '" + code + "' does not exist.");
        }
        return connection;
    }

    private TrainConnection findDomainObjectById(Long id) throws IdNotFoundException {
        TrainConnection connection = this.trainConnectionDao.findById(id);
        if (connection == null) {
            throw new IdNotFoundException("Train connection with id " + id + " does not exist.");
        }
        return connection;
    }

    @Override
    public List<TrainCarDto> findAllCarsFor(Long id) throws IdNotFoundException {
        TrainConnection connection = this.findDomainObjectById(id);
        return this.findAllCarsFor(connection);
    }

    @Override
    public List<TrainCarDto> findAllCarsFor(String code) throws IdNotFoundException {
        TrainConnection connection = this.findDomainObjectByCode(code);
        return connection.getTrainCars().stream()
                .map(trainCar -> new TrainCarDto(
                        trainCar.getId(),
                        trainCar.getNumber(),
                        trainCar.getType(),
                        trainCar.getCapacity()
                )).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<TrainCarDto> findAllCarsFor(TrainConnection connection) {
        return connection.getTrainCars().stream()
                .map(trainCar -> new TrainCarDto(
                        trainCar.getId(),
                        trainCar.getNumber(),
                        trainCar.getType(),
                        trainCar.getCapacity()
                )).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<TrainConnectionDto> findAll() {
        return this.trainConnectionDao.findAll().stream()
                .map(trainConnection -> new TrainConnectionDto(
                        trainConnection.getId(),
                        trainConnection.getCode(),
                        trainConnection.getTrainCars().stream()
                                .map(trainCar -> new TrainCarDto(
                                        trainCar.getId(),
                                        trainCar.getNumber(),
                                        trainCar.getType(),
                                        trainCar.getCapacity()
                                )).collect(Collectors.toUnmodifiableList())
                )).collect(Collectors.toUnmodifiableList());
    }
}
