package eu.nighttrains.booking.domain;

public class BookingConnection {
    private long originId;
    private long destinationId;
    private TrainCarType trainCarType;

    public BookingConnection(){
    }

    public BookingConnection(long originId, long destinationId, TrainCarType trainCarType) {
        this.originId = originId;
        this.destinationId = destinationId;
        this.trainCarType = trainCarType;
    }

    public long getOriginId() {
        return originId;
    }

    public void setOriginId(long originId) {
        this.originId = originId;
    }

    public long getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(long destinationId) {
        this.destinationId = destinationId;
    }

    public TrainCarType getTrainCarType() {
        return trainCarType;
    }

    public void setTrainCarType(TrainCarType trainCarType) {
        this.trainCarType = trainCarType;
    }
}
