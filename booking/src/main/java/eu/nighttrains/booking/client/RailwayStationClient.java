package eu.nighttrains.booking.client;

import eu.nighttrains.booking.domain.RailwayStation;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.ProcessingException;
import java.util.List;

@RegisterRestClient(configKey = "timetableService")
@Path("/railwayStation")
public interface RailwayStationClient extends AutoCloseable {
    @GET
    @Path("/")
    List<RailwayStation> getAllRailwayStations()
            throws UnknownUriException, ProcessingException;

    @GET
    @Path("/{id}")
    RailwayStation getRailwayStationById(@PathParam("id") long id)
        throws UnknownUriException, ProcessingException;
}
