package eu.nighttrains.booking.dto;

public class RailwayStationDto {
    private long id;
    private String name;

    public RailwayStationDto() {}

    public RailwayStationDto(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
