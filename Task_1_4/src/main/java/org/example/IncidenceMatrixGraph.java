package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Реализация графа через матрицу инцидентности.
 *
 * @param <V> тип вершин графа
 */
public class IncidenceMatrixGraph<V> extends AbstractGraph<V> {

    private static final int OUT = 1;
    private static final int IN = -1;
    private static final int LOOP = 2;
    private static final int NONE = 0;

    /**
     * Список вершин графа (соответствует строкам матрицы).
     */
    private final List<V> vertices = new ArrayList<>();

    /**
     * Список столбцов матрицы инцидентности (каждый столбец соответствует одному ребру).
     */
    private final List<List<Integer>> incidenceColumns = new ArrayList<>();

    /**
     * Создает пустой граф на базе матрицы инцидентности.
     */
    public IncidenceMatrixGraph() {
    }

    /**
     * Добавляет новую вершину (строку) в матрицу инцидентности.
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
        for (List<Integer> col : incidenceColumns) {
            col.add(NONE);
        }
        return true;
    }

    /**
     * Удаляет вершину и все инцидентные ей столбцы-ребра.
     *
     * @param vertex удаляемая вершина
     * @return {@code true}, если вершина была удалена; {@code false} иначе
     */
    @Override
    public boolean removeVertex(V vertex) {
        int vIdx = vertices.indexOf(vertex);
        if (vIdx == -1) {
            return false;
        }

        incidenceColumns.removeIf(col -> col.get(vIdx) != NONE);
        vertices.remove(vIdx);
        for (List<Integer> col : incidenceColumns) {
            col.remove(vIdx);
        }
        return true;
    }

    /**
     * Добавляет новое ориентированное ребро (новый столбец) в матрицу инцидентности.
     *
     * @param from начальная вершина
     * @param to   конечная вершина
     * @return {@code true}, если ребро добавлено; {@code false}, если уже существует
     */
    @Override
    public boolean addEdge(V from, V to) {
        addVertex(from);
        addVertex(to);
        if (hasEdge(from, to)) {
            return false;
        }

        int u = vertices.indexOf(from);
        int v = vertices.indexOf(to);

        List<Integer> newCol = new ArrayList<>(Collections.nCopies(vertices.size(), NONE));
        if (u == v) {
            newCol.set(u, LOOP);
        } else {
            newCol.set(u, OUT);
            newCol.set(v, IN);
        }
        incidenceColumns.add(newCol);
        return true;
    }

    /**
     * Удаляет ребро (столбец) из матрицы инцидентности.
     *
     * @param from начальная вершина
     * @param to   конечная вершина
     * @return {@code true}, если ребро найдено и удалено; {@code false} иначе
     */
    @Override
    public boolean removeEdge(V from, V to) {
        int colIdx = findEdgeColumnIndex(from, to);
        if (colIdx == -1) {
            return false;
        }
        incidenceColumns.remove(colIdx);
        return true;
    }

    /**
     * Возвращает список всех соседей вершины, анализируя столбцы матрицы инцидентности.
     *
     * @param vertex исходная вершина
     * @return список соседних вершин
     * @throws IllegalArgumentException если вершина отсутствует в графе
     */
    @Override
    public List<V> getNeighbors(V vertex) {
        int u = vertices.indexOf(vertex);
        if (u == -1) {
            throw new IllegalArgumentException("Вершина не найдена: " + vertex);
        }

        List<V> neighbors = new ArrayList<>();
        for (List<Integer> col : incidenceColumns) {
            if (col.get(u) == LOOP) {
                neighbors.add(vertex);
            } else if (col.get(u) == OUT) {
                for (int v = 0; v < col.size(); v++) {
                    if (col.get(v) == IN) {
                        neighbors.add(vertices.get(v));
                        break;
                    }
                }
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
     * @return {@code true}, если вершина существует
     */
    @Override
    public boolean hasVertex(V vertex) {
        return vertices.contains(vertex);
    }

    /**
     * Проверяет наличие ориентированного ребра между вершинами.
     *
     * @param from начальная вершина
     * @param to   конечная вершина
     * @return {@code true}, если ребро существует
     */
    @Override
    public boolean hasEdge(V from, V to) {
        return findEdgeColumnIndex(from, to) != -1;
    }

    /**
     * Очищает матрицу инцидентности и список вершин.
     */
    @Override
    public void clear() {
        vertices.clear();
        incidenceColumns.clear();
    }

    /**
     * Находит индекс столбца, соответствующего ориентированному ребру (from -> to).
     *
     * @param from начальная вершина
     * @param to   конечная вершина
     * @return индекс столбца или {@code -1}, если ребро не найдено
     */
    private int findEdgeColumnIndex(V from, V to) {
        int u = vertices.indexOf(from);
        int v = vertices.indexOf(to);
        if (u == -1 || v == -1) {
            return -1;
        }

        for (int i = 0; i < incidenceColumns.size(); i++) {
            List<Integer> col = incidenceColumns.get(i);
            if (u == v && col.get(u) == LOOP) {
                return i;
            } else if (u != v && col.get(u) == OUT && col.get(v) == IN) {
                return i;
            }
        }
        return -1;
    }
}