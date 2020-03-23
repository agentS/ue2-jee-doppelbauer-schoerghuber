package eu.nighttrains.timetable.businesslogic.cdi;

import eu.nighttrains.timetable.businesslogic.IdNotFoundException;
import eu.nighttrains.timetable.businesslogic.NoRouteException;
import eu.nighttrains.timetable.businesslogic.RouteManager;
import eu.nighttrains.timetable.dal.RailwayStationConnectionDao;
import eu.nighttrains.timetable.dal.RailwayStationDao;
import eu.nighttrains.timetable.dto.RailwayStationConnectionDto;
import eu.nighttrains.timetable.dto.RailwayStationDestinationsDto;
import eu.nighttrains.timetable.dto.RailwayStationDto;
import eu.nighttrains.timetable.dto.TrainConnectionDto;
import eu.nighttrains.timetable.model.RailwayStation;
import eu.nighttrains.timetable.model.RailwayStationConnection;
import eu.nighttrains.timetable.model.RailwayStationConnectionSearchGraph;
import eu.nighttrains.timetable.route.SearchGraphAlgorithms;
import eu.nighttrains.timetable.route.SearchGraphNode;
import org.eclipse.microprofile.opentracing.Traced;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
@Transactional
@Traced
public class RouteManagerCdi implements RouteManager {
    private final RailwayStationConnectionDao railwayStationConnectionDao;
    private final RailwayStationDao railwayStationDao;

    @Inject
    public RouteManagerCdi(
            RailwayStationDao railwayStationDao,
            RailwayStationConnectionDao railwayStationConnectionDao
    ) {
        this.railwayStationDao = railwayStationDao;
        this.railwayStationConnectionDao = railwayStationConnectionDao;
    }

    @Override
    public RailwayStationDestinationsDto findAllDestinationsFrom(Long originId) throws IdNotFoundException {
        RailwayStation originStation = this.railwayStationDao.findById(originId);
        if (originStation == null) {
            throw new IdNotFoundException("Railway station with id " + originId + " does not exist.");
        }
        return new RailwayStationDestinationsDto(
                new RailwayStationDto(
                        originStation.getId(),
                        originStation.getName()
                ),
                this.railwayStationConnectionDao.findAllDestinationsFrom(originId).stream()
                        .map(station -> new RailwayStationDto(
                                station.getId(),
                                station.getName()
                        ))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public List<RailwayStationConnectionDto> findAllStopsBetween(Long originId, Long destinationId) throws NoRouteException {
        List<RailwayStationConnection> allConnections =
                this.railwayStationConnectionDao.findAllConnectionsFrom(originId);
        var searchPath = SearchGraphAlgorithms.findPathIterativeDeepeningSearch(
                RailwayStationConnectionSearchGraph.createFromConnections(allConnections), destinationId
        );
        if (searchPath != null) {
            return searchPath.stream()
                    .skip(1)
                    .map(SearchGraphNode::getValue)
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
        throw new NoRouteException("No route exists between train stations with IDs " + originId + " and " + destinationId + '.');
    }
}
