package eu.nighttrains.booking.businesslogic.cdi;

import eu.nighttrains.booking.businesslogic.DestinationManager;
import eu.nighttrains.booking.client.DestinationsClient;
import eu.nighttrains.booking.domain.RailwayStationConnection;
import eu.nighttrains.booking.logging.Logger;
import eu.nighttrains.booking.logging.LoggerQualifier;
import eu.nighttrains.booking.logging.LoggerType;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@RequestScoped
public class DestinationManagerCdi implements DestinationManager {
    @Inject
    @RestClient
    private DestinationsClient destinationsClient;
    @Inject @LoggerQualifier(type = LoggerType.CONSOLE)
    private Logger logger;

    @Override
    public List<RailwayStationConnection> getConnections(long fromId, long toId) {
        try {
            return destinationsClient.getConnections(fromId, toId);
        } catch(Exception ex) {
            logger.info(ex.getMessage());
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }
}
