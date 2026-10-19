package org.example;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

/**
 * Интерфейс ориентированного графа.
 *
 * @param <V> тип вершин графа
 */
public interface Graph<V> {

    /**
     * Добавляет вершину в граф.
     *
     * @param vertex добавляемая вершина
     * @return {@code true}, если вершина успешно добавлена; {@code false}, если уже существует
     */
    boolean addVertex(V vertex);

    /**
     * Удаляет вершину и все связанные с ней ребра из графа.
     *
     * @param vertex удаляемая вершина
     * @return {@code true}, если вершина существовала и была удалена; {@code false} иначе
     */
    boolean removeVertex(V vertex);

    /**
     * Добавляет ориентированное ребро между двумя вершинами.
     *
     * @param from начальная вершина ребра
     * @param to   конечная вершина ребра
     * @return {@code true}, если ребро добавлено; {@code false}, если оно уже существует
     */
    boolean addEdge(V from, V to);

    /**
     * Удаляет ориентированное ребро между двумя вершинами.
     *
     * @param from начальная вершина ребра
     * @param to   конечная вершина ребра
     * @return {@code true}, если ребро существовало и было удалено; {@code false} иначе
     */
    boolean removeEdge(V from, V to);

    /**
     * Возвращает список всех «соседей» указанной вершины.
     *
     * @param vertex вершина, для которой ищутся соседи
     * @return список соседних вершин
     * @throws IllegalArgumentException если вершина отсутствует в графе
     */
    List<V> getNeighbors(V vertex);

    /**
     * Считывает граф из файла фиксированного формата.
     *
     * @param filePath путь к файлу
     * @throws IOException если возникла ошибка при чтении файла
     */
    void readFromFile(Path filePath) throws IOException;

    /**
     * Возвращает множество всех вершин графа.
     *
     * @return множество вершин
     */
    Set<V> getVertices();

    /**
     * Проверяет наличие вершины в графе.
     *
     * @param vertex искомая вершина
     * @return {@code true}, если вершина есть в графе; {@code false} иначе
     */
    boolean hasVertex(V vertex);

    /**
     * Проверяет наличие ориентированного ребра между вершинами.
     *
     * @param from начальная вершина
     * @param to   конечная вершина
     * @return {@code true}, если ребро существует; {@code false} иначе
     */
    boolean hasEdge(V from, V to);

    /**
     * Очищает граф, удаляя все вершины и ребра.
     */
    void clear();
}