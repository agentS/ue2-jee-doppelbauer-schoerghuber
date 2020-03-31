package eu.nighttrains.booking.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class StopDto {
    @Valid
    // @NotNull // unfortunately, the OpenAPI code generator has issues with this annotation --> due to the incompetence of the library authors, we can not use this beans validation feature
    private RailwayStationConnectionDto connection;

    @Valid
    // @NotNull // unfortunately, the OpenAPI code generator has issues with this annotation --> due to the incompetence of the library authors, we can not use this beans validation feature
    private ReservationDto reservation;

    public RailwayStationConnectionDto getConnection() {
        return connection;
    }

    public void setConnection(RailwayStationConnectionDto connection) {
        this.connection = connection;
    }

    public ReservationDto getReservation() {
        return reservation;
    }

    public void setReservation(ReservationDto reservation) {
        this.reservation = reservation;
    }
}
