package eu.nighttrains.booking.dto;

import eu.nighttrains.booking.domain.TrainCarType;

import java.time.LocalDate;

public class BookingRequestDto2 {
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
