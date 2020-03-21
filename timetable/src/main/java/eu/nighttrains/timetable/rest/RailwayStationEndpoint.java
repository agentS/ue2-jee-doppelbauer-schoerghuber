package eu.nighttrains.timetable.rest;

import eu.nighttrains.timetable.businesslogic.RailwayStationManager;
import eu.nighttrains.timetable.model.RailwayStation;

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
    private RailwayStationManager railwayStationManager;

    @Inject
    public RailwayStationEndpoint(RailwayStationManager railwayStationManager) {
        this.railwayStationManager = railwayStationManager;
    }

    @Path("/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<RailwayStation> findAllRailwayStations() {
        return this.railwayStationManager.findAllRailwayStations();
    }
}
