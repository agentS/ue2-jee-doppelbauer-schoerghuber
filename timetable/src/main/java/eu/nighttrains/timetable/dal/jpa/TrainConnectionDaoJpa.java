package eu.nighttrains.timetable.dal.jpa;

import eu.nighttrains.timetable.dal.TrainConnectionDao;
import eu.nighttrains.timetable.model.TrainCar;
import eu.nighttrains.timetable.model.TrainConnection;

import javax.enterprise.context.RequestScoped;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@RequestScoped
@Transactional
public class TrainConnectionDaoJpa extends AbstractDaoBean<TrainConnection, Long> implements TrainConnectionDao {
    @Override
    public TrainConnection findByCode(String code) {
        TypedQuery<TrainConnection> query = this.getEntityManager()
                .createQuery(
                    "SELECT T FROM TrainConnection AS T WHERE T.code = :code",
                    TrainConnection.class
                )
                .setParameter("code", code);
        try {
            return query.getSingleResult();
        } catch (NoResultException exception) {
            return null;
        }
    }
}
