package graph;

import model.Vertex;
import model.Edge;
import datastructures.MyLinkedList;

public class Graph {

    private MyLinkedList<Vertex> vertices;
    private MyLinkedList<Edge> edges;

    public Graph() {
        vertices = new MyLinkedList<>();
        edges = new MyLinkedList<>();
    }

    public void addVertex(Vertex vertex) {
        vertices.add(vertex);
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    public MyLinkedList<Vertex> getVertices() {
        return vertices;
    }

    public MyLinkedList<Edge> getEdges() {
        return edges;
    }

    public void displayVertices() {
        for (int i = 0; i < vertices.size(); i++) {
            System.out.println(vertices.get(i));
        }
    }

    public void displayEdges() {
        for (int i = 0; i < edges.size(); i++) {
            System.out.println(edges.get(i));
        }
    }

    public MyLinkedList<Vertex> getNeighbors(Vertex vertex) {

        MyLinkedList<Vertex> neighbors = new MyLinkedList<>();

        for (int i = 0; i < edges.size(); i++) {

            Edge edge = edges.get(i);

            if (edge.getFrom() == vertex) {
                neighbors.add(edge.getTo());
            }

            if (edge.getTo() == vertex) {
                neighbors.add(edge.getFrom());
            }
        }

        return neighbors;
    }

    public Edge getEdge(Vertex from, Vertex to) {

        for (int i = 0; i < edges.size(); i++) {

            Edge edge = edges.get(i);

            if ((edge.getFrom() == from && edge.getTo() == to)
                    || (edge.getFrom() == to && edge.getTo() == from)) {

                return edge;
            }
        }

        return null;
    }

    public Vertex findVertex(String id) {

        for (int i = 0; i < vertices.size(); i++) {

            Vertex vertex = vertices.get(i);

            if (vertex.getId().equals(id)) {
                return vertex;
            }
        }

        return null;
    }

    // Find a vertex using its location name
    public Vertex findVertexByName(String name) {

        for (int i = 0; i < vertices.size(); i++) {

            Vertex vertex = vertices.get(i);

            if (vertex.getName().equalsIgnoreCase(name)) {
                return vertex;
            }
        }

        return null;
    }

    // Verify that every edge has both endpoints
    public void verifyEdges() {

        int validEdges = 0;
        int invalidEdges = 0;

        for (int i = 0; i < edges.size(); i++) {

            Edge edge = edges.get(i);

            if (edge.getFrom() != null &&
                edge.getTo() != null) {

                validEdges++;

            } else {

                invalidEdges++;

                System.out.println(
                        "Invalid edge: " + edge
                );
            }
        }

        System.out.println();
        System.out.println("===== EDGE VERIFICATION =====");
        System.out.println("Valid edges: " + validEdges);
        System.out.println("Invalid edges: " + invalidEdges);
    }
}