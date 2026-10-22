package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Реализация графа через список смежности.
 *
 * @param <V> тип вершин графа
 */
public class AdjacencyListGraph<V> extends AbstractGraph<V> {

    /**
     * Хранилище списков смежности: вершина -> множество соседних вершин.
     */
    private final Map<V, Set<V>> adjList = new LinkedHashMap<>();

    /**
     * Создает пустой граф на базе списка смежности.
     */
    public AdjacencyListGraph() {
    }

    @Override
    public boolean addVertex(V vertex) {
        Objects.requireNonNull(vertex, "Вершина не может быть null");
        if (adjList.containsKey(vertex)) {
            return false;
        }
        adjList.put(vertex, new LinkedHashSet<>());
        return true;
    }

    @Override
    public boolean removeVertex(V vertex) {
        if (!adjList.containsKey(vertex)) {
            return false;
        }
        adjList.remove(vertex);
        for (Set<V> neighbors : adjList.values()) {
            neighbors.remove(vertex);
        }
        return true;
    }

    @Override
    public boolean addEdge(V from, V to) {
        addVertex(from);
        addVertex(to);
        return adjList.get(from).add(to);
    }

    @Override
    public boolean removeEdge(V from, V to) {
        Set<V> neighbors = adjList.get(from);
        return neighbors != null && neighbors.remove(to);
    }

    @Override
    public List<V> getNeighbors(V vertex) {
        Set<V> neighbors = adjList.get(vertex);
        if (neighbors == null) {
            throw new IllegalArgumentException("Вершина не найдена: " + vertex);
        }
        return new ArrayList<>(neighbors);
    }

    @Override
    public Set<V> getVertices() {
        return Collections.unmodifiableSet(adjList.keySet());
    }

    @Override
    public boolean hasVertex(V vertex) {
        return adjList.containsKey(vertex);
    }

    @Override
    public boolean hasEdge(V from, V to) {
        return adjList.containsKey(from) && adjList.get(from).contains(to);
    }

    @Override
    public void clear() {
        adjList.clear();
    }
}