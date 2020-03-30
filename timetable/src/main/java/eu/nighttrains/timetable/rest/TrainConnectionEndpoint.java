package eu.nighttrains.timetable.rest;

import eu.nighttrains.timetable.businesslogic.IdNotFoundException;
import eu.nighttrains.timetable.businesslogic.TrainConnectionManager;
import eu.nighttrains.timetable.dto.TrainCarDto;
import eu.nighttrains.timetable.dto.TrainConnectionDto;
import eu.nighttrains.timetable.model.TrainConnection;
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
import java.util.stream.Collectors;

@RequestScoped
@Path("/trainConnection")
public class TrainConnectionEndpoint {
    private final TrainConnectionManager trainConnectionManager;

    @Inject
    public TrainConnectionEndpoint(TrainConnectionManager trainConnectionManager) {
        this.trainConnectionManager = trainConnectionManager;
    }

    @Path("/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponse(
            responseCode = "200",
            name = "All train connections",
            description = "A list of all train connections including their respective train cars, but not including stops"
    )
    @Tag(ref = TimetableApiApplication.OPEN_API_TAG_NAME_TIMETABLE)
    public List<TrainConnectionDto> findAllTrainConnections() {
        return this.trainConnectionManager.findAll();
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Parameters({
            @Parameter(name = "id", description = "Train connection id", required = true, example = "1")
    })
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    name = "Train connection details",
                    description = "Code and train cars of the specified train connection not including any stops"
            ),
            @APIResponse(
                    responseCode = "404",
                    name = "Train connection does not exist",
                    description = "The train connection with the specified ID does not exist.",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN)
            )
    })
    @Tag(ref = TimetableApiApplication.OPEN_API_TAG_NAME_TIMETABLE)
    public TrainConnectionDto findById(@PathParam("id") Long id) {
        try {
            return this.trainConnectionManager.findById(id);
        } catch (IdNotFoundException exception) {
            throw new NotFoundException(exception);
        }
    }

    @Path("/code/{code}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Parameters({
            @Parameter(name = "code", description = "Train connection code", required = true, example = "NJ 466")
    })
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    name = "Train connection details",
                    description = "Code and train cars of the specified train connection not including any stops"
            ),
            @APIResponse(
                    responseCode = "404",
                    name = "Train connection does not exist",
                    description = "The train connection with the specified code does not exist.",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN)
            )
    })
    @Tag(ref = TimetableApiApplication.OPEN_API_TAG_NAME_TIMETABLE)
    public TrainConnectionDto findByCode(@PathParam("code") String code) {
        try {
            return this.trainConnectionManager.findByCode(code);
        } catch (IdNotFoundException exception) {
            throw new NotFoundException(exception);
        }
    }

    @Path("/{id}/cars")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Parameters({
            @Parameter(name = "id", description = "Train connection id", required = true, example = "1")
    })
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    name = "Train connection details",
                    description = "Train cars of the specified train connection"
            ),
            @APIResponse(
                    responseCode = "404",
                    name = "Train connection does not exist",
                    description = "The train connection with the specified ID does not exist.",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN)
            )
    })
    @Tag(ref = TimetableApiApplication.OPEN_API_TAG_NAME_TIMETABLE)
    public List<TrainCarDto> findAllCarsById(@PathParam("id") Long id) {
        try {
            return this.trainConnectionManager.findAllCarsFor(id);
        } catch (IdNotFoundException exception) {
            throw new NotFoundException(exception);
        }
    }

    @Path("/code/{code}/cars")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Parameters({
            @Parameter(name = "code", description = "Train connection code", required = true, example = "NJ 466")
    })
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    name = "Train connection details",
                    description = "Train cars of the specified train connection"
            ),
            @APIResponse(
                    responseCode = "404",
                    name = "Train connection does not exist",
                    description = "The train connection with the specified code does not exist.",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN)
            )
    })
    @Tag(ref = TimetableApiApplication.OPEN_API_TAG_NAME_TIMETABLE)
    public List<TrainCarDto> findAllCarsByCode(@PathParam("code") String code) {
        try {
            return this.trainConnectionManager.findAllCarsFor(code);
        } catch (IdNotFoundException exception) {
            throw new NotFoundException(exception);
        }
    }
}
