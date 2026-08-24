package algorithms;

import graph.Graph;
import model.Vertex;
import datastructures.MyStack;
import datastructures.MyLinkedList;

public class DFS {

    public static void traverse(Graph graph, Vertex start) {

        MyStack<Vertex> stack = new MyStack<>();
        MyLinkedList<Vertex> visited = new MyLinkedList<>();

        stack.push(start);

        while (!stack.isEmpty()) {

            Vertex current = stack.pop();

            if (contains(visited, current)) {
                continue;
            }

            visited.add(current);

            System.out.println("Visited: " + current.getName());

            MyLinkedList<Vertex> neighbors =
                    graph.getNeighbors(current);

            for (int i = neighbors.size() - 1; i >= 0; i--) {

                Vertex neighbor = neighbors.get(i);

                if (!contains(visited, neighbor)) {
                    stack.push(neighbor);
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