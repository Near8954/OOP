package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Абстрактный базовый класс графа, реализующий общие операции:
 * чтение из файла, сравнение на равенство ({@code equals}/{@code hashCode}) и вывод в строку ({@code toString}).
 *
 * @param <V> тип вершин графа
 */
public abstract class AbstractGraph<V> implements Graph<V> {

    /**
     * Конструктор по умолчанию.
     */
    protected AbstractGraph() {
    }

    /**
     * Считывает граф из текстового файла фиксированного формата.
     * Каждая строка файла содержит либо одну вершину, либо пару вершин (ребро) через пробел.
     *
     * @param filePath путь к текстовому файлу
     * @throws IOException если произошла ошибка чтения файла
     */
    @Override
    @SuppressWarnings("unchecked")
    public void readFromFile(Path filePath) throws IOException {
        this.clear();
        List<String> lines = Files.readAllLines(filePath);
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.isEmpty()) {
                continue;
            }
            String[] parts = trimmed.split("\\s+");
            if (parts.length == 2) {
                addEdge((V) parts[0], (V) parts[1]);
            } else if (parts.length == 1) {
                addVertex((V) parts[0]);
            }
        }
    }

    /**
     * Сравнивает два графа на равенство независимо от способа их представления в памяти.
     *
     * @param o объект для сравнения
     * @return {@code true}, если графы содержат одинаковые вершины и ребра; {@code false} иначе
     */
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

        for (V v : this.getVertices()) {
            @SuppressWarnings("unchecked")
            Graph<V> castedOther = (Graph<V>) other;
            Set<V> myNeighbors = new HashSet<>(this.getNeighbors(v));
            Set<V> otherNeighbors = new HashSet<>(castedOther.getNeighbors(v));
            if (!Objects.equals(myNeighbors, otherNeighbors)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Вычисляет хэш-код графа на основе множества вершин и их соседей.
     *
     * @return хэш-код графа
     */
    @Override
    public int hashCode() {
        int result = getVertices().hashCode();
        for (V v : getVertices()) {
            result = 31 * result + Objects.hash(v, new HashSet<>(getNeighbors(v)));
        }
        return result;
    }

    /**
     * Формирует строковое представление графа.
     *
     * @return строка со списком вершин и их соседей
     */
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