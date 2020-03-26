package eu.nighttrains.booking.domain;

import java.util.List;

public class RailwayStationDestinations {
    private RailwayStation origin;
    private List<RailwayStation> destinations;

    public RailwayStationDestinations() {}

    public RailwayStationDestinations(RailwayStation origin, List<RailwayStation> destinations) {
        this.origin = origin;
        this.destinations = destinations;
    }

    public RailwayStation getOrigin() {
        return this.origin;
    }

    public void setOrigin(RailwayStation origin) {
        this.origin = origin;
    }

    public List<RailwayStation> getDestinations() {
        return this.destinations;
    }

    public void setDestinations(List<RailwayStation> destinations) {
        this.destinations = destinations;
    }
}
