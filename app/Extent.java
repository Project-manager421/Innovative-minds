package app;

/**
 * Represents one road or edge in the campus graph.
 *
 * Distance is stored in metres.
 * Travel time is stored in minutes.
 */
public class Extent {

    private final Locations source;
    private final Locations destination;
    private final int distance;
    private final long time;

    public Extent(
            Locations source,
            Locations destination,
            int distance) {

        this(source, destination, distance, 0);
    }

    public Extent(
            Locations source,
            Locations destination,
            int distance,
            long time) {

        this.source = source;
        this.destination = destination;
        this.distance = distance;
        this.time = time;
    }

    public Locations getSource() {
        return source;
    }

    public Locations getDestination() {
        return destination;
    }

    public int getDistance() {
        return distance;
    }

    public long getTime() {
        return time;
    }

    @Override
    public String toString() {
        return source.getName()
                + " -> "
                + destination.getName()
                + " | Distance: "
                + distance
                + "m | Time: "
                + time
                + " minutes";
    }
}