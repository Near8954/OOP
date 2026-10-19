package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс для выполнения топологической сортировки вершин графа.
 */
public final class TopologicalSort {

    /**
     * Статусы посещения вершин при обходе в глубину.
     */
    private enum State {
        UNVISITED,
        VISITING,
        VISITED
    }

    /**
     * Приватный конструктор утилитного класса.
     */
    private TopologicalSort() {
    }

    /**
     * Выполняет топологическую сортировку вершин графа.
     *
     * @param g   граф для сортировки
     * @param <V> тип вершин графа
     * @return отсортированный в топологическом порядке список вершин
     * @throws IllegalStateException если граф содержит цикл
     */
    public static <V> List<V> topSort(Graph<V> g) {
        Map<V, State> states = new HashMap<>();
        for (V v : g.getVertices()) {
            states.put(v, State.UNVISITED);
        }

        List<V> result = new ArrayList<>();
        for (V v : g.getVertices()) {
            if (states.get(v) == State.UNVISITED) {
                dfs(v, g, states, result);
            }
        }

        Collections.reverse(result);
        return result;
    }

    /**
     * Рекурсивный обход графа в глубину (DFS) для построения топологического порядка.
     *
     * @param current текущая вершина
     * @param g       объект графа
     * @param states  карта состояний вершин
     * @param result  результирующий список вершин
     * @param <V>     тип вершин графа
     */
    private static <V> void dfs(V current, Graph<V> g, Map<V, State> states, List<V> result) {
        states.put(current, State.VISITING);

        for (V neighbor : g.getNeighbors(current)) {
            State neighborState = states.get(neighbor);
            if (neighborState == State.VISITING) {
                throw new IllegalStateException("В графе обнаружен цикл! Топологическая сортировка невозможна.");
            }
            if (neighborState == State.UNVISITED) {
                dfs(neighbor, g, states, result);
            }
        }

        states.put(current, State.VISITED);
        result.add(current);
    }
}