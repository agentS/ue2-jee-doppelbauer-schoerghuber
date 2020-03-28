package eu.nighttrains.booking.businesslogic.exception;

public class BookingNotFound extends RuntimeException {
    public BookingNotFound() {
        super();
    }

    public BookingNotFound(String message) {
        super(message);
    }

    public BookingNotFound(String message, Exception cause){
        super(message, cause);
    }
}
