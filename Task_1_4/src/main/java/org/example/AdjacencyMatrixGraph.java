package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Реализация графа через матрицу смежности.
 *
 * @param <V> тип вершин графа
 */
public class AdjacencyMatrixGraph<V> extends AbstractGraph<V> {

    /**
     * Список вершин для сопоставления каждой вершины с её индексом в матрице.
     */
    private final List<V> vertices = new ArrayList<>();

    /**
     * Двумерная матрица смежности.
     */
    private final List<List<Boolean>> matrix = new ArrayList<>();

    /**
     * Создает пустой граф на базе матрицы смежности.
     */
    public AdjacencyMatrixGraph() {
    }

    /**
     * Добавляет новую вершину и расширяет матрицу смежности.
     *
     * @param vertex добавляемая вершина
     * @return {@code true}, если вершина добавлена; {@code false}, если уже существует
     */
    @Override
    public boolean addVertex(V vertex) {
        Objects.requireNonNull(vertex, "Вершина не может быть null");
        if (vertices.contains(vertex)) {
            return false;
        }

        vertices.add(vertex);
        for (List<Boolean> row : matrix) {
            row.add(false);
        }
        matrix.add(new ArrayList<>(Collections.nCopies(vertices.size(), false)));
        return true;
    }

    /**
     * Удаляет вершину и соответствующие ей строку и столбец матрицы смежности.
     *
     * @param vertex удаляемая вершина
     * @return {@code true}, если вершина удалена; {@code false} иначе
     */
    @Override
    public boolean removeVertex(V vertex) {
        int idx = vertices.indexOf(vertex);
        if (idx == -1) {
            return false;
        }

        vertices.remove(idx);
        matrix.remove(idx);
        for (List<Boolean> row : matrix) {
            row.remove(idx);
        }
        return true;
    }

    /**
     * Добавляет ориентированное ребро в матрицу смежности.
     *
     * @param from начальная вершина
     * @param to   конечная вершина
     * @return {@code true}, если ребро добавлено; {@code false}, если уже было
     */
    @Override
    public boolean addEdge(V from, V to) {
        addVertex(from);
        addVertex(to);
        int u = vertices.indexOf(from);
        int v = vertices.indexOf(to);
        if (matrix.get(u).get(v)) {
            return false;
        }
        matrix.get(u).set(v, true);
        return true;
    }

    /**
     * Удаляет ориентированное ребро из матрицы смежности.
     *
     * @param from начальная вершина
     * @param to   конечная вершина
     * @return {@code true}, если ребро удалено; {@code false} иначе
     */
    @Override
    public boolean removeEdge(V from, V to) {
        int u = vertices.indexOf(from);
        int v = vertices.indexOf(to);
        if (u == -1 || v == -1 || !matrix.get(u).get(v)) {
            return false;
        }
        matrix.get(u).set(v, false);
        return true;
    }

    /**
     * Возвращает список всех соседей указанной вершины по строке матрицы смежности.
     *
     * @param vertex исходная вершина
     * @return список соседних вершин
     * @throws IllegalArgumentException если вершина не найдена
     */
    @Override
    public List<V> getNeighbors(V vertex) {
        int u = vertices.indexOf(vertex);
        if (u == -1) {
            throw new IllegalArgumentException("Вершина не найдена: " + vertex);
        }

        List<V> neighbors = new ArrayList<>();
        List<Boolean> row = matrix.get(u);
        for (int v = 0; v < row.size(); v++) {
            if (row.get(v)) {
                neighbors.add(vertices.get(v));
            }
        }
        return neighbors;
    }

    /**
     * Возвращает множество всех вершин графа.
     *
     * @return множество вершин
     */
    @Override
    public Set<V> getVertices() {
        return new LinkedHashSet<>(vertices);
    }

    /**
     * Проверяет наличие вершины в графе.
     *
     * @param vertex проверяемая вершина
     * @return {@code true}, если вершина присутствует
     */
    @Override
    public boolean hasVertex(V vertex) {
        return vertices.contains(vertex);
    }

    /**
     * Проверяет наличие ребра в матрице смежности.
     *
     * @param from начальная вершина
     * @param to   конечная вершина
     * @return {@code true}, если ребро существует
     */
    @Override
    public boolean hasEdge(V from, V to) {
        int u = vertices.indexOf(from);
        int v = vertices.indexOf(to);
        return u != -1 && v != -1 && matrix.get(u).get(v);
    }

    /**
     * Очищает список вершин и матрицу смежности.
     */
    @Override
    public void clear() {
        vertices.clear();
        matrix.clear();
    }
}