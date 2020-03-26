package eu.nighttrains.booking.businesslogic.cdi;

import eu.nighttrains.booking.dto.RailwayStationConnectionDto;

public class NoTrainCarAvailable extends RuntimeException {
    private RailwayStationConnectionDto connection;

    public NoTrainCarAvailable() {
        super();
    }

    public NoTrainCarAvailable(RailwayStationConnectionDto connection){
        super();
        this.connection = connection;
    }

    public NoTrainCarAvailable(String message) {
        super(message);
    }

    public NoTrainCarAvailable(String message, Exception cause){
        super(message, cause);
    }

    public RailwayStationConnectionDto getConnection() {
        return connection;
    }
}
