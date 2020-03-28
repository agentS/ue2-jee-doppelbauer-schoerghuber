package eu.nighttrains.booking.domain;

import java.util.ArrayList;
import java.util.List;

public class TrainConnection {
    private long id;
    private String code;
    private List<TrainCar> trainCars;

    public TrainConnection() {
        this.trainCars = new ArrayList<>();
    }

    public TrainConnection(long id, String code, List<TrainCar> trainCars) {
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

    public List<TrainCar> getTrainCars() {
        return this.trainCars;
    }

    public void setTrainCars(List<TrainCar> trainCars) {
        this.trainCars = trainCars;
    }
}
