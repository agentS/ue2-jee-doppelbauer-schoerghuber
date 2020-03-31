package eu.nighttrains.timetable.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class RailwayStationDto {
    @Min(0)
    @Schema(required = true)
    private long id;

    @NotBlank
    @Schema(required = true)
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
