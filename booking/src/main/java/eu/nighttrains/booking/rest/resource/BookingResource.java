package eu.nighttrains.booking.rest.resource;

import eu.nighttrains.booking.businesslogic.BookingManager;
import eu.nighttrains.booking.businesslogic.exception.BookingNotPossible;
import eu.nighttrains.booking.dto.BookingDto;
import eu.nighttrains.booking.dto.BookingRequestDto2;
import eu.nighttrains.booking.dto.ErrorInfoDto;
import eu.nighttrains.booking.logging.Logger;
import eu.nighttrains.booking.logging.LoggerQualifier;
import eu.nighttrains.booking.logging.LoggerType;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
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
    public Response postBooking(BookingRequestDto2 bookingRequest){
        try{
            Long bookingId = bookingManager.book(bookingRequest);
            URI bookingUri = uriInfo
                    .getAbsolutePathBuilder()
                    .path(String.valueOf(bookingId))
                    .build();
            return Response.created(bookingUri).build();
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
    public BookingDto findById(@PathParam("id") Long id){
        return bookingManager.findBookingById(id);
    }
}
