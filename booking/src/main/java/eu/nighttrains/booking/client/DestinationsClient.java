package eu.nighttrains.booking.client;

import eu.nighttrains.booking.domain.RailwayStationConnection;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.ProcessingException;
import java.util.List;

@RegisterRestClient(configKey = "timetableService")
// @RegisterProvider(UnknownUriExceptionMapper.class)
@Path("/destinations")
public interface DestinationsClient extends AutoCloseable {
    @GET
    @Path("/from/{originId}/to/{destinationId}")
    List<RailwayStationConnection> getConnections(@PathParam("originId") long originId,
                                                  @PathParam("destinationId") long destinationId )
            throws UnknownUriException, ProcessingException;
}
