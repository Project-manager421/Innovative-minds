package app;

/**
 * Custom directed graph implementation for the UG routing project.
 *
 * The graph uses arrays only. No java.util collection classes are used.
 */
public class Digraph {

    protected Locations[] vertices = new Locations[16];
    protected Extent[] ghu = new Extent[128];

    protected int edgeSize = 0;
    private int vertexSize = 0;

    public void addVertex(Locations vertex) {
        if (vertex == null) {
            return;
        }

        if (getNodeByName(vertex.getName()) != null) {
            return;
        }

        ensureVertexCapacity();
        vertices[vertexSize] = vertex;
        vertexSize++;
    }

    protected void addStoredEdge(Extent edge) {
        if (edge == null) {
            return;
        }

        ensureEdgeCapacity();
        ghu[edgeSize] = edge;
        edgeSize++;
    }

    public void addEdge(Extent edge) {
        if (edge == null) {
            return;
        }

        if (edge.getSource() == null
                || edge.getDestination() == null) {
            return;
        }

        if (getEdge(
                edge.getSource(),
                edge.getDestination()) != null) {
            return;
        }

        addStoredEdge(edge);
    }

    public Extent[] getDestinationEdges(Locations source) {
        if (source == null) {
            return new Extent[0];
        }

        int count = 0;

        for (int i = 0; i < edgeSize; i++) {
            if (ghu[i].getSource().equals(source)) {
                count++;
            }
        }

        Extent[] result = new Extent[count];
        int index = 0;

        for (int i = 0; i < edgeSize; i++) {
            if (ghu[i].getSource().equals(source)) {
                result[index] = ghu[i];
                index++;
            }
        }

        return result;
    }

    public Extent getEdge(
            Locations source,
            Locations destination) {

        if (source == null || destination == null) {
            return null;
        }

        for (int i = 0; i < edgeSize; i++) {
            boolean sameSource =
                    ghu[i].getSource().equals(source);

            boolean sameDestination =
                    ghu[i].getDestination().equals(destination);

            if (sameSource && sameDestination) {
                return ghu[i];
            }
        }

        return null;
    }

    public Locations getNodeByName(String name) {
        if (name == null) {
            return null;
        }

        for (int i = 0; i < vertexSize; i++) {
            if (vertices[i].getName().equalsIgnoreCase(name)) {
                return vertices[i];
            }
        }

        return null;
    }

    public Locations[] getNodes() {
        Locations[] result = new Locations[vertexSize];

        for (int i = 0; i < vertexSize; i++) {
            result[i] = vertices[i];
        }

        return result;
    }

    public Locations[] getAllNodes() {
        return getNodes();
    }

    public String[] getAllNodeNames() {
        String[] result = new String[vertexSize];

        for (int i = 0; i < vertexSize; i++) {
            result[i] = vertices[i].getName();
        }

        return result;
    }

    public int getNodeSize() {
        return vertexSize;
    }

    public int getEdgeSize() {
        return edgeSize;
    }

    public void printGraph() {
        System.out.println();
        System.out.println("GRAPH: ADJACENCY LIST");
        System.out.println("PLACES ON CAMPUS");
        System.out.println();

        for (int i = 0; i < vertexSize; i++) {
            Locations vertex = vertices[i];
            Extent[] destinations =
                    getDestinationEdges(vertex);

            StringBuilder builder =
                    new StringBuilder("[");

            for (int j = 0; j < destinations.length; j++) {
                if (j > 0) {
                    builder.append(", ");
                }

                builder.append(
                        destinations[j]
                                .getDestination()
                                .getName()
                );
            }

            builder.append("]");

            System.out.println(
                    vertex.getName()
                            + " --> "
                            + builder
            );
        }
    }

    public void listPlaces(Locations except) {
        int displayNumber = 1;

        for (int i = 0; i < vertexSize; i++) {
            if (vertices[i] != except) {
                System.out.println(
                        displayNumber
                                + ". "
                                + vertices[i].getName()
                );

                displayNumber++;
            }
        }
    }

    private void ensureVertexCapacity() {
        if (vertexSize < vertices.length) {
            return;
        }

        Locations[] larger =
                new Locations[vertices.length * 2];

        for (int i = 0; i < vertices.length; i++) {
            larger[i] = vertices[i];
        }

        vertices = larger;
    }

    private void ensureEdgeCapacity() {
        if (edgeSize < ghu.length) {
            return;
        }

        Extent[] larger =
                new Extent[ghu.length * 2];

        for (int i = 0; i < ghu.length; i++) {
            larger[i] = ghu[i];
        }

        ghu = larger;
    }
}