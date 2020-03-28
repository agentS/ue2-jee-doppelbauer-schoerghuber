package eu.nighttrains.timetable.model;

import eu.nighttrains.timetable.route.SearchGraph;
import eu.nighttrains.timetable.route.SearchGraphNode;

import java.util.ArrayList;
import java.util.List;

public final class RailwayStationConnectionSearchGraph implements SearchGraph<RailwayStationConnection> {
    public static final class RailwayStationConnectionSearchGraphNode implements SearchGraphNode<RailwayStationConnection> {
        private final long id;
        private final RailwayStationConnection value;
        private final List<SearchGraphNode<RailwayStationConnection>> children;

        public RailwayStationConnectionSearchGraphNode(long id, RailwayStationConnection value) {
            this.id = id;
            this.value = value;
            this.children = new ArrayList<>();
        }

        @Override
        public long getId() {
            return this.id;
        }

        @Override
        public RailwayStationConnection getValue() {
            return this.value;
        }

        @Override
        public List<SearchGraphNode<RailwayStationConnection>> getChildren() {
            return this.children;
        }
    }

    private final RailwayStationConnectionSearchGraphNode root;

    public RailwayStationConnectionSearchGraph(RailwayStationConnectionSearchGraphNode root) {
        this.root = root;
    }

    @Override
    public SearchGraphNode<RailwayStationConnection> getRoot() {
        return this.root;
    }

    public static RailwayStationConnectionSearchGraph createFromConnections(
            List<RailwayStationConnection> connections
    ) {
        RailwayStationConnection rootConnection = connections.stream()
                .filter(connection -> connection.getSearchPath().length == 1)
                .findFirst()
                .orElseThrow();
        RailwayStationConnectionSearchGraphNode root = new RailwayStationConnectionSearchGraphNode(
                rootConnection.getDepartureStation().getId(),
                rootConnection
        );
        for (var connection : connections) {
            SearchGraphNode<RailwayStationConnection> parent = root;
            for (int index = 1; index < connection.getSearchPath().length; ++index) {
                final long searchPathPart = connection.getSearchPath()[index];
                parent = parent.getChildren().stream()
                        .filter(node -> node.getId() == searchPathPart)
                        .findFirst()
                        .orElseThrow();
            }
            var child = new RailwayStationConnectionSearchGraphNode(
                    connection.getArrivalStation().getId(),
                    connection
            );
            parent.getChildren().add(child);
        }
        return new RailwayStationConnectionSearchGraph(root);
    }
}
