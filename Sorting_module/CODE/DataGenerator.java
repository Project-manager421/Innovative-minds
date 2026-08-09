import java.io.IOException;
import java.time.LocalDateTime;

/** Generates synthetic ServiceRequest datasets for testing and benchmarking. */
public class DataGenerator {

    private static final String[] CATEGORIES = {
        "IT Support", "Electrical", "Plumbing", "Cleaning",
        "Security", "Transport", "Facility Maintenance"
    };

    private static final ServiceRequest.Urgency[] URGENCIES = ServiceRequest.Urgency.values();

    /**
     * Convenience overload: loads location names from the given edges CSV
     * itself, then generates. Prefer the array-based overload below when
     * generating many datasets in one run (e.g. across benchmark sizes),
     * so the CSV is only read from disk once.
     */
    public static ServiceRequest[] generate(int n, long seed, String edgesCsvPath) throws IOException {
        String[] locationNames = LocationLoader.loadLocationNames(edgesCsvPath);
        return generate(n, seed, locationNames);
    }

    /**
     * Generates n ServiceRequests, drawing source/destination from the
     * given array of real location names (e.g. loaded once via
     * LocationLoader.loadLocationNames("innovative-minds-DATASET-edges.csv")).
     */
    public static ServiceRequest[] generate(int n, long seed, String[] locationNames) {
        if (locationNames == null || locationNames.length < 2) {
            throw new IllegalArgumentException("Need at least 2 distinct locations to pick a source and destination");
        }

        MyRandom rand = new MyRandom(seed);
        ServiceRequest[] requests = new ServiceRequest[n];
        LocalDateTime base = LocalDateTime.of(2026, 8, 1, 8, 0);

        for (int i = 0; i < n; i++) {
            String id = String.format("SR%04d", i + 1);
            String category = CATEGORIES[rand.nextInt(CATEGORIES.length)];
            ServiceRequest.Urgency urgency = URGENCIES[rand.nextInt(URGENCIES.length)];

            int sourceIndex = rand.nextInt(locationNames.length);
            int destIndex = rand.nextInt(locationNames.length - 1);
            if (destIndex >= sourceIndex) {
                destIndex++; // skip sourceIndex so destination != source
            }
            String source = locationNames[sourceIndex];
            String destination = locationNames[destIndex];

            LocalDateTime submitted = base.plusMinutes(rand.nextInt(20000));
            LocalDateTime deadline = submitted.plusMinutes(30 + rand.nextInt(600));

            requests[i] = new ServiceRequest(id, source, destination, category, urgency,
                    submitted, deadline, ServiceRequest.Status.PENDING);
        }
        return requests;
    }
}
