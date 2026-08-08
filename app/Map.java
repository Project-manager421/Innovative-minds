package app;

/**
 * Undirected campus map.
 *
 * Every supplied road is stored in both directions.
 * For example:
 *
 * A -> B
 * B -> A
 */
public class Map extends Digraph {

    @Override
    public void addEdge(Extent edge) {
        if (edge == null) {
            return;
        }

        if (edge.getSource() == null
                || edge.getDestination() == null) {
            return;
        }

        // Do not add the same forward edge twice.
        if (getEdge(
                edge.getSource(),
                edge.getDestination()) != null) {
            return;
        }

        // Store the original direction.
        addStoredEdge(edge);

        // Store the reverse direction if it does not already exist.
        if (getEdge(
                edge.getDestination(),
                edge.getSource()) == null) {

            Extent reverseEdge = new Extent(
                    edge.getDestination(),
                    edge.getSource(),
                    edge.getDistance(),
                    edge.getTime()
            );

            addStoredEdge(reverseEdge);
        }
    }

    public int getDistance(
            Locations source,
            Locations destination) {

        Extent edge = getEdge(source, destination);

        if (edge == null) {
            return -1;
        }

        return edge.getDistance();
    }

    public long getTravelTime(
            Locations source,
            Locations destination) {

        Extent edge = getEdge(source, destination);

        if (edge == null) {
            return -1;
        }

        return edge.getTime();
    }
}