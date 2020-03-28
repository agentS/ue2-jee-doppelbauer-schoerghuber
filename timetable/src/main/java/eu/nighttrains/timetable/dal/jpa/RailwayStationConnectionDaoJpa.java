package eu.nighttrains.timetable.dal.jpa;

import com.vladmihalcea.hibernate.type.array.LongArrayType;
import eu.nighttrains.timetable.dal.RailwayStationConnectionDao;
import eu.nighttrains.timetable.model.RailwayStation;
import eu.nighttrains.timetable.model.RailwayStationConnection;
import eu.nighttrains.timetable.model.TrainConnection;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
@Transactional
public class RailwayStationConnectionDaoJpa implements RailwayStationConnectionDao {
    @PersistenceContext
    private EntityManager entityManager;

    private static final String QUERY_ALL_CONNECTIONS_FROM =
            "WITH RECURSIVE connections_to(departurestation_id, arrivalstation_id, trainconnection_id, departuretime, arrivaltime, depth, searchpath, cycle) AS "
            + "("
            + "SELECT departurestation_id, arrivalstation_id, trainconnection_id, departuretime, arrivaltime, 1, ARRAY[departurestation_id], false "
            + "FROM railwaystationconnection "
            + "WHERE departurestation_id = :departureStationId "
            + "UNION ALL "
            + "SELECT R.departurestation_id, R.arrivalstation_id, R.trainconnection_id, R.departuretime, R.arrivaltime, C.depth + 1, searchpath || R.departurestation_id, R.arrivalstation_id = ANY(searchpath) "
            + "FROM connections_to AS C INNER JOIN railwaystationconnection AS R ON C.arrivalstation_id = R.departurestation_id "
            + "WHERE NOT cycle"
            + ") "
            + "SELECT C.departurestation_id, D.name AS departurestation_name, C.arrivalstation_id, A.name AS arrivalstation_name, C.trainconnection_id, T.code AS trainconnection_code, C.departuretime AS departuretime, C.arrivaltime AS arrivaltime, C.searchpath AS searchpath FROM connections_to AS C "
            + "INNER JOIN railwaystation AS D ON D.id = C.departurestation_id "
            + "INNER JOIN railwaystation AS A ON A.id = C.arrivalstation_id "
            + "INNER JOIN trainconnection AS T ON T.id = C.trainconnection_id "
            + "WHERE NOT cycle "
            + "ORDER BY C.searchpath ASC;";

    @Override
    public List<RailwayStationConnection> findAllConnectionsFrom(Long originId) {
        Query query = this.entityManager.createNativeQuery(
                QUERY_ALL_CONNECTIONS_FROM,
                "RailwayStationConnectionWithSearchPath"
        );
        query.setParameter("departureStationId", originId);
        List<Object[]> results = query.getResultList();
        return results.stream().map(record -> {
            RailwayStation departureStation = (RailwayStation) record[0];
            RailwayStation arrivalStation = (RailwayStation) record[1];
            TrainConnection trainConnection = (TrainConnection) record[2];
            RailwayStationConnection connection = new RailwayStationConnection();
            connection.setDepartureStation(departureStation);
            connection.setArrivalStation(arrivalStation);
            connection.setTrainConnection(trainConnection);
            connection.setDepartureTime((LocalTime) record[3]);
            connection.setArrivalTime((LocalTime) record[4]);
            connection.setSearchPath(((long[]) record[5]));
            return connection;
        }).collect(Collectors.toList());
    }

    private static final String QUERY_ALL_DESTINATIONS_FROM =
            "WITH RECURSIVE connections_to(departurestation_id, arrivalstation_id, trainconnection_id, departuretime, arrivaltime, depth, searchpath, cycle) AS "
            + "("
            + "SELECT departurestation_id, arrivalstation_id, trainconnection_id, departuretime, arrivaltime, 1, ARRAY[departurestation_id], false "
            + "FROM railwaystationconnection "
            + "WHERE departurestation_id = :departureStationId "
            + "UNION ALL "
            + "SELECT R.departurestation_id, R.arrivalstation_id, R.trainconnection_id, R.departuretime, R.arrivaltime, C.depth + 1, searchpath || R.departurestation_id, R.arrivalstation_id = ANY(searchpath) "
            + "FROM connections_to AS C INNER JOIN railwaystationconnection AS R ON C.arrivalstation_id = R.departurestation_id "
            + "WHERE NOT cycle"
            + ") "
            + "SELECT R.id, R.name FROM connections_to AS C "
            + "INNER JOIN railwaystation AS R ON C.arrivalstation_id = R.id "
            + "WHERE NOT cycle "
            + "GROUP BY R.id, R.name;";

    @Override
    public List<RailwayStation> findAllDestinationsFrom(Long originId) {
        Query query = this.entityManager.createNativeQuery(
                QUERY_ALL_DESTINATIONS_FROM,
                RailwayStation.class
        );
        query.setParameter("departureStationId", originId);
        return ((List<RailwayStation>) query.getResultList());
    }
}
