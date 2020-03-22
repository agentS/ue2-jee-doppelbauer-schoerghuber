package eu.nighttrains.timetable.rest;

import eu.nighttrains.timetable.businesslogic.RouteManager;
import eu.nighttrains.timetable.dto.RailwayStationConnectionDto;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@RequestScoped
@Path("/destinations")
public class DestinationsEndpoint {
    private final RouteManager routeManager;

    @Inject
    public DestinationsEndpoint(RouteManager routeManager) {
        this.routeManager = routeManager;
    }

    @Path("/from/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Parameter(name = "id", description = "Origin railway station ID", required = true, example = "1")
    @APIResponse(
            responseCode = "200",
            name = "All destinations reachable from",
            description = "A list of all destinations reachable from the specified destination"
    )
    public List<RailwayStationConnectionDto> findAllDestinationsFrom(@PathParam("id") Long id) {
        return this.routeManager.findAllConnectionsFrom(id);
    }
}
