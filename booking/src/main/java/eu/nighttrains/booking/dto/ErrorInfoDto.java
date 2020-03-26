package eu.nighttrains.booking.dto;

public class ErrorInfoDto {
    private String message;
    private Object item;

    public ErrorInfoDto() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getItem() {
        return item;
    }

    public void setItem(Object item) {
        this.item = item;
    }
}
