package eu.nighttrains.booking.rest.resource;

import eu.nighttrains.booking.businesslogic.BookingManager;
import eu.nighttrains.booking.businesslogic.exception.BookingNotPossible;
import eu.nighttrains.booking.dto.BookingRequestDto;
import eu.nighttrains.booking.dto.ErrorInfoDto;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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

    public BookingResource(){
    }

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response postBooking(BookingRequestDto bookingRequest){
        try{
            Long bookingId = bookingManager.book(bookingRequest);
            URI bookingUri = uriInfo
                    .getAbsolutePathBuilder()
                    .path(String.valueOf(bookingId))
                    .build();
            return Response.created(bookingUri).build();
        } catch (BookingNotPossible ex) {
            ErrorInfoDto errorInfoDto = new ErrorInfoDto();
            errorInfoDto.setMessage(ex.getMessage());
            errorInfoDto.setItem(ex.getItem());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(errorInfoDto)
                    .build();
        } catch(Exception ex){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
}
