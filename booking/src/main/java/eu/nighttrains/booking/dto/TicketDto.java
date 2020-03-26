package eu.nighttrains.booking.dto;

import java.time.LocalDate;

public class TicketDto {
    private Long id;
    private LocalDate date;
    private RailwayStationConnectionDto railwayStationConnection;
    private TrainConnectionDto trainConnection;

    public TicketDto(){
    }

    public TicketDto(Long id, LocalDate date,
                     RailwayStationConnectionDto railwayStationConnection,
                     TrainConnectionDto trainConnection) {
        this.id = id;
        this.date = date;
        this.railwayStationConnection = railwayStationConnection;
        this.trainConnection = trainConnection;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public RailwayStationConnectionDto getRailwayStationConnection() {
        return railwayStationConnection;
    }

    public void setRailwayStationConnection(RailwayStationConnectionDto railwayStationConnection) {
        this.railwayStationConnection = railwayStationConnection;
    }

    public TrainConnectionDto getTrainConnection() {
        return trainConnection;
    }

    public void setTrainConnection(TrainConnectionDto trainConnection) {
        this.trainConnection = trainConnection;
    }
}
