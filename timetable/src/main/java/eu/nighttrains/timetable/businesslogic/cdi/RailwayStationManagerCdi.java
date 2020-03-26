package eu.nighttrains.timetable.businesslogic.cdi;

import eu.nighttrains.timetable.businesslogic.IdNotFoundException;
import eu.nighttrains.timetable.businesslogic.RailwayStationManager;
import eu.nighttrains.timetable.dal.RailwayStationDao;
import eu.nighttrains.timetable.dto.RailwayStationDto;
import eu.nighttrains.timetable.model.RailwayStation;
import org.eclipse.microprofile.opentracing.Traced;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
@Transactional
@Traced
public class RailwayStationManagerCdi implements RailwayStationManager {
    private final RailwayStationDao railwayStationDao;

    @Inject
    public RailwayStationManagerCdi(RailwayStationDao railwayStationDao) {
        this.railwayStationDao = railwayStationDao;
    }

    @Override
    public RailwayStationDto findRailwayStationById(Long id) throws IdNotFoundException {
        RailwayStation station = this.railwayStationDao.findById(id);
        if (station == null) {
            throw new IdNotFoundException("Railway station with id " + id + " does not exist.");
        }
        return new RailwayStationDto(station.getId(), station.getName());
    }

    @Override
    public List<RailwayStationDto> findAllRailwayStations() {
        return this.railwayStationDao.findAll().stream()
                .map(railwayStation -> new RailwayStationDto(railwayStation.getId(), railwayStation.getName()))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<RailwayStationDto> searchByName(String searchTerm) {
        return this.railwayStationDao.findByNameLike(searchTerm).stream()
                .map(railwayStation -> new RailwayStationDto(railwayStation.getId(), railwayStation.getName()))
                .collect(Collectors.toUnmodifiableList());
    }
}
