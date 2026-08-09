import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Group B - Sorting Module
 *
 * Reads the team's edges CSV (the same file Group D's graph module works
 * from - columns: Edge ID,From,To,Distance (km),Travel Time (min)) and
 * extracts the distinct location names that appear as a "From" or "To"
 * endpoint. DataGenerator uses the result so service requests get real
 * source/destination names instead of synthetic IDs.
 *
 * File reading is done with java.io, which Section 8.ii of the project
 * brief explicitly allows. The dedup itself does not use any java.util
 * collection (no HashSet/ArrayList) - a fixed-capacity array with manual
 * linear-search dedup does that job, consistent with the rest of the
 * sorting module.
 */
public class LocationLoader {

    private static final int MAX_LOCATIONS = 200; // generous cap; dataset has ~50

    /**
     * @param csvPath path to the edges CSV
     * @return array of distinct location names found in the From/To columns
     */
    public static String[] loadLocationNames(String csvPath) throws IOException {
        String[] found = new String[MAX_LOCATIONS];
        int count = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(csvPath))) {
            String line = reader.readLine(); // header row - skip it
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] fields = line.split(",");
                if (fields.length < 3) {
                    continue; // malformed/short row - skip
                }

                String from = fields[1].trim();
                String to = fields[2].trim();

                count = addIfMissing(found, count, from);
                count = addIfMissing(found, count, to);
            }
        }

        if (count == 0) {
            throw new IOException("No locations found in " + csvPath + " - check the file path/format");
        }

        String[] result = new String[count];
        for (int i = 0; i < count; i++) {
            result[i] = found[i];
        }
        return result;
    }

    /** Linear-search dedup: appends name only if not already present. */
    private static int addIfMissing(String[] found, int count, String name) {
        for (int i = 0; i < count; i++) {
            if (found[i].equals(name)) {
                return count; // already present, nothing to add
            }
        }
        if (count >= found.length) {
            throw new IllegalStateException(
                "Exceeded MAX_LOCATIONS=" + found.length + " - raise the cap in LocationLoader");
        }
        found[count] = name;
        return count + 1;
    }
}
