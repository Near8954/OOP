package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GraphTest {

    private List<Graph<String>> createAllGraphs() {
        return List.of(
            new AdjacencyListGraph<>(),
            new AdjacencyMatrixGraph<>(),
            new IncidenceMatrixGraph<>()
        );
    }

    @Test
    void testAddHasAndRemoveVertex() {
        for (Graph<String> g : createAllGraphs()) {
            assertTrue(g.addVertex("A"));
            assertFalse(g.addVertex("A"));
            assertTrue(g.hasVertex("A"));
            assertFalse(g.hasVertex("B"));

            assertEquals(Set.of("A"), g.getVertices());

            assertThrows(NullPointerException.class, () -> g.addVertex(null));

            assertTrue(g.removeVertex("A"));
            assertFalse(g.hasVertex("A"));
            assertFalse(g.removeVertex("A"));
            assertFalse(g.removeVertex("NonExistent"));
        }
    }

    @Test
    void testRemoveVertexRemovesIncidentEdges() {
        for (Graph<String> g : createAllGraphs()) {
            g.addEdge("A", "B");
            g.addEdge("B", "C");
            g.addEdge("A", "C");

            assertTrue(g.removeVertex("B"));
            assertFalse(g.hasVertex("B"));
            assertFalse(g.hasEdge("A", "B"));
            assertFalse(g.hasEdge("B", "C"));
            assertTrue(g.hasEdge("A", "C"));
            assertEquals(List.of("C"), g.getNeighbors("A"));
        }
    }

    @Test
    void testAddHasAndRemoveEdgeAndSelfLoop() {
        for (Graph<String> g : createAllGraphs()) {
            assertTrue(g.addEdge("A", "B"));
            assertFalse(g.addEdge("A", "B"));
            assertTrue(g.hasEdge("A", "B"));
            assertFalse(g.hasEdge("B", "A"));
            assertFalse(g.hasEdge("A", "Z"));
            assertFalse(g.hasEdge("Z", "A"));

            assertTrue(g.addEdge("A", "A"));
            assertFalse(g.addEdge("A", "A"));
            assertTrue(g.hasEdge("A", "A"));
            assertTrue(g.getNeighbors("A").containsAll(List.of("A", "B")));

            assertTrue(g.removeEdge("A", "A"));
            assertFalse(g.hasEdge("A", "A"));
            assertFalse(g.removeEdge("A", "A"));

            assertTrue(g.removeEdge("A", "B"));
            assertFalse(g.hasEdge("A", "B"));
            assertFalse(g.removeEdge("A", "B"));

            assertFalse(g.removeEdge("X", "Y"));
            assertFalse(g.removeEdge("A", "Y"));
        }
    }

    @Test
    void testGetNeighborsAndClear() {
        for (Graph<String> g : createAllGraphs()) {
            g.addEdge("A", "B");
            g.addEdge("A", "C");

            List<String> neighbors = g.getNeighbors("A");
            assertEquals(2, neighbors.size());
            assertTrue(neighbors.containsAll(List.of("B", "C")));
            assertTrue(g.getNeighbors("B").isEmpty());

            assertThrows(IllegalArgumentException.class, () -> g.getNeighbors("UNKNOWN"));

            g.clear();
            assertTrue(g.getVertices().isEmpty());
            assertFalse(g.hasVertex("A"));
        }
    }

    @Test
    void testEqualsHashCodeAndToStringAcrossRepresentations() {
        Graph<String> listGraph = new AdjacencyListGraph<>();
        Graph<String> matrixGraph = new AdjacencyMatrixGraph<>();
        Graph<String> incGraph = new IncidenceMatrixGraph<>();

        for (Graph<String> g : List.of(listGraph, matrixGraph, incGraph)) {
            g.addEdge("1", "2");
            g.addEdge("2", "3");
        }

        assertEquals(listGraph, listGraph);
        assertEquals(listGraph, matrixGraph);
        assertEquals(matrixGraph, incGraph);
        assertEquals(incGraph, listGraph);

        assertEquals(listGraph.hashCode(), matrixGraph.hashCode());
        assertEquals(matrixGraph.hashCode(), incGraph.hashCode());

        assertNotEquals(null, listGraph);
        assertNotEquals("Not a graph", listGraph);

        incGraph.addEdge("1", "3");
        assertNotEquals(listGraph, incGraph);

        matrixGraph.addVertex("4");
        assertNotEquals(listGraph, matrixGraph);

        String str = listGraph.toString();
        assertTrue(str.contains("AdjacencyListGraph"));
        assertTrue(str.contains("1 -> [2]"));
    }

    @Test
    void testReadFromFile(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("graph.txt");
        String content = """
                A B
                
                B C
                D
                """;
        Files.writeString(file, content);

        List<Graph<String>> graphs = createAllGraphs();
        for (Graph<String> g : graphs) {
            g.readFromFile(file);
            assertEquals(Set.of("A", "B", "C", "D"), g.getVertices());
            assertTrue(g.hasEdge("A", "B"));
            assertTrue(g.hasEdge("B", "C"));
            assertTrue(g.getNeighbors("D").isEmpty());
        }

        assertEquals(graphs.get(0), graphs.get(1));
        assertEquals(graphs.get(1), graphs.get(2));
    }

    @Test
    void testTopologicalSortAndCycleDetection() {
        for (Graph<String> g : createAllGraphs()) {
            g.addEdge("A", "C");
            g.addEdge("B", "C");
            g.addEdge("C", "D");

            List<String> sorted = TopologicalSort.topSort(g);
            assertEquals(4, sorted.size());
            assertTrue(sorted.indexOf("A") < sorted.indexOf("C"));
            assertTrue(sorted.indexOf("B") < sorted.indexOf("C"));
            assertTrue(sorted.indexOf("C") < sorted.indexOf("D"));

            g.addEdge("D", "A");
            assertThrows(IllegalStateException.class, () -> TopologicalSort.topSort(g));
        }
    }

    @Test
    void testMainMethodRunsWithoutExceptions() {
        Main app = new Main();
        Main.main(new String[]{});
    }

    @Test
    void testRemoveEdgeComprehensive() {
        for (Graph<String> g : createAllGraphs()) {
            g.addEdge("A", "B");
            assertTrue(g.hasEdge("A", "B"));
            assertTrue(g.removeEdge("A", "B"));
            assertFalse(g.hasEdge("A", "B"));
            assertFalse(g.removeEdge("A", "B"));

            g.addEdge("X", "X");
            assertTrue(g.hasEdge("X", "X"));
            assertTrue(g.removeEdge("X", "X"));
            assertFalse(g.hasEdge("X", "X"));
            assertFalse(g.removeEdge("X", "X"));

            g.addVertex("M");
            assertFalse(g.removeEdge("M", "NON_EXISTENT"));
            assertFalse(g.removeEdge("NON_EXISTENT", "M"));
            assertFalse(g.removeEdge("NOT_EXISTS_1", "NOT_EXISTS_2"));

            g.addVertex("N");
            assertFalse(g.removeEdge("M", "N"));
        }
    }
}