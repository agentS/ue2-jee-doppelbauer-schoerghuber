package eu.nighttrains.timetable.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class RailwayStationDestinationsDto {
    @Valid
    //@NotNull // unfortunately, the OpenAPI code generator has issues with this annotation --> due to the incompetence of the library authors, we can not use this beans validation feature
    @Schema(required = true)
    private RailwayStationDto origin;

    //@NotEmpty // unfortunately, the OpenAPI code generator has issues with this annotation --> due to the incompetence of the library authors, we can not use this beans validation feature
    @Schema(required = true)
    private List<@Valid RailwayStationDto> destinations;

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
