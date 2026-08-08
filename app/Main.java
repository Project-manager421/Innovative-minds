package app;

public class Main {

    public static Map graph = new Map();

    private static final int EXPECTED_LOCATION_COUNT = 50;
    private static final int EXPECTED_ROAD_COUNT = 100;

    private static final String[] LOCATION_NAMES = {
        "Main Gate",
        "Legon Main Entrance",
        "University Square",
        "Registry",
        "Great Hall",
        "Balme Library",
        "Computer Science Department",
        "School of Engineering Sciences",
        "ICT Directorate",
        "Department of Mathematics",
        "Department of Physics",
        "Department of Chemistry",
        "Department of Earth Science",
        "Centre for Remote Sensing & GIS",
        "Institute for Environment & Sanitation Studies",
        "School of Communication Studies",
        "School of Performing Arts",
        "University of Ghana Business School",
        "School of Law",
        "Graduate School",
        "Department of Plant & Environmental Biology",
        "School of Biological Sciences",
        "WACCBIP",
        "UG Hospital",
        "School of Public Health",
        "Medical School",
        "Dental School",
        "Legon Hall",
        "Legon Hall Annex A",
        "Akuafo Hall",
        "Mensah Sarbah Hall",
        "Commonwealth Hall",
        "Volta Hall",
        "Jean Nelson Aka Hall",
        "Diaspora Halls",
        "Jubilee Hall",
        "Bush Canteen",
        "Night Market",
        "Banking Square",
        "Central Cafeteria",
        "Athletic Oval",
        "Sports Directorate",
        "Shuttle Terminal",
        "Maintenance Workshop",
        "Security Services HQ",
        "Fire Station",
        "Visitor Car Park",
        "Staff Village Junction",
        "University Guest Centre",
        "Cedi Conference Centre"
    };

    private static final String[] FROM = {
        "Main Gate", "Legon Main Entrance", "University Square",
        "Registry", "Great Hall", "Balme Library",
        "Computer Science Department", "School of Engineering Sciences",
        "ICT Directorate", "Department of Mathematics",
        "Department of Physics", "Department of Chemistry",
        "Department of Earth Science",
        "Centre for Remote Sensing & GIS",
        "Institute for Environment & Sanitation Studies",
        "School of Communication Studies", "School of Performing Arts",
        "Central Cafeteria", "Bush Canteen", "Night Market",
        "Banking Square", "University of Ghana Business School",
        "Cedi Conference Centre", "Graduate School", "School of Law",
        "Graduate School", "Department of Plant & Environmental Biology",
        "School of Biological Sciences", "WACCBIP", "UG Hospital",
        "School of Public Health", "Medical School", "Dental School",
        "Volta Hall", "Commonwealth Hall", "Mensah Sarbah Hall",
        "Legon Hall", "Legon Hall", "Akuafo Hall",
        "Athletic Oval", "Sports Directorate", "Bush Canteen",
        "University Guest Centre", "Cedi Conference Centre",
        "Banking Square", "Visitor Car Park", "Main Gate",
        "Shuttle Terminal", "Staff Village Junction",
        "Security Services HQ", "Fire Station", "Maintenance Workshop",
        "Maintenance Workshop", "Security Services HQ",
        "Security Services HQ", "Shuttle Terminal", "Shuttle Terminal",
        "Great Hall", "University of Ghana Business School",
        "Balme Library", "Balme Library",
        "Computer Science Department", "Computer Science Department",
        "School of Engineering Sciences", "Department of Chemistry",
        "School of Biological Sciences", "Medical School", "Dental School",
        "Volta Hall", "Jean Nelson Aka Hall", "Diaspora Halls",
        "Jubilee Hall", "Night Market", "Athletic Oval",
        "Central Cafeteria", "Great Hall", "Registry", "Registry",
        "University Square", "Computer Science Department",
        "School of Engineering Sciences", "Department of Earth Science",
        "Institute for Environment & Sanitation Studies",
        "School of Communication Studies", "School of Performing Arts",
        "Central Cafeteria", "Bush Canteen", "Athletic Oval",
        "Mensah Sarbah Hall", "Commonwealth Hall",
        "Jean Nelson Aka Hall", "UG Hospital",
        "School of Public Health", "Medical School",
        "Sports Directorate", "Shuttle Terminal", "Visitor Car Park",
        "Banking Square", "Staff Village Junction", "Fire Station"
    };

