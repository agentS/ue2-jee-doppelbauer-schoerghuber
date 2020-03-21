package eu.nighttrains.timetable.rest;

import eu.nighttrains.timetable.businesslogic.RailwayStationManager;
import eu.nighttrains.timetable.model.RailwayStation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.jboss.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@RequestScoped
@Path("/railwayStation")
public class RailwayStationEndpoint {
    private static final Logger LOGGER = Logger.getLogger(RailwayStationEndpoint.class);

    private RailwayStationManager railwayStationManager;

    @Inject
    public RailwayStationEndpoint(RailwayStationManager railwayStationManager) {
        this.railwayStationManager = railwayStationManager;
    }

    @Path("/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponse(
            responseCode = "200",
            description = "All railway stations"
    )
    public List<RailwayStation> findAllRailwayStations() {
        LOGGER.info("It works!");
        return this.railwayStationManager.findAllRailwayStations();
    }
}
