package eu.nighttrains.timetable.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class TrainConnection {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @OneToMany(mappedBy = "trainConnection")
    private Set<TrainCar> trainCars = new HashSet<>();

    public TrainConnection() {}

    public Long getId() {
        return this.id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Set<TrainCar> getTrainCars() {
        return this.trainCars;
    }

    public void setTrainCars(Set<TrainCar> trainCars) {
        this.trainCars = trainCars;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrainConnection that = (TrainConnection) o;
        return code.equals(that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
