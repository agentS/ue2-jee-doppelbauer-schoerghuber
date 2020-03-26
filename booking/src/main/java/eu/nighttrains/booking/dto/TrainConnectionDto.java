package eu.nighttrains.booking.dto;

import java.util.ArrayList;
import java.util.List;

public class TrainConnectionDto {
    private long id;
    private String code;
    private TrainCarDto trainCar;

    public TrainConnectionDto() {
    }

    public TrainConnectionDto(long id, String code, TrainCarDto trainCar) {
        this.id = id;
        this.code = code;
        this.trainCar = trainCar;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public TrainCarDto getTrainCar() {
        return trainCar;
    }

    public void setTrainCar(TrainCarDto trainCar) {
        this.trainCar = trainCar;
    }
}
