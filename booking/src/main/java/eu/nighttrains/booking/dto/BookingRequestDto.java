package eu.nighttrains.booking.dto;

import java.time.LocalDate;
import java.util.Date;

public class BookingRequestDto {
    private long originId;
    private long destinationId;
    private LocalDate journeyStartDate;
    private TrainCarType trainCarType;

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

    public LocalDate getJourneyStartDate() {
        return journeyStartDate;
    }

    public void setJourneyStartDate(LocalDate journeyStartDate) {
        this.journeyStartDate = journeyStartDate;
    }

    public TrainCarType getTrainCarType() {
        return trainCarType;
    }

    public void setTrainCarType(TrainCarType trainCarType) {
        this.trainCarType = trainCarType;
    }
}
