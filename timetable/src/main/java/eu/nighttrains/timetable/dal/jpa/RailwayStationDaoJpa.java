package eu.nighttrains.timetable.dal.jpa;

import eu.nighttrains.timetable.dal.RailwayStationDao;
import eu.nighttrains.timetable.model.RailwayStation;

import javax.enterprise.context.RequestScoped;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@RequestScoped
@Transactional
public class RailwayStationDaoJpa extends AbstractDaoBean<RailwayStation, Long> implements RailwayStationDao {
    @Override
    public List<RailwayStation> findByNameLike(String searchTerm) {
        TypedQuery<RailwayStation> query = this.getEntityManager().createQuery(
                "SELECT RS FROM RailwayStation AS RS WHERE RS.name LIKE :searchTerm",
                RailwayStation.class
        );
        query.setParameter("searchTerm", searchTerm + "%");
        return query.getResultList();
    }
}
