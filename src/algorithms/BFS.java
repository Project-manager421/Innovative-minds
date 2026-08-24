package algorithms;

import graph.Graph;
import model.Vertex;
import datastructures.MyQueue;
import datastructures.MyLinkedList;

public class BFS {

    public static void traverse(Graph graph, Vertex start) {

        MyQueue<Vertex> queue = new MyQueue<>();
        MyLinkedList<Vertex> visited = new MyLinkedList<>();

        queue.enqueue(start);
        visited.add(start);

        while (!queue.isEmpty()) {

            Vertex current = queue.dequeue();

            System.out.println("Visited: " + current.getName());

            MyLinkedList<Vertex> neighbors =
                    graph.getNeighbors(current);

            for (int i = 0; i < neighbors.size(); i++) {

                Vertex neighbor = neighbors.get(i);

                if (!contains(visited, neighbor)) {

                    visited.add(neighbor);
                    queue.enqueue(neighbor);
                }
            }
        }
    }

    private static boolean contains(
            MyLinkedList<Vertex> list,
            Vertex vertex) {

        for (int i = 0; i < list.size(); i++) {

            if (list.get(i) == vertex) {
                return true;
            }
        }

        return false;
    }
}