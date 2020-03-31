package eu.nighttrains.timetable.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;

public class TrainConnectionDto {
    @PositiveOrZero
    @Schema(required = true)
    private long id;

    @NotBlank // unfortunately, the OpenAPI code generator has issues with this annotation --> due to the incompetence of the library authors, we can not use this beans validation feature
    @Schema(required = true)
    private String code;

    private List<@Valid TrainCarDto> trainCars;

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