    private static final String[] TO = {
        "Legon Main Entrance", "University Square", "Registry",
        "Great Hall", "Balme Library",
        "Computer Science Department", "School of Engineering Sciences",
        "ICT Directorate", "Department of Mathematics",
        "Department of Physics", "Department of Chemistry",
        "Department of Earth Science",
        "Centre for Remote Sensing & GIS",
        "Institute for Environment & Sanitation Studies",
        "School of Communication Studies", "School of Performing Arts",
        "Central Cafeteria", "Bush Canteen", "Night Market",
        "Banking Square", "University of Ghana Business School",
        "Cedi Conference Centre", "Graduate School", "School of Law",
        "Balme Library", "Department of Plant & Environmental Biology",
        "School of Biological Sciences", "WACCBIP", "UG Hospital",
        "School of Public Health", "Medical School", "Dental School",
        "Volta Hall", "Commonwealth Hall", "Mensah Sarbah Hall",
        "Legon Hall", "Legon Hall Annex A", "Akuafo Hall",
        "Athletic Oval", "Sports Directorate", "Bush Canteen",
        "University Guest Centre", "Cedi Conference Centre",
        "Banking Square", "Visitor Car Park", "Main Gate",
        "Shuttle Terminal", "Staff Village Junction",
        "Security Services HQ", "Fire Station", "Maintenance Workshop",
        "UG Hospital", "Registry", "Registry", "Main Gate",
        "University Square", "Great Hall",
        "University of Ghana Business School", "School of Law",
        "University of Ghana Business School", "Department of Mathematics",
        "Department of Physics", "ICT Directorate",
        "Department of Chemistry", "School of Biological Sciences",
        "School of Public Health", "WACCBIP", "Commonwealth Hall",
        "Jean Nelson Aka Hall", "Diaspora Halls", "Jubilee Hall",
        "Night Market", "Bush Canteen", "Central Cafeteria",
        "Banking Square", "School of Communication Studies",
        "Balme Library", "University of Ghana Business School",
        "Great Hall", "School of Law", "Department of Mathematics",
        "School of Biological Sciences",
        "Department of Plant & Environmental Biology",
        "University Guest Centre", "Night Market",
        "University Guest Centre", "Athletic Oval", "Akuafo Hall",
        "Volta Hall", "Jean Nelson Aka Hall", "UG Hospital",
        "Security Services HQ", "Maintenance Workshop",
        "Sports Directorate", "Shuttle Terminal", "Visitor Car Park",
        "Banking Square", "Main Gate", "Shuttle Terminal", "Main Gate"
    };

    private static final double[] DISTANCE_KM = {
        0.35, 0.30, 0.18, 0.15, 0.32, 0.42, 0.18, 0.12, 0.25, 0.14,
        0.16, 0.23, 0.17, 0.20, 0.24, 0.30, 0.22, 0.25, 0.38, 0.20,
        0.25, 0.16, 0.28, 0.30, 0.45, 0.22, 0.18, 0.15, 0.45, 0.15,
        0.12, 0.10, 0.35, 0.28, 0.32, 0.30, 0.10, 0.25, 0.35, 0.12,
        0.32, 0.38, 0.30, 0.22, 0.18, 0.15, 0.22, 0.40, 0.35, 0.15,
        0.20, 0.35, 0.65, 0.70, 0.45, 0.35, 0.40, 0.50, 0.45, 0.40,
        0.38, 0.25, 0.15, 0.25, 0.40, 0.45, 0.28, 0.55, 0.30, 0.18,
        0.22, 0.45, 0.35, 0.25, 0.30, 0.55, 0.25, 0.45, 0.20, 0.40,
        0.20, 0.30, 0.35, 0.45, 0.35, 0.20, 0.30, 0.28, 0.45, 0.40,
        0.55, 0.40, 0.35, 0.60, 0.40, 0.20, 0.25, 0.35, 0.30, 0.25
    };

