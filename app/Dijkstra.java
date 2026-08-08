package app;

/**
 * Dijkstra shortest-path algorithm implemented with arrays only.
 *
 * Travel time is used as the edge weight.
 * Distance is calculated separately for display.
 *
 * No java.util collection classes are used.
 */
public class Dijkstra {

    private static Digraph lastGraph;
    private static Locations[] vertices;
    private static long[] travelTimes;
    private static Locations[] previous;
    private static boolean[] visited;

    private static Locations lastSource;
    private static Locations lastDestination;

    public static void findShortestPath(
            Digraph graph,
            Locations source,
            Locations destination) {

        if (graph == null
                || source == null
                || destination == null) {

            System.out.println(
                    "Graph, source, or destination was not found."
            );

            return;
        }

        vertices = graph.getNodes();
        int numberOfVertices = vertices.length;

        travelTimes = new long[numberOfVertices];
        previous = new Locations[numberOfVertices];
        visited = new boolean[numberOfVertices];

        lastGraph = graph;
        lastSource = source;
        lastDestination = destination;

        for (int i = 0; i < numberOfVertices; i++) {
            travelTimes[i] = Long.MAX_VALUE;
            previous[i] = null;
            visited[i] = false;
        }

        int sourceIndex = indexOf(source);
        int destinationIndex = indexOf(destination);

        if (sourceIndex == -1 || destinationIndex == -1) {
            System.out.println(
                    "Source or destination does not exist in the graph."
            );

            return;
        }

        travelTimes[sourceIndex] = 0;

        for (int count = 0;
             count < numberOfVertices;
             count++) {

            int currentIndex =
                    findVertexWithMinimumTravelTime();

            if (currentIndex == -1) {
                break;
            }

            visited[currentIndex] = true;

            if (vertices[currentIndex].equals(destination)) {
                break;
            }

            Extent[] outgoingEdges =
                    graph.getDestinationEdges(
                            vertices[currentIndex]
                    );

            for (int i = 0;
                 i < outgoingEdges.length;
                 i++) {

                Extent edge = outgoingEdges[i];
                Locations nextLocation =
                        edge.getDestination();

                int nextIndex =
                        indexOf(nextLocation);

                if (nextIndex == -1
                        || visited[nextIndex]
                        || travelTimes[currentIndex]
                        == Long.MAX_VALUE) {
                    continue;
                }

                long alternativeTime =
                        travelTimes[currentIndex]
                                + edge.getTime();

                if (alternativeTime
                        < travelTimes[nextIndex]) {

                    travelTimes[nextIndex] =
                            alternativeTime;

                    previous[nextIndex] =
                            vertices[currentIndex];
                }
            }
        }

        printShortestPath(source, destination);
        printRouteInformation(destination);
    }

    /**
     * Returns the total physical distance of the selected route
     * in kilometres.
     */
    public static float getTotalDistance(
            Locations destination) {

        if (lastGraph == null
                || lastSource == null
                || destination == null
                || travelTimes == null) {

            return -1.0F;
        }

        int destinationIndex =
                indexOf(destination);

        if (destinationIndex == -1
                || travelTimes[destinationIndex]
                == Long.MAX_VALUE) {

            return -1.0F;
        }

        int totalMetres = 0;
        Locations current = destination;

        while (!current.equals(lastSource)) {

            int currentIndex =
                    indexOf(current);

            if (currentIndex == -1
                    || previous[currentIndex] == null) {
                return -1.0F;
            }

            Locations previousLocation =
                    previous[currentIndex];

            Extent edge =
                    lastGraph.getEdge(
                            previousLocation,
                            current
                    );

            if (edge == null) {
                return -1.0F;
            }

            totalMetres += edge.getDistance();
            current = previousLocation;
        }

        return totalMetres / 1000.0F;
    }

    /**
     * Returns the total travel time of the selected route
     * in minutes.
     */
    public static long getTotalTravelTime(
            Locations destination) {

        if (travelTimes == null
                || destination == null) {
            return -1;
        }

        int destinationIndex =
                indexOf(destination);

        if (destinationIndex == -1
                || travelTimes[destinationIndex]
                == Long.MAX_VALUE) {

            return -1;
        }

        return travelTimes[destinationIndex];
    }

    /**
     * Returns the selected route as a readable string.
     */
    public static String getShortestPath(
            Locations source,
            Locations destination) {

        if (vertices == null
                || travelTimes == null
                || previous == null) {

            return "No route calculated.";
        }

        int sourceIndex =
                indexOf(source);

        int destinationIndex =
                indexOf(destination);

        if (sourceIndex == -1
                || destinationIndex == -1) {

            return "No route available.";
        }

        if (travelTimes[destinationIndex]
                == Long.MAX_VALUE) {

            return "No route available.";
        }

        Locations[] path =
                new Locations[vertices.length];

        int pathSize = 0;
        Locations current = destination;

        while (current != null
                && pathSize < path.length) {

            path[pathSize] = current;
            pathSize++;

            if (current.equals(source)) {
                break;
            }

            int currentIndex =
                    indexOf(current);

            if (currentIndex == -1) {
                return "No route available.";
            }

            current = previous[currentIndex];
        }

        if (current == null
                || !current.equals(source)) {

            return "No route available.";
        }

        StringBuilder result =
                new StringBuilder();

        for (int i = pathSize - 1; i >= 0; i--) {

            result.append(path[i].getName());

            if (i > 0) {
                result.append(" -> ");
            }
        }

        return result.toString();
    }

    private static int findVertexWithMinimumTravelTime() {

        int minimumIndex = -1;
        long minimumTime = Long.MAX_VALUE;

        for (int i = 0; i < vertices.length; i++) {

            if (!visited[i]
                    && travelTimes[i] < minimumTime) {

                minimumTime = travelTimes[i];
                minimumIndex = i;
            }
        }

        return minimumIndex;
    }

    private static int indexOf(Locations location) {

        if (vertices == null
                || location == null) {

            return -1;
        }

        for (int i = 0; i < vertices.length; i++) {

            if (vertices[i] != null
                    && vertices[i].equals(location)) {

                return i;
            }
        }

        return -1;
    }

    private static void printShortestPath(
            Locations source,
            Locations destination) {

        System.out.println();

        System.out.println(
                "Fastest path from '"
                        + source.getName()
                        + "' to '"
                        + destination.getName()
                        + "'"
        );

        System.out.println(
                getShortestPath(source, destination)
        );
    }

    private static void printRouteInformation(
            Locations destination) {

        float distance =
                getTotalDistance(destination);

        long time =
                getTotalTravelTime(destination);

        if (distance < 0 || time < 0) {

            System.out.println(
                    "Route information unavailable."
            );

            return;
        }

        System.out.println(
                "Approximate distance: "
                        + distance
                        + " km"
        );

        System.out.println(
                "Estimated travel time: "
                        + time
                        + " minutes"
        );
    }
}