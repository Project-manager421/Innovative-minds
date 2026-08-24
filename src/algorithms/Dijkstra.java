package algorithms;

import graph.Graph;
import model.Vertex;
import model.Edge;
import datastructures.MyMinHeap;
import datastructures.MyLinkedList;

public class Dijkstra {

    public static void findShortestPath(
            Graph graph,
            Vertex start,
            Vertex destination) {

        int numberOfVertices = graph.getVertices().size();

        Vertex[] vertices = new Vertex[numberOfVertices];
        double[] distances = new double[numberOfVertices];
        Vertex[] previous = new Vertex[numberOfVertices];
        boolean[] visited = new boolean[numberOfVertices];

        for (int i = 0; i < numberOfVertices; i++) {

            vertices[i] = graph.getVertices().get(i);

            distances[i] = Double.POSITIVE_INFINITY;

            previous[i] = null;

            visited[i] = false;
        }

        int startIndex = getIndex(vertices, start);

        distances[startIndex] = 0;

        MyMinHeap<Vertex> heap =
                new MyMinHeap<>(numberOfVertices);

        heap.insert(start, 0);

        while (!heap.isEmpty()) {

            MyMinHeap.HeapNode<Vertex> node =
                    heap.extractMin();

            Vertex current = node.getData();

            int currentIndex =
                    getIndex(vertices, current);

            if (visited[currentIndex]) {
                continue;
            }

            visited[currentIndex] = true;

            if (current == destination) {
                break;
            }

            MyLinkedList<Vertex> neighbors =
                    graph.getNeighbors(current);

            for (int i = 0; i < neighbors.size(); i++) {

                Vertex neighbor = neighbors.get(i);

                int neighborIndex =
                        getIndex(vertices, neighbor);

                if (visited[neighborIndex]) {
                    continue;
                }

                Edge edge =
                        graph.getEdge(current, neighbor);

                double newDistance =
                        distances[currentIndex]
                        + edge.getTravelTime();

                if (newDistance <
                        distances[neighborIndex]) {

                    distances[neighborIndex] =
                            newDistance;

                    previous[neighborIndex] =
                            current;

                    heap.insert(
                            neighbor,
                            newDistance
                    );
                }
            }
        }

        printPath(
                vertices,
                distances,
                previous,
                start,
                destination
        );
    }

    private static int getIndex(
            Vertex[] vertices,
            Vertex target) {

        for (int i = 0; i < vertices.length; i++) {

            if (vertices[i] == target) {
                return i;
            }
        }

        return -1;
    }

    private static void printPath(
            Vertex[] vertices,
            double[] distances,
            Vertex[] previous,
            Vertex start,
            Vertex destination) {

        int destinationIndex =
                getIndex(vertices, destination);

        if (distances[destinationIndex] ==
                Double.POSITIVE_INFINITY) {

            System.out.println(
                    "No route found."
            );

            return;
        }

        MyLinkedList<Vertex> path =
                new MyLinkedList<>();

        Vertex current = destination;

        while (current != null) {

            path.add(current);

            if (current == start) {
                break;
            }

            int index =
                    getIndex(vertices, current);

            current = previous[index];
        }

        System.out.println();
        System.out.println("Shortest Route:");

        for (int i = path.size() - 1; i >= 0; i--) {

            System.out.print(
                    path.get(i).getName()
            );

            if (i > 0) {
                System.out.print(" -> ");
            }
        }

        System.out.println();

        System.out.println(
                "Total Travel Time: "
                + distances[destinationIndex]
                + " minutes"
        );
    }
}