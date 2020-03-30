package eu.nighttrains.booking.businesslogic.cdi;

import eu.nighttrains.booking.businesslogic.RailwayStationManager;
import eu.nighttrains.booking.client.RailwayStationClient;
import eu.nighttrains.booking.domain.RailwayStation;
import eu.nighttrains.booking.logging.Logger;
import eu.nighttrains.booking.logging.LoggerQualifier;
import eu.nighttrains.booking.logging.LoggerType;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequestScoped
public class RailwayStationManagerCdi implements RailwayStationManager {
    @Inject
    @RestClient
    private RailwayStationClient railwayStationClient;
    @Inject @LoggerQualifier(type = LoggerType.CONSOLE)
    private Logger logger;
    private List<RailwayStation> cachedStations;

    @Override
    public List<RailwayStation> findAllRailwayStations() {
        try {
            if(cachedStations == null){
                cachedStations = railwayStationClient.getAllRailwayStations();
            }
            return cachedStations;
        } catch(Exception ex) {
            logger.info(ex.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public RailwayStation findRailwayStationById(long id) {
        try {
            RailwayStation foundStation = null;
            if(cachedStations != null){
                foundStation = findRailwayStationByIdInCache(id);
            }
            if(foundStation == null){
                foundStation = railwayStationClient.getRailwayStationById(id);
            }
            return foundStation;
        } catch(Exception ex) {
            logger.info(ex.getMessage());
            return null;
        }
    }

    private RailwayStation findRailwayStationByIdInCache(long id) {
        Optional<RailwayStation> foundStation = cachedStations
                .stream()
                .filter(station -> station.getId() == id)
                .findFirst();

        if(foundStation.isPresent()){
            return foundStation.get();
        }
        return null;
    }
}
