package eu.nighttrains.timetable.dto;

import eu.nighttrains.timetable.model.TrainCarType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class TrainCarDto {
    @Min(0)
    @Schema(required = true)
    private long id;

    @Min(0)
    @Schema(required = true)
    private int number;

    //@NotNull // unfortunately, the OpenAPI code generator has issues with this annotation --> due to the incompetence of the library authors, we can not use this beans validation feature
    @Schema(required = true)
    private TrainCarType type;

    @Min(1)
    @Schema(required = true)
    private int capacity;

    public TrainCarDto() {}

    public TrainCarDto(long id, int number, TrainCarType type, int capacity) {
        this.id = id;
        this.number = number;
        this.type = type;
        this.capacity = capacity;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNumber() {
        return this.number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public TrainCarType getType() {
        return this.type;
    }

    public void setType(TrainCarType type) {
        this.type = type;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
