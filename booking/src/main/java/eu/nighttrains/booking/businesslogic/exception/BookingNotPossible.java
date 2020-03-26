package eu.nighttrains.booking.businesslogic.exception;

public class BookingNotPossible extends RuntimeException {
    private Object item;

    public BookingNotPossible() {
        super();
    }

    public BookingNotPossible(String message) {
        super(message);
    }

    public BookingNotPossible(String message, Object item) {
        super(message);
        this.item = item;
    }

    public BookingNotPossible(String message, Exception cause){
        super(message, cause);
    }

    public Object getItem() {
        return item;
    }
}
