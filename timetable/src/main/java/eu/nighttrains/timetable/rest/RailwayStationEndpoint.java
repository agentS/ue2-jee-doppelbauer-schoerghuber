package eu.nighttrains.timetable.rest;

import eu.nighttrains.timetable.businesslogic.IdNotFoundException;
import eu.nighttrains.timetable.businesslogic.RailwayStationManager;
import eu.nighttrains.timetable.dto.RailwayStationDto;
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
@Path("/railwayStation")
public class RailwayStationEndpoint {
    private final RailwayStationManager railwayStationManager;

    @Inject
    public RailwayStationEndpoint(RailwayStationManager railwayStationManager) {
        this.railwayStationManager = railwayStationManager;
    }

    @Path("/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponse(
            responseCode = "200",
            name = "All railway stations",
            description = "A list containing all railway stations"
    )
    @Tag(ref = TimetableApiApplication.OPEN_API_TAG_NAME_TIMETABLE)
    public List<RailwayStationDto> findAllRailwayStations() {
        return this.railwayStationManager.findAllRailwayStations();
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Parameters({
            @Parameter(name = "id", description = "Railway connection id", required = true, example = "1")
    })
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    name = "Railway station details",
                    description = "Details of the railway station"
            ),
            @APIResponse(
                    responseCode = "404",
                    name = "Railway station does not exist",
                    description = "The railway station with the specified ID does not exist.",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN)
            )
    })
    @Tag(ref = TimetableApiApplication.OPEN_API_TAG_NAME_TIMETABLE)
    public RailwayStationDto findById(@PathParam("id") Long id) {
        try {
            return this.railwayStationManager.findRailwayStationById(id);
        } catch (IdNotFoundException exception) {
            throw new NotFoundException(exception);
        }
    }

    @Path("/search/{searchTerm}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Parameter(name = "searchTerm", description = "Search term entered by the user", example = "Amst")
    @APIResponse(responseCode = "200", name = "List of railway stations", description = "A list of railway stations whose name starts with the specified search term")
    @Tag(ref = TimetableApiApplication.OPEN_API_TAG_NAME_TIMETABLE)
    public List<RailwayStationDto> searchByName(@PathParam("searchTerm") String searchTerm) {
        return this.railwayStationManager.searchByName(searchTerm);
    }
}
