package eu.nighttrains.timetable.businesslogic.cdi;

import eu.nighttrains.timetable.businesslogic.RouteManager;
import eu.nighttrains.timetable.dal.RailwayStationConnectionDao;
import eu.nighttrains.timetable.dto.RailwayStationConnectionDto;
import eu.nighttrains.timetable.dto.RailwayStationDto;
import eu.nighttrains.timetable.dto.TrainConnectionDto;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
@Transactional
public class RouteManagerCdi implements RouteManager {
    private final RailwayStationConnectionDao railwayStationConnectionDao;

    @Inject
    public RouteManagerCdi(RailwayStationConnectionDao railwayStationConnectionDao) {
        this.railwayStationConnectionDao = railwayStationConnectionDao;
    }

    @Override
    public List<RailwayStationConnectionDto> findAllConnectionsFrom(Long originId) {
        return this.railwayStationConnectionDao.findAllConnectionsFrom(originId).stream()
                .map(connection -> new RailwayStationConnectionDto(
                        new RailwayStationDto(
                                connection.getDepartureStation().getId(),
                                connection.getDepartureStation().getName()
                        ),
                        new RailwayStationDto(
                                connection.getArrivalStation().getId(),
                                connection.getArrivalStation().getName()
                        ),
                        new TrainConnectionDto(
                                connection.getTrainConnection().getId(),
                                connection.getTrainConnection().getCode(),
                                new ArrayList<>()
                        ),
                        connection.getDepartureTime(),
                        connection.getArrivalTime()
                ))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<RailwayStationConnectionDto> findAllConnectionsBetween(Long originId, Long destinationId) {
        return null;
    }

    @Override
    public List<RailwayStationConnectionDto> findAllStopsBetween(Long originId, Long destinationId, Long trainConnectionId) {
        return null;
    }
}
