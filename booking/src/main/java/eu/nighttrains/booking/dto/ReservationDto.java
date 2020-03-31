package eu.nighttrains.booking.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public class ReservationDto {
    @PositiveOrZero
    private long id;

    @Valid
    // @NotNull // unfortunately, the OpenAPI code generator has issues with this annotation --> due to the incompetence of the library authors, we can not use this beans validation feature
    private TrainCarDto trainCar;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public TrainCarDto getTrainCar() {
        return trainCar;
    }

    public void setTrainCar(TrainCarDto trainCar) {
        this.trainCar = trainCar;
    }
}
