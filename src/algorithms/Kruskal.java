package algorithms;

import graph.Graph;
import model.Vertex;
import model.Edge;
import datastructures.MyMinHeap;
import datastructures.MyDisjointSet;

public class Kruskal {

    public static void findMST(Graph graph) {

        int numberOfVertices =
                graph.getVertices().size();

        int numberOfEdges =
                graph.getEdges().size();

        MyDisjointSet disjointSet =
                new MyDisjointSet(numberOfVertices);

        MyMinHeap<Edge> heap =
                new MyMinHeap<>(numberOfEdges);

        // Put every road into the min heap.
        for (int i = 0; i < numberOfEdges; i++) {

            Edge edge =
                    graph.getEdges().get(i);

            heap.insert(
                    edge,
                    edge.getTravelTime()
            );
        }

        double totalWeight = 0;

        int edgesSelected = 0;

        System.out.println();
        System.out.println(
                "Kruskal's Minimum Spanning Tree:"
        );

        while (!heap.isEmpty()
                && edgesSelected < numberOfVertices - 1) {

            MyMinHeap.HeapNode<Edge> node =
                    heap.extractMin();

            Edge edge = node.getData();

            int fromIndex =
                    getIndex(
                            graph,
                            edge.getFrom()
                    );

            int toIndex =
                    getIndex(
                            graph,
                            edge.getTo()
                    );

            /*
             * union() returns true if the two
             * locations belong to different
             * groups.
             *
             * If true, adding the road does
             * not create a cycle.
             */
            if (disjointSet.union(
                    fromIndex,
                    toIndex)) {

                System.out.println(
                        edge.getFrom().getName()
                        + " -> "
                        + edge.getTo().getName()
                        + " : "
                        + edge.getTravelTime()
                        + " minutes"
                );

                totalWeight +=
                        edge.getTravelTime();

                edgesSelected++;
            }
        }

        System.out.println(
                "Total MST Travel Time: "
                + totalWeight
                + " minutes"
        );
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