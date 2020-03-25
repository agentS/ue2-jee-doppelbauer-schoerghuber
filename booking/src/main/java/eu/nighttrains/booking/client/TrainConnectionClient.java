package eu.nighttrains.booking.client;

import eu.nighttrains.booking.dto.TrainCarDto;
import eu.nighttrains.booking.dto.TrainConnectionDto;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;

@RegisterRestClient(configKey = "trainConnectionClient", baseUri = "http://localhost:8000")
@Path("/trainConnection")
public interface TrainConnectionClient extends AutoCloseable {
    @GET
    @Path("/")
    List<TrainConnectionDto> findAllTrainConnections();

    @GET
    @Path("/{id}")
    TrainConnectionDto findTrainConnectionById(@PathParam("id") long id);

    @GET
    @Path("/code/{code}")
    TrainConnectionDto findTrainConnectionByCode(@PathParam("code") String code);

    @GET
    @Path("/{id}/cars")
    List<TrainCarDto> findAllTrainCarsById(@PathParam("id") long id);

    @GET
    @Path("/code/{code}/cars")
    List<TrainCarDto> findAllTrainCarsByCode(@PathParam("code") String code);
}
