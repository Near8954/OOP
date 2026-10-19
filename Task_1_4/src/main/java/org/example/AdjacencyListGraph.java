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

    /**
     * Добавляет вершину в список смежности.
     *
     * @param vertex добавляемая вершина
     * @return {@code true}, если вершина добавлена; {@code false}, если уже присутствует
     */
    @Override
    public boolean addVertex(V vertex) {
        Objects.requireNonNull(vertex, "Вершина не может быть null");
        if (adjList.containsKey(vertex)) {
            return false;
        }
        adjList.put(vertex, new LinkedHashSet<>());
        return true;
    }

    /**
     * Удаляет вершину и все связанные с ней ребра.
     *
     * @param vertex удаляемая вершина
     * @return {@code true}, если вершина удалена; {@code false}, если не найдена
     */
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

    /**
     * Добавляет ориентированное ребро в список смежности.
     *
     * @param from начальная вершина
     * @param to   конечная вершина
     * @return {@code true}, если ребро добавлено; {@code false}, если уже существует
     */
    @Override
    public boolean addEdge(V from, V to) {
        addVertex(from);
        addVertex(to);
        return adjList.get(from).add(to);
    }

    /**
     * Удаляет ориентированное ребро из списка смежности.
     *
     * @param from начальная вершина
     * @param to   конечная вершина
     * @return {@code true}, если ребро удалено; {@code false} иначе
     */
    @Override
    public boolean removeEdge(V from, V to) {
        Set<V> neighbors = adjList.get(from);
        return neighbors != null && neighbors.remove(to);
    }

    /**
     * Возвращает список соседей вершины.
     *
     * @param vertex исходная вершина
     * @return список соседних вершин
     * @throws IllegalArgumentException если вершина не найдена
     */
    @Override
    public List<V> getNeighbors(V vertex) {
        Set<V> neighbors = adjList.get(vertex);
        if (neighbors == null) {
            throw new IllegalArgumentException("Вершина не найдена: " + vertex);
        }
        return new ArrayList<>(neighbors);
    }

    /**
     * Возвращает множество всех вершин графа.
     *
     * @return неизменяемое множество вершин
     */
    @Override
    public Set<V> getVertices() {
        return Collections.unmodifiableSet(adjList.keySet());
    }

    /**
     * Проверяет существование вершины.
     *
     * @param vertex проверяемая вершина
     * @return {@code true}, если вершина есть в графе
     */
    @Override
    public boolean hasVertex(V vertex) {
        return adjList.containsKey(vertex);
    }

    /**
     * Проверяет существование ребра между вершинами.
     *
     * @param from начальная вершина
     * @param to   конечная вершина
     * @return {@code true}, если ребро существует
     */
    @Override
    public boolean hasEdge(V from, V to) {
        return adjList.containsKey(from) && adjList.get(from).contains(to);
    }

    /**
     * Очищает список смежности графа.
     */
    @Override
    public void clear() {
        adjList.clear();
    }
}