package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

/**
 * Абстрактный базовый класс графа, реализующий общие операции:
 * чтение из файла, сравнение на равенство ({@code equals}/{@code hashCode})
 * и вывод в строку ({@code toString}).
 *
 * @param <V> тип вершин графа
 */
public abstract class AbstractGraph<V> implements Graph<V> {

    /**
     * Конструктор по умолчанию.
     */
    protected AbstractGraph() {
    }

    @Override
    public void readFromFile(Path filePath, Function<String, V> parser) throws IOException {
        Objects.requireNonNull(filePath, "Путь к файлу не может быть null");
        Objects.requireNonNull(parser, "Функция парсера не может быть null");

        this.clear();
        List<String> lines = Files.readAllLines(filePath);
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.isEmpty()) {
                continue;
            }
            String[] parts = trimmed.split("\\s+");
            if (parts.length == 2) {
                addEdge(parser.apply(parts[0]), parser.apply(parts[1]));
            } else if (parts.length == 1) {
                addVertex(parser.apply(parts[0]));
            }
        }
    }

    /**
     * Сравнивает текущий граф с другим графом того же параметризованного типа.
     *
     * @param other другой граф для сравнения
     * @return {@code true}, если графы содержат одинаковые вершины и ребра; {@code false} иначе
     */
    public boolean equals(Graph<V> other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (!Objects.equals(this.getVertices(), other.getVertices())) {
            return false;
        }
        for (V v : this.getVertices()) {
            Set<V> myNeighbors = new HashSet<>(this.getNeighbors(v));
            Set<V> otherNeighbors = new HashSet<>(other.getNeighbors(v));
            if (!Objects.equals(myNeighbors, otherNeighbors)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Graph<?> other)) {
            return false;
        }
        if (!Objects.equals(this.getVertices(), other.getVertices())) {
            return false;
        }
        for (V u : this.getVertices()) {
            for (V v : this.getVertices()) {
                if (this.hasEdge(u, v) != otherEdgeExists(other, u, v)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Вспомогательный метод для безопасной проверки ребра в {@code Graph<?>} без приведения типов.
     */
    private static <T> boolean otherEdgeExists(Graph<T> graph, Object u, Object v) {
        for (T vertex : graph.getVertices()) {
            if (Objects.equals(vertex, u)) {
                return graph.getNeighbors(vertex).contains(v);
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = getVertices().hashCode();
        for (V v : getVertices()) {
            result = 31 * result + Objects.hash(v, new HashSet<>(getNeighbors(v)));
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName() + " {\n");
        for (V v : getVertices()) {
            sb.append("  ").append(v).append(" -> ").append(getNeighbors(v)).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }
}