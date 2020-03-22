package eu.nighttrains.timetable.dto;

import java.util.ArrayList;
import java.util.List;

public class TrainConnectionDto {
    private long id;
    private String code;
    private List<TrainCarDto> trainCars;

    public TrainConnectionDto() {
        this.trainCars = new ArrayList<>();
    }

    public TrainConnectionDto(long id, String code, List<TrainCarDto> trainCars) {
        this.id = id;
        this.code = code;
        this.trainCars = trainCars;
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

    public List<TrainCarDto> getTrainCars() {
        return this.trainCars;
    }

    public void setTrainCars(List<TrainCarDto> trainCars) {
        this.trainCars = trainCars;
    }
}
