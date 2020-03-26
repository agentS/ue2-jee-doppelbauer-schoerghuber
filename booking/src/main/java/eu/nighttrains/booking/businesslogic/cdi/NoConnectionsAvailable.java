package eu.nighttrains.booking.businesslogic.cdi;

public class NoConnectionsAvailable extends RuntimeException {
    public NoConnectionsAvailable() {
        super();
    }

    public NoConnectionsAvailable(String message) {
        super(message);
    }

    public NoConnectionsAvailable(String message, Exception cause){
        super(message, cause);
    }
}
