package eu.nighttrains.timetable.rest;

import eu.nighttrains.timetable.businesslogic.IdNotFoundException;
import eu.nighttrains.timetable.businesslogic.NoRouteException;
import eu.nighttrains.timetable.businesslogic.RouteManager;
import eu.nighttrains.timetable.dto.RailwayStationConnectionDto;
import eu.nighttrains.timetable.dto.RailwayStationDestinationsDto;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameters;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

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
    @Parameter(name = "id", description = "Origin railway station ID", required = true, example = "0")
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
    @Tag(ref = TimetableApiApplication.OPEN_API_TAG_NAME_TIMETABLE)
    public RailwayStationDestinationsDto findAllDestinationsFrom(@PathParam("id") Long id) {
        try {
            return this.routeManager.findAllDestinationsFrom(id);
        } catch (IdNotFoundException exception) {
            throw new NotFoundException(exception);
        }
    }

    @Path("/from/{originId}/to/{destinationId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Parameters({
            @Parameter(name = "originId", description = "Origin railway station ID", required = true, example = "0"),
            @Parameter(name = "destinationId", description = "Destination railway station ID", required = true, example = "14")
    })
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    name = "Stops including train connection information between the stations",
                    description = "A list of all stops including their train connection information between the origin station and the destination station."
            ),
            @APIResponse(
                    responseCode = "404",
                    name = "No route between the railway stations",
                    description = "There is no route connecting the two railway stations. This might be because there is no connection between the stations or at least one of the stations does not exist.",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN)
            )
    })
    @Tag(ref = TimetableApiApplication.OPEN_API_TAG_NAME_TIMETABLE)
    public List<RailwayStationConnectionDto> findAllStopsBetween(
            @PathParam("originId") Long originId,
            @PathParam("destinationId") Long destinationId
    ) {
        try {
            return this.routeManager.findAllStopsBetween(originId, destinationId);
        } catch (NoRouteException exception) {
            throw new NotFoundException(exception);
        }
    }
}
