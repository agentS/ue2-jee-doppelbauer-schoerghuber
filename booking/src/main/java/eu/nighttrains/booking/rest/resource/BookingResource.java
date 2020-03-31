package eu.nighttrains.booking.rest.resource;

import eu.nighttrains.booking.businesslogic.BookingManager;
import eu.nighttrains.booking.businesslogic.exception.BookingNotFound;
import eu.nighttrains.booking.businesslogic.exception.BookingNotPossible;
import eu.nighttrains.booking.dto.BookingDto;
import eu.nighttrains.booking.dto.BookingRequestDto2;
import eu.nighttrains.booking.dto.BookingResponseDto;
import eu.nighttrains.booking.dto.ErrorInfoDto;
import eu.nighttrains.booking.logging.Logger;
import eu.nighttrains.booking.logging.LoggerQualifier;
import eu.nighttrains.booking.logging.LoggerType;
import eu.nighttrains.booking.rest.BookingApplication;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameters;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

@RequestScoped
@Path("/bookings")
public class BookingResource {
    @Context
    private UriInfo uriInfo;

    @Inject
    private BookingManager bookingManager;

    @Inject
    @LoggerQualifier(type = LoggerType.CONSOLE)
    private Logger logger;

    public BookingResource(){
    }

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestBody(
            name = "Booking request object",
            description = "Contains all details about the booking request",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = BookingRequestDto2.class)
            )
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "201",
                    name = "Booking created",
                    description = "Books all stops between origin and destination station",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = BookingResponseDto.class)
                    )
            ),
            @APIResponse(
                    responseCode = "400",
                    name = "The booking could not be completed",
                    description = "The booking could not be completed",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON)
            )
    })
    @Tag(ref = BookingApplication.OPEN_API_TAG_NAME_BOOKING)
    public Response postBooking(@Valid BookingRequestDto2 bookingRequest){
        try{
            Long bookingId = bookingManager.book(bookingRequest);
            URI bookingUri = uriInfo
                    .getAbsolutePathBuilder()
                    .path(String.valueOf(bookingId))
                    .build();
            return Response
                    .created(bookingUri)
                    .entity(new BookingResponseDto(bookingId, "It works!"))
                    .build();
        } catch (BookingNotPossible ex) {
            ex.printStackTrace();
            logger.info(ex.getMessage());
            ErrorInfoDto errorInfoDto = new ErrorInfoDto();
            errorInfoDto.setMessage(ex.getMessage());
            errorInfoDto.setItem(ex.getItem());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(errorInfoDto)
                    .build();
        } catch(Exception ex){
            logger.info(ex.getMessage());
            ex.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Parameters({
            @Parameter(name = "id", description = "Booking id", required = true, example = "1")
    })
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    name = "Booking details.",
                    description = "Details of the requested Booking"
            ),
            @APIResponse(
                    responseCode = "404",
                    name = "Booking does not exist",
                    description = "The booking with the specified ID does not exist.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON)
            )
    })
    @Tag(ref = BookingApplication.OPEN_API_TAG_NAME_BOOKING)
    public Response findById(@PathParam("id") Long id){
        try{
            BookingDto bookingDto = bookingManager.findBookingById(id);
            return Response.status(Response.Status.OK)
                    .entity(bookingDto)
                    .build();
        } catch(BookingNotFound ex){
            return Response.status(Response.Status.NOT_FOUND)
                    .build();
        } catch(Exception ex){
            logger.info(ex.getMessage());
            ex.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
}
