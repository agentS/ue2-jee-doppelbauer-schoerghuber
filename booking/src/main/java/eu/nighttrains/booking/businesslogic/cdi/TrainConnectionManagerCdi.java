package eu.nighttrains.booking.businesslogic.cdi;

import eu.nighttrains.booking.businesslogic.TrainConnectionManager;
import eu.nighttrains.booking.client.TrainConnectionClient;
import eu.nighttrains.booking.dto.TrainCarDto;
import eu.nighttrains.booking.dto.TrainConnectionDto;
import eu.nighttrains.booking.logging.Logger;
import eu.nighttrains.booking.logging.LoggerQualifier;
import eu.nighttrains.booking.logging.LoggerType;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@RequestScoped
public class TrainConnectionManagerCdi implements TrainConnectionManager {
    @Inject
    @RestClient
    private TrainConnectionClient trainConnectionClient;
    @Inject @LoggerQualifier(type = LoggerType.CONSOLE)
    private Logger logger;

    @Override
    public List<TrainConnectionDto> findAllTrainConnections(){
        try {
            return trainConnectionClient.findAllTrainConnections();
        } catch(Exception ex) {
            logger.info(ex.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public TrainConnectionDto findTrainConnectionById(long id){
        try {
            return trainConnectionClient.findTrainConnectionById(id);
        } catch(Exception ex) {
            logger.info(ex.getMessage());
            return null;
        }
    }

    @Override
    public TrainConnectionDto findTrainConnectionByCode(String code){
        try {
            return trainConnectionClient.findTrainConnectionByCode(code);
        } catch(Exception ex) {
            logger.info(ex.getMessage());
            return null;
        }
    }

    @Override
    public List<TrainCarDto> findAllTrainCarsById(long id){
        try {
            return trainConnectionClient.findAllTrainCarsById(id);
        } catch(Exception ex) {
            logger.info(ex.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<TrainCarDto> findAllTrainCarsByCode(String code){
        try {
            return trainConnectionClient.findAllTrainCarsByCode(code);
        } catch(Exception ex) {
            logger.info(ex.getMessage());
            return new ArrayList<>();
        }
    }
}
