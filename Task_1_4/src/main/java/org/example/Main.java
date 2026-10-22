package org.example;

import java.util.List;
public class Main {
    public Main() {
    }

    public static void main(String[] args) {
        Graph<String> adjList = new AdjacencyListGraph<>();
        Graph<String> adjMatrix = new AdjacencyMatrixGraph<>();
        Graph<String> incMatrix = new IncidenceMatrixGraph<>();

        for (Graph<String> g : List.of(adjList, adjMatrix, incMatrix)) {
            g.addEdge("A", "C");
            g.addEdge("B", "C");
            g.addEdge("C", "D");
        }

        System.out.println(adjList);
        System.out.println(adjMatrix);
        System.out.println(incMatrix);

        System.out.println("adjList.equals(adjMatrix): " + adjList.equals(adjMatrix));
        System.out.println("adjMatrix.equals(incMatrix): " + adjMatrix.equals(incMatrix));

        System.out.println("topSort: " + TopologicalSort.topSort(adjList));
    }
}