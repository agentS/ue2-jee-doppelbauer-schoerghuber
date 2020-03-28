package eu.nighttrains.booking.dto;

import eu.nighttrains.booking.domain.TrainCarType;

public class TrainCarDto {
    private long id;
    private int number;
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
