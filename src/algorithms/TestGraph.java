package algorithms;

import graph.Graph;
import model.Vertex;
import model.Edge;

public class TestGraph {

    public static void main(String[] args) {

        Graph graph = new Graph();

        // Create UG locations
        Vertex mainGate =
                new Vertex("L001", "Main Gate");

        Vertex balme =
                new Vertex("L002", "Balme Library");

        Vertex cs =
                new Vertex("L003", "Computer Science Department");

        Vertex greatHall =
                new Vertex("L004", "Great Hall");

        // Add locations to graph
        graph.addVertex(mainGate);
        graph.addVertex(balme);
        graph.addVertex(cs);
        graph.addVertex(greatHall);

        // Add roads
        graph.addEdge(
                new Edge(
                        "E001",
                        mainGate,
                        balme,
                        0.5,
                        3
                )
        );

        graph.addEdge(
                new Edge(
                        "E002",
                        balme,
                        cs,
                        0.8,
                        4
                )
        );

        graph.addEdge(
                new Edge(
                        "E003",
                        cs,
                        greatHall,
                        1.1,
                        6
                )
        );

System.out.println("===== KRUSKAL TEST =====");

Kruskal.findMST(graph);
    }
}