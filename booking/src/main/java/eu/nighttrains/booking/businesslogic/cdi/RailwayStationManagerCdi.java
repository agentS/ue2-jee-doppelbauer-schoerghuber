package eu.nighttrains.booking.businesslogic.cdi;

import eu.nighttrains.booking.businesslogic.RailwayStationManager;
import eu.nighttrains.booking.client.RailwayStationClient;
import eu.nighttrains.booking.dto.RailwayStationDto;
import eu.nighttrains.booking.logging.Logger;
import eu.nighttrains.booking.logging.LoggerQualifier;
import eu.nighttrains.booking.logging.LoggerType;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@RequestScoped
public class RailwayStationManagerCdi implements RailwayStationManager {
    @Inject
    @RestClient
    private RailwayStationClient railwayStationClient;
    @Inject @LoggerQualifier(type = LoggerType.CONSOLE)
    private Logger logger;

    @Override
    public List<RailwayStationDto> findAllRailwayStations() {
        try {
            return railwayStationClient.getAllRailwayStations();
        } catch(Exception ex) {
            logger.info(ex.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public RailwayStationDto findRailwayStationById(long id) {
        try {
            return railwayStationClient.getRailwayStationById(id);
        } catch(Exception ex) {
            logger.info(ex.getMessage());
            return null;
        }
    }
}
