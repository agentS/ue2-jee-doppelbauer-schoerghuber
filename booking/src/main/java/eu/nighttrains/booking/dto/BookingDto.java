package eu.nighttrains.booking.dto;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

public class BookingDto {
    @PositiveOrZero
    private Long id;

    @PositiveOrZero
    private long originId;

    @NotBlank
    private String originStationName;

    @PositiveOrZero
    private long destinationId;

    @NotBlank
    private String destinationStationName;

    // @NotEmpty // unfortunately, the OpenAPI code generator has issues with this annotation --> due to the incompetence of the library authors, we can not use this beans validation feature
    @Size(min = 1)
    private List<@Valid TicketDto> tickets;

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

    public long getOriginId() {
        return originId;
    }

    public void setOriginId(long originId) {
        this.originId = originId;
    }

    public String getOriginStationName() {
        return originStationName;
    }

    public void setOriginStationName(String originStationName) {
        this.originStationName = originStationName;
    }

    public long getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(long destinationId) {
        this.destinationId = destinationId;
    }

    public String getDestinationStationName() {
        return destinationStationName;
    }

    public void setDestinationStationName(String destinationStationName) {
        this.destinationStationName = destinationStationName;
    }

    public List<TicketDto> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketDto> tickets) {
        this.tickets = tickets;
    }
}