    private static final int[] TRAVEL_TIME_MIN = {
        4, 4, 2, 2, 4, 5, 2, 2, 3, 2,
        2, 3, 2, 3, 3, 4, 3, 3, 5, 3,
        3, 2, 4, 4, 6, 3, 2, 2, 6, 2,
        2, 2, 5, 4, 4, 4, 2, 3, 5, 2,
        4, 5, 4, 3, 2, 2, 3, 5, 4, 2,
        3, 5, 8, 9, 6, 5, 5, 6, 6, 5,
        5, 3, 2, 3, 5, 6, 4, 7, 4, 2,
        3, 6, 5, 3, 4, 7, 3, 6, 3, 5,
        3, 4, 5, 6, 5, 3, 4, 4, 6, 5,
        7, 5, 5, 8, 5, 3, 3, 5, 4, 3
    };

    public static void main(String[] args) {
        buildGraph();

        System.out.println(
                "UG Routing graph loaded: "
                        + graph.getNodeSize()
                        + " locations, "
                        + EXPECTED_ROAD_COUNT
                        + " supplied roads, "
                        + graph.getEdgeSize()
                        + " directed edges."
        );

        new UserInterface();
    }

    public static void buildGraph() {
        validateDataset();

        graph = new Map();

        Locations[] locations =
                new Locations[LOCATION_NAMES.length];

        for (int i = 0;
             i < LOCATION_NAMES.length;
             i++) {

            locations[i] =
                    new Locations(
                            LOCATION_NAMES[i]
                    );

            graph.addVertex(locations[i]);
        }

        for (int i = 0;
             i < EXPECTED_ROAD_COUNT;
             i++) {

            Locations source =
                    graph.getNodeByName(FROM[i]);

            Locations destination =
                    graph.getNodeByName(TO[i]);

            if (source == null) {
                throw new IllegalStateException(
                        "Source location not found: "
                                + FROM[i]
                );
            }

            if (destination == null) {
                throw new IllegalStateException(
                        "Destination location not found: "
                                + TO[i]
                );
            }

            int distanceInMetres =
                    (int) Math.round(
                            DISTANCE_KM[i] * 1000.0
                    );

            Extent edge =
                    new Extent(
                            source,
                            destination,
                            distanceInMetres,
                            TRAVEL_TIME_MIN[i]
                    );

            graph.addEdge(edge);
        }
    }

    private static void validateDataset() {
        if (LOCATION_NAMES.length
                != EXPECTED_LOCATION_COUNT) {

            throw new IllegalStateException(
                    "There must be exactly 50 locations."
            );
        }

        if (FROM.length != EXPECTED_ROAD_COUNT
                || TO.length != EXPECTED_ROAD_COUNT
                || DISTANCE_KM.length
                != EXPECTED_ROAD_COUNT
                || TRAVEL_TIME_MIN.length
                != EXPECTED_ROAD_COUNT) {

            throw new IllegalStateException(
                    "Every road array must contain exactly 100 entries."
            );
        }
    }

    public static String[] getLocationNames() {
        String[] copy =
                new String[LOCATION_NAMES.length];

        for (int i = 0;
             i < LOCATION_NAMES.length;
             i++) {

            copy[i] =
                    LOCATION_NAMES[i];
        }

        return copy;
    }

    public static class RouteResult {

        private final boolean reachable;
        private final String pathText;
        private final double distanceKm;
        private final int travelTimeMin;

        public RouteResult(
                boolean reachable,
                String pathText,
                double distanceKm,
                int travelTimeMin
        ) {
            this.reachable = reachable;
            this.pathText = pathText;
            this.distanceKm = distanceKm;
            this.travelTimeMin = travelTimeMin;
        }

        public boolean isReachable() {
            return reachable;
        }

        public String getPathText() {
            return pathText;
        }

        public double getDistanceKm() {
            return distanceKm;
        }

        public int getTravelTimeMin() {
            return travelTimeMin;
        }
    }

