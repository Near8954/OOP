package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
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
        AbstractGraph<String> listGraph = new AdjacencyListGraph<>();
        AbstractGraph<String> matrixGraph = new AdjacencyMatrixGraph<>();
        AbstractGraph<String> incGraph = new IncidenceMatrixGraph<>();

        for (Graph<String> g : List.of(listGraph, matrixGraph, incGraph)) {
            g.addEdge("1", "2");
            g.addEdge("2", "3");
        }

        assertEquals(listGraph, listGraph);
        assertEquals(listGraph, matrixGraph);
        assertEquals(matrixGraph, incGraph);
        assertEquals(incGraph, listGraph);

        assertTrue(listGraph.equals((Graph<String>) listGraph));
        assertTrue(listGraph.equals((Graph<String>) matrixGraph));
        assertFalse(listGraph.equals((Graph<String>) null));

        assertEquals(listGraph.hashCode(), matrixGraph.hashCode());
        assertEquals(matrixGraph.hashCode(), incGraph.hashCode());

        assertNotEquals(null, listGraph);
        assertNotEquals("Not a graph", listGraph);

        incGraph.addEdge("1", "3");
        assertNotEquals(listGraph, incGraph);
        assertFalse(listGraph.equals((Graph<String>) incGraph));

        matrixGraph.addVertex("4");
        assertNotEquals(listGraph, matrixGraph);
        assertFalse(listGraph.equals((Graph<String>) matrixGraph));

        String str = listGraph.toString();
        assertTrue(str.contains("AdjacencyListGraph"));
        assertTrue(str.contains("1 -> [2]"));
    }

    @Test
    void testReadFromFileStringAndInteger(@TempDir Path tempDir) throws IOException {
        Path stringFile = tempDir.resolve("graph_str.txt");
        String strContent = """
                A B
                
                B C
                D
                """;
        Files.writeString(stringFile, strContent);

        List<Graph<String>> graphs = createAllGraphs();
        for (Graph<String> g : graphs) {
            g.readFromFile(stringFile, Function.identity());
            assertEquals(Set.of("A", "B", "C", "D"), g.getVertices());
            assertTrue(g.hasEdge("A", "B"));
            assertTrue(g.hasEdge("B", "C"));
            assertTrue(g.getNeighbors("D").isEmpty());
        }

        assertEquals(graphs.get(0), graphs.get(1));
        assertEquals(graphs.get(1), graphs.get(2));

        Path intFile = tempDir.resolve("graph_int.txt");
        String intContent = """
                10 20
                20 30
                40
                """;
        Files.writeString(intFile, intContent);

        Graph<Integer> intGraph = new AdjacencyListGraph<>();
        intGraph.readFromFile(intFile, Integer::parseInt);
        assertEquals(Set.of(10, 20, 30, 40), intGraph.getVertices());
        assertTrue(intGraph.hasEdge(10, 20));
        assertTrue(intGraph.hasEdge(20, 30));
        assertTrue(intGraph.getNeighbors(40).isEmpty());
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