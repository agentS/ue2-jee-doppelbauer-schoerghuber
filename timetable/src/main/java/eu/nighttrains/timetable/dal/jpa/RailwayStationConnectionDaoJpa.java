package eu.nighttrains.timetable.dal.jpa;

import eu.nighttrains.timetable.dal.RailwayStationConnectionDao;
import eu.nighttrains.timetable.model.RailwayStationConnection;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
@Transactional
public class RailwayStationConnectionDaoJpa implements RailwayStationConnectionDao {
    @PersistenceContext
    private EntityManager entityManager;

    private static final String QUERY_ALL_CONNECTIONS_FROM =
            "WITH RECURSIVE connections_to(departurestation_id, arrivalstation_id, trainconnection_id, departuretime, arrivaltime, depth, search_path, cycle) AS "
            + "("
            + "SELECT departurestation_id, arrivalstation_id, trainconnection_id, departuretime, arrivaltime, 1, ARRAY[departurestation_id], false "
            + "FROM railwaystationconnection "
            + "WHERE departurestation_id = :departureStationId "
            + "UNION ALL "
            + "SELECT R.departurestation_id, R.arrivalstation_id, R.trainconnection_id, R.departuretime, R.arrivaltime, C.depth + 1, search_path || R.departurestation_id, R.arrivalstation_id = ANY(search_path) "
            + "FROM connections_to AS C INNER JOIN railwaystationconnection AS R ON C.arrivalstation_id = R.departurestation_id "
            + "WHERE NOT cycle"
            + ") "
            + "SELECT C.departurestation_id, D.name AS departurestation_name, C.arrivalstation_id, A.name AS arrivalstation_name, C.trainconnection_id, T.code AS trainconnection_code, C.departuretime, C.arrivaltime FROM connections_to AS C "
            + "INNER JOIN railwaystation AS D ON D.id = C.departurestation_id "
            + "INNER JOIN railwaystation AS A ON A.id = C.arrivalstation_id "
            + "INNER JOIN trainconnection AS T ON T.id = C.trainconnection_id "
            + "WHERE NOT cycle;";

    @Override
    public List<RailwayStationConnection> findAllConnectionsFrom(Long originId) {
        Query query = this.entityManager.createNativeQuery(
                QUERY_ALL_CONNECTIONS_FROM,
                RailwayStationConnection.class
        );
        query.setParameter("departureStationId", originId);
        return query.getResultList();
    }
}
