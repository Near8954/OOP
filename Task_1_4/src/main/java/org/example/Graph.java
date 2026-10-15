package org.example;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public interface Graph<V> {
    boolean addVertex(V vertex);
    boolean removeVertex(V vertex);

    boolean addEdge(V from, V to);
    boolean removeEdge(V from, V to);

    List<V> getNeighbors(V vertex);

    Set<V> getVertices();
    boolean hasVertex(V vertex);
    boolean hasEdge(V from, V to);
    void clear();

    void readFromFile(Path filePath) throws IOException;
}