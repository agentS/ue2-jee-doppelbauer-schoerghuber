package eu.nighttrains.booking.dto;

public class StopDto {
    private RailwayStationConnectionDto connection;
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