    public static RouteResult calculateRoute(
            String startName,
            String endName
    ) {
        int startIndex =
                findLocationIndex(startName);

        int endIndex =
                findLocationIndex(endName);

        if (startIndex == -1
                || endIndex == -1) {

            return new RouteResult(
                    false,
                    "No route found.",
                    0.0,
                    0
            );
        }

        int locationCount =
                LOCATION_NAMES.length;

        double[] distances =
                new double[locationCount];

        int[] travelTimes =
                new int[locationCount];

        int[] previous =
                new int[locationCount];

        boolean[] visited =
                new boolean[locationCount];

        for (int i = 0;
             i < locationCount;
             i++) {

            distances[i] =
                    Double.POSITIVE_INFINITY;

            travelTimes[i] =
                    Integer.MAX_VALUE;

            previous[i] =
                    -1;

            visited[i] =
                    false;
        }

        distances[startIndex] =
                0.0;

        travelTimes[startIndex] =
                0;

        for (int step = 0;
             step < locationCount;
             step++) {

            int currentIndex =
                    getClosestUnvisited(
                            distances,
                            visited
                    );

            if (currentIndex == -1) {
                break;
            }

            visited[currentIndex] =
                    true;

            if (currentIndex == endIndex) {
                break;
            }

            for (int road = 0;
                 road < FROM.length;
                 road++) {

                int fromIndex =
                        findLocationIndex(
                                FROM[road]
                        );

                int toIndex =
                        findLocationIndex(
                                TO[road]
                        );

                int nextIndex =
                        -1;

                if (fromIndex == currentIndex) {
                    nextIndex =
                            toIndex;
                } else if (toIndex == currentIndex) {
                    nextIndex =
                            fromIndex;
                }

                if (nextIndex == -1
                        || visited[nextIndex]) {
                    continue;
                }

                double newDistance =
                        distances[currentIndex]
                                + DISTANCE_KM[road];

                int newTravelTime =
                        travelTimes[currentIndex]
                                + TRAVEL_TIME_MIN[road];

                if (newDistance
                        < distances[nextIndex]) {

                    distances[nextIndex] =
                            newDistance;

                    travelTimes[nextIndex] =
                            newTravelTime;

                    previous[nextIndex] =
                            currentIndex;
                }
            }
        }

        if (Double.isInfinite(
                distances[endIndex]
        )) {
            return new RouteResult(
                    false,
                    "No route found.",
                    0.0,
                    0
            );
        }

        String pathText =
                createPathText(
                        previous,
                        endIndex
                );

        return new RouteResult(
                true,
                pathText,
                distances[endIndex],
                travelTimes[endIndex]
        );
    }

    private static int getClosestUnvisited(
            double[] distances,
            boolean[] visited
    ) {
        int closestIndex =
                -1;

        double closestDistance =
                Double.POSITIVE_INFINITY;

        for (int i = 0;
             i < distances.length;
             i++) {

            if (!visited[i]
                    && distances[i]
                    < closestDistance) {

                closestDistance =
                        distances[i];

                closestIndex =
                        i;
            }
        }

        return closestIndex;
    }

    private static String createPathText(
            int[] previous,
            int endIndex
    ) {
        String[] reversePath =
                new String[LOCATION_NAMES.length];

        int pathLength =
                0;

        int currentIndex =
                endIndex;

        while (currentIndex != -1) {
            reversePath[pathLength] =
                    LOCATION_NAMES[currentIndex];

            pathLength++;

            currentIndex =
                    previous[currentIndex];
        }

        String pathText =
                "";

        for (int i = pathLength - 1;
             i >= 0;
             i--) {

            pathText =
                    pathText
                            + reversePath[i];

            if (i > 0) {
                pathText =
                        pathText
                                + " -> ";
            }
        }

        return pathText;
    }

    private static int findLocationIndex(
            String locationName
    ) {
        if (locationName == null) {
            return -1;
        }

        for (int i = 0;
             i < LOCATION_NAMES.length;
             i++) {

            if (LOCATION_NAMES[i]
                    .equalsIgnoreCase(locationName)) {

                return i;
            }
        }

        return -1;
    }
}