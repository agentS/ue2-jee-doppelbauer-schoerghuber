package eu.nighttrains.timetable.rest;

import eu.nighttrains.timetable.businesslogic.IdNotFoundException;
import eu.nighttrains.timetable.businesslogic.RouteManager;
import eu.nighttrains.timetable.dto.RailwayStationDestinationsDto;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
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
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    name = "All destinations reachable from",
                    description = "A list of all destinations reachable from the specified destination not including any stops in between"
            ),
            @APIResponse(
                    responseCode = "404",
                    name = "Railway station does not exist",
                    description = "The railway station with the specified ID does not exist.",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN)
            )
    })

    public RailwayStationDestinationsDto findAllDestinationsFrom(@PathParam("id") Long id) {
        try {
            return this.routeManager.findAllDestinationsFrom(id);
        } catch (IdNotFoundException exception) {
            throw new NotFoundException(exception);
        }
    }
}
