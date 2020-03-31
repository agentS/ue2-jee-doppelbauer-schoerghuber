package eu.nighttrains.booking.dto;

import eu.nighttrains.booking.domain.TrainCarType;

import javax.validation.Valid;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

public class BookingRequestDto2 {
    @PositiveOrZero
    private long originId;

    @PositiveOrZero
    private long destinationId;

    //@NotNull // unfortunately, the OpenAPI code generator has issues with this annotation --> due to the incompetence of the library authors, we can not use this beans validation feature
    @FutureOrPresent
    private LocalDate journeyStartDate;

    //@NotNull // unfortunately, the OpenAPI code generator has issues with this annotation --> due to the incompetence of the library authors, we can not use this beans validation feature
    @Valid
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
