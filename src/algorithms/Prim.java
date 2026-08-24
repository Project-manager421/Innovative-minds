package algorithms;

import graph.Graph;
import model.Vertex;
import model.Edge;
import datastructures.MyMinHeap;

public class Prim {

    public static void findMST(Graph graph, Vertex start) {

        int numberOfVertices = graph.getVertices().size();

        boolean[] inMST = new boolean[numberOfVertices];

        MyMinHeap<Edge> heap =
                new MyMinHeap<>(numberOfVertices);

        int startIndex = getIndex(
                graph,
                start
        );

        inMST[startIndex] = true;

        addEdgesToHeap(
                graph,
                start,
                inMST,
                heap
        );

        double totalWeight = 0;

        int edgesSelected = 0;

        System.out.println();
        System.out.println("Prim's Minimum Spanning Tree:");

        while (!heap.isEmpty()
                && edgesSelected < numberOfVertices - 1) {

            MyMinHeap.HeapNode<Edge> node =
                    heap.extractMin();

            Edge edge = node.getData();

            Vertex from = edge.getFrom();
            Vertex to = edge.getTo();

            int fromIndex =
                    getIndex(graph, from);

            int toIndex =
                    getIndex(graph, to);

            if (inMST[fromIndex]
                    && inMST[toIndex]) {

                continue;
            }

            Vertex newVertex;

            if (!inMST[fromIndex]) {
                newVertex = from;
            } else {
                newVertex = to;
            }

            int newIndex =
                    getIndex(graph, newVertex);

            if (inMST[newIndex]) {
                continue;
            }

            inMST[newIndex] = true;

            System.out.println(
                    from.getName()
                    + " -> "
                    + to.getName()
                    + " : "
                    + edge.getTravelTime()
                    + " minutes"
            );

            totalWeight += edge.getTravelTime();

            edgesSelected++;

            addEdgesToHeap(
                    graph,
                    newVertex,
                    inMST,
                    heap
            );
        }

        System.out.println(
                "Total MST Travel Time: "
                + totalWeight
                + " minutes"
        );
    }

    private static void addEdgesToHeap(
            Graph graph,
            Vertex vertex,
            boolean[] inMST,
            MyMinHeap<Edge> heap) {

        for (int i = 0;
                i < graph.getEdges().size();
                i++) {

            Edge edge =
                    graph.getEdges().get(i);

            Vertex other = null;

            if (edge.getFrom() == vertex) {
                other = edge.getTo();
            }

            else if (edge.getTo() == vertex) {
                other = edge.getFrom();
            }

            if (other != null) {

                int otherIndex =
                        getIndex(graph, other);

                if (!inMST[otherIndex]) {

                    heap.insert(
                            edge,
                            edge.getTravelTime()
                    );
                }
            }
        }
    }

    private static int getIndex(
            Graph graph,
            Vertex vertex) {

        for (int i = 0;
                i < graph.getVertices().size();
                i++) {

            if (graph.getVertices().get(i) == vertex) {
                return i;
            }
        }

        return -1;
    }
}