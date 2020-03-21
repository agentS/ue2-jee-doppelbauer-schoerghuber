package eu.nighttrains.timetable.model;

import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class TrainCar {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private Integer number;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private TrainCarType type;

    @Column(nullable = false)
    private Integer capacity;

    @ManyToOne(optional = false, cascade = { CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST })
    private TrainConnection trainConnection;

    public TrainCar() {}

    public Long getId() {
        return id;
    }

    public Integer getNumber() {
        return this.number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public TrainCarType getType() {
        return type;
    }

    public void setType(TrainCarType type) {
        this.type = type;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer seatCapacity) {
        this.capacity = seatCapacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrainCar trainCar = (TrainCar) o;
        return number.equals(trainCar.number) &&
                type == trainCar.type &&
                capacity.equals(trainCar.capacity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, type, capacity);
    }
}
