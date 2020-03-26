package eu.nighttrains.booking.dto;

import java.util.List;

public class BookingDto {
    private Long id;
    private String from;
    private String to;
    private List<TicketDto> tickets;

    public BookingDto() {
    }

    public BookingDto(Long id, List<TicketDto> tickets) {
        this.id = id;
        this.tickets = tickets;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<TicketDto> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketDto> tickets) {
        this.tickets = tickets;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
