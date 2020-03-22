package eu.nighttrains.timetable.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class RailwayStation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "departureStation")
    private Set<RailwayStationConnection> connectionsTo = new HashSet<>();

    @OneToMany(mappedBy = "arrivalStation")
    private Set<RailwayStationConnection> connectionsFrom = new HashSet<>();

    public RailwayStation() {}

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<RailwayStationConnection> getConnectionsTo() {
        return this.connectionsTo;
    }

    public void setConnectionsTo(Set<RailwayStationConnection> connectionsTo) {
        this.connectionsTo = connectionsTo;
    }

    public Set<RailwayStationConnection> getConnectionsFrom() {
        return this.connectionsFrom;
    }

    public void setConnectionsFrom(Set<RailwayStationConnection> connectionsFrom) {
        this.connectionsFrom = connectionsFrom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RailwayStation that = (RailwayStation) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
