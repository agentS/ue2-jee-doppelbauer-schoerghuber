package eu.nighttrains.booking.domain;

public class TrainCar {
    private long id;
    private int number;
    private TrainCarType type;
    private int capacity;

    public TrainCar() {}

    public TrainCar(long id, int number, TrainCarType type, int capacity) {
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
