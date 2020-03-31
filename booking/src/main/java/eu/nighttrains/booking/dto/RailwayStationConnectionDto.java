package eu.nighttrains.booking.dto;

import eu.nighttrains.booking.domain.RailwayStation;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public class RailwayStationConnectionDto {
    // @NotNull // unfortunately, the OpenAPI code generator has issues with this annotation --> due to the incompetence of the library authors, we can not use this beans validation feature
    private LocalDate date;

    // @NotNull // unfortunately, the OpenAPI code generator has issues with this annotation --> due to the incompetence of the library authors, we can not use this beans validation feature
    @Valid
    private RailwayStation departureStation;

    // @NotNull // unfortunately, the OpenAPI code generator has issues with this annotation --> due to the incompetence of the library authors, we can not use this beans validation feature
    @Valid
    private RailwayStation arrivalStation;

    // @NotNull // unfortunately, the OpenAPI code generator has issues with this annotation --> due to the incompetence of the library authors, we can not use this beans validation feature
    private LocalTime departureTime;

    // @NotNull // unfortunately, the OpenAPI code generator has issues with this annotation --> due to the incompetence of the library authors, we can not use this beans validation feature
    private LocalTime arrivalTime;

    public RailwayStationConnectionDto() {}

    public RailwayStationConnectionDto(RailwayStation departureStation, RailwayStation arrivalStation, LocalTime departureTime, LocalTime arrivalTime) {
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    public RailwayStation getDepartureStation() {
        return this.departureStation;
    }

    public void setDepartureStation(RailwayStation departureStation) {
        this.departureStation = departureStation;
    }

    public RailwayStation getArrivalStation() {
        return this.arrivalStation;
    }

    public void setArrivalStation(RailwayStation arrivalStation) {
        this.arrivalStation = arrivalStation;
    }

    public LocalTime getDepartureTime() {
        return this.departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalTime getArrivalTime() {
        return this.arrivalTime;
    }

    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
