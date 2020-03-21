package eu.nighttrains.timetable.dal.jpa;

import eu.nighttrains.timetable.dal.RailwayStationDao;
import eu.nighttrains.timetable.model.RailwayStation;

import javax.enterprise.context.RequestScoped;
import javax.transaction.Transactional;

@RequestScoped
@Transactional
public class RailwayStationDaoJpa extends AbstractDaoBean<RailwayStation, Long> implements RailwayStationDao {
}
