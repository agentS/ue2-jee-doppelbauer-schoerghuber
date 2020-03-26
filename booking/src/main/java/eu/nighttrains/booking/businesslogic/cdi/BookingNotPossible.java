package eu.nighttrains.booking.businesslogic.cdi;

public class BookingNotPossible extends RuntimeException {
    public BookingNotPossible() {
        super();
    }

    public BookingNotPossible(String message) {
        super(message);
    }

    public BookingNotPossible(String message, Exception cause){
        super(message, cause);
    }
}
