package eu.nighttrains.booking.businesslogic.exception;

import eu.nighttrains.booking.domain.RailwayStationConnection;

public class NoTrainCarAvailable extends RuntimeException {
    private RailwayStationConnection connection;

    public NoTrainCarAvailable() {
        super();
    }

    public NoTrainCarAvailable(RailwayStationConnection connection){
        super();
        this.connection = connection;
    }

    public NoTrainCarAvailable(String message) {
        super(message);
    }

    public NoTrainCarAvailable(String message, Exception cause){
        super(message, cause);
    }

    public RailwayStationConnection getConnection() {
        return connection;
    }
}
