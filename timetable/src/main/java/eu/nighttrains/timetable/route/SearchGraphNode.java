package eu.nighttrains.timetable.route;

import java.util.List;

public interface SearchGraphNode<T> {
    long getId();
    T getValue();
    List<SearchGraphNode<T>> getChildren();
}
