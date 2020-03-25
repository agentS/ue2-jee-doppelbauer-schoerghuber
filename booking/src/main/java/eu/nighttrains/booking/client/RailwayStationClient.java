package eu.nighttrains.booking.client;

import eu.nighttrains.booking.dto.RailwayStationDto;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.ProcessingException;
import java.util.List;

@RegisterRestClient(configKey = "railwayStationClient", baseUri = "http://localhost:8000")
@Path("/railwayStation")
public interface RailwayStationClient extends AutoCloseable {
    @GET
    @Path("/")
    List<RailwayStationDto> getAllRailwayStations()
            throws UnknownUriException, ProcessingException;

    @GET
    @Path("/{id}")
    RailwayStationDto getRailwayStationById(@PathParam("id") long id)
        throws UnknownUriException, ProcessingException;
}
