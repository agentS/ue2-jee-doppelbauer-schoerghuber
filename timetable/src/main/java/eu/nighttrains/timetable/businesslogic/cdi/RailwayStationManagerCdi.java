package eu.nighttrains.timetable.businesslogic.cdi;

import eu.nighttrains.timetable.businesslogic.RailwayStationManager;
import eu.nighttrains.timetable.dal.RailwayStationDao;
import eu.nighttrains.timetable.model.RailwayStation;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@RequestScoped
@Transactional
public class RailwayStationManagerCdi implements RailwayStationManager {
    private RailwayStationDao railwayStationDao;

    @Inject
    public RailwayStationManagerCdi(RailwayStationDao railwayStationDao) {
        this.railwayStationDao = railwayStationDao;
    }

    @Override
    public List<RailwayStation> findAllRailwayStations() {
        return this.railwayStationDao.findAll();
    }
}
