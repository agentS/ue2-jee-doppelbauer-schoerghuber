package eu.nighttrains.timetable.businesslogic;

public class NoRouteException extends Exception {
    public NoRouteException() {
    }

    public NoRouteException(String message) {
        super(message);
    }

    public NoRouteException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoRouteException(Throwable cause) {
        super(cause);
    }
}
