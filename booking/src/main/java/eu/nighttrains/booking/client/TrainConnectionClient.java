package eu.nighttrains.booking.client;

import eu.nighttrains.booking.domain.TrainCar;
import eu.nighttrains.booking.domain.TrainConnection;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.ProcessingException;
import java.util.List;

@RegisterRestClient(configKey = "timetableService")
@Path("/trainConnection")
public interface TrainConnectionClient extends AutoCloseable {
    @GET
    @Path("/")
    List<TrainConnection> findAllTrainConnections()
            throws UnknownUriException, ProcessingException;

    @GET
    @Path("/{id}")
    TrainConnection findTrainConnectionById(@PathParam("id") long id)
            throws UnknownUriException, ProcessingException;

    @GET
    @Path("/code/{code}")
    TrainConnection findTrainConnectionByCode(@PathParam("code") String code)
            throws UnknownUriException, ProcessingException;

    @GET
    @Path("/{id}/cars")
    List<TrainCar> findAllTrainCarsById(@PathParam("id") long id)
            throws UnknownUriException, ProcessingException;

    @GET
    @Path("/code/{code}/cars")
    List<TrainCar> findAllTrainCarsByCode(@PathParam("code") String code)
            throws UnknownUriException, ProcessingException;
}
