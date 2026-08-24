import data.CSVLoader;
import graph.Graph;
import model.Vertex;
import algorithms.BFS;
import algorithms.DFS;
import algorithms.Dijkstra;
import algorithms.Prim;
import algorithms.Kruskal;


public class TestCSVLoader {

    public static void main(String[] args) {

        Graph graph = new Graph();

        try {

            // ========================================
            // LOAD REAL UG DATASET
            // ========================================

            CSVLoader.loadLocations(
                    "data/locations.csv",
                    graph
            );

            CSVLoader.loadRoads(
                    "data/roads.csv",
                    graph
            );

            System.out.println();
            System.out.println("===== DATASET SUMMARY =====");

            System.out.println(
                    "Total locations: "
                    + graph.getVertices().size()
            );

            System.out.println(
                    "Total roads: "
                    + graph.getEdges().size()
            );

            graph.verifyEdges();

            

            // ========================================
            // FIND STARTING LOCATIONS
            // ========================================

            Vertex mainGate =
                    graph.findVertexByName("Main Gate");

            Vertex greatHall =
                    graph.findVertexByName("Great Hall");


            // ========================================
            // BFS
            // ========================================

            System.out.println();
            System.out.println("===== BFS TEST =====");

            BFS.traverse(
                    graph,
                    mainGate
            );


            // ========================================
            // DFS
            // ========================================

            System.out.println();
            System.out.println("===== DFS TEST =====");

            DFS.traverse(
                    graph,
                    mainGate
            );


            // ========================================
            // DIJKSTRA
            // ========================================

            System.out.println();
            System.out.println("===== DIJKSTRA TEST =====");

            Dijkstra.findShortestPath(
                    graph,
                    mainGate,
                    greatHall
            );


            // ========================================
            // PRIM
            // ========================================

            System.out.println();
            System.out.println("===== PRIM TEST =====");

            Prim.findMST(
                    graph,
                    mainGate
            );


            // ========================================
            // KRUSKAL
            // ========================================

            System.out.println();
            System.out.println("===== KRUSKAL TEST =====");

            Kruskal.findMST(graph);


        } catch (Exception e) {

            System.out.println();
            System.out.println("ERROR:");

            e.printStackTrace();
        }
    }
}