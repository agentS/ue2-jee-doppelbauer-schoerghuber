package eu.nighttrains.booking.dto;

public class ReservationDto {
    private long id;
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
