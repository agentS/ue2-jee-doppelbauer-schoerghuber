package eu.nighttrains.booking.client;

import eu.nighttrains.booking.dto.RailwayStationConnectionDto;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.ProcessingException;
import java.util.List;

//http://localhost:8000/destinations/from/0/to/1
@RegisterRestClient(configKey = "destinationsClient", baseUri = "http://localhost:8000")
// @RegisterProvider(UnknownUriExceptionMapper.class)
@Path("/destinations")
public interface DestinationsClient extends AutoCloseable {
    @GET
    @Path("/from/{originId}/to/{destinationId}")
    List<RailwayStationConnectionDto> getConnections(@PathParam("originId") long originId,
                                                     @PathParam("destinationId") long destinationId )
            throws UnknownUriException, ProcessingException;
}
