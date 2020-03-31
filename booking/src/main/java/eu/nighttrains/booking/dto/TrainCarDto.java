package eu.nighttrains.booking.dto;

import eu.nighttrains.booking.domain.TrainCarType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public class TrainCarDto {
    @PositiveOrZero
    private long id;

    @PositiveOrZero
    private int number;

    // @NotNull // unfortunately, the OpenAPI code generator has issues with this annotation --> due to the incompetence of the library authors, we can not use this beans validation feature
    private TrainCarType type;

    public TrainCarDto() {}

    public TrainCarDto(long id, int number, TrainCarType type) {
        this.id = id;
        this.number = number;
        this.type = type;
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
}
