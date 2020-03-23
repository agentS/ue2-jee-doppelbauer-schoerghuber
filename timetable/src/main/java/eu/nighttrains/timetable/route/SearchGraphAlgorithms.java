package eu.nighttrains.timetable.route;

import org.eclipse.microprofile.opentracing.Traced;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class SearchGraphAlgorithms {
    private static final int DEPTH_STEP_WIDTH = 1;

    private SearchGraphAlgorithms() {}

    private static final class State {
        private boolean bottomReached;

        private State() {
            bottomReached = false;
        }

        public boolean isBottomReached() {
            return this.bottomReached;
        }

        public void setBottomReached(boolean bottomReached) {
            this.bottomReached = bottomReached;
        }
    }

    @Traced
    public static <T> List<SearchGraphNode<T>> findPathIterativeDeepeningSearch(SearchGraph<T> searchGraph, long targetId) {
        State state = new State();
        int depth = 1;
        while (!state.isBottomReached()) {
            state.setBottomReached(true);
            List<SearchGraphNode<T>> result = findPathIterativeDeepeningSearch(
                    searchGraph.getRoot(),
                    targetId,
                    new ArrayList<>(),
                    0,
                    depth,
                    state
            );
            if (result != null) {
                return result;
            }
            depth += DEPTH_STEP_WIDTH;
        }
        return null;
    }

    private static <T> List<SearchGraphNode<T>> findPathIterativeDeepeningSearch(
            SearchGraphNode<T> node,
            long targetId,
            List<SearchGraphNode<T>> currentSearchPath,
            int currentDepth,
            int maxDepth,
            State state
    ) {
        currentSearchPath.add(node);

        if (Objects.equals(node.getId(), targetId)) {
            return currentSearchPath;
        }

        if (currentDepth == maxDepth) {
            if (node.getChildren().size() > 0) {
                state.setBottomReached(false);
            }
            return null;
        }

        for (var child : node.getChildren()) {
            List<SearchGraphNode<T>> result = findPathIterativeDeepeningSearch(
                    child,
                    targetId,
                    new ArrayList<>(currentSearchPath),
                    currentDepth + 1,
                    maxDepth,
                    state
            );
            if (result != null) {
                return result;
            }
        }

        return null;
    }
}
