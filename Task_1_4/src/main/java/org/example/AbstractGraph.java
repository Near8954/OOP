package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public abstract class AbstractGraph<V> implements Graph<V> {

    @Override
    @SuppressWarnings("unchecked")
    public void readFromFile(Path filePath) throws IOException {
        this.clear();
        List<String> lines = Files.readAllLines(filePath);
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.isEmpty()) continue;
            String[] parts = trimmed.split("\\s+");
            if (parts.length == 2) {
                addEdge((V) parts[0], (V) parts[1]);
            } else if (parts.length == 1) {
                addVertex((V) parts[0]);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Graph<?> other)) return false;

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