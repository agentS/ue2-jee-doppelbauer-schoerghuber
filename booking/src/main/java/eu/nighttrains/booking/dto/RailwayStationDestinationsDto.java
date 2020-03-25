package eu.nighttrains.booking.dto;

import java.util.List;

public class RailwayStationDestinationsDto {
    private RailwayStationDto origin;
    private List<RailwayStationDto> destinations;

    public RailwayStationDestinationsDto() {}

    public RailwayStationDestinationsDto(RailwayStationDto origin, List<RailwayStationDto> destinations) {
        this.origin = origin;
        this.destinations = destinations;
    }

    public RailwayStationDto getOrigin() {
        return this.origin;
    }

    public void setOrigin(RailwayStationDto origin) {
        this.origin = origin;
    }

    public List<RailwayStationDto> getDestinations() {
        return this.destinations;
    }

    public void setDestinations(List<RailwayStationDto> destinations) {
        this.destinations = destinations;
    }
}
