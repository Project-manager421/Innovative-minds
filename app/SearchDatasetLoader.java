package app;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class SearchDatasetLoader {

    private SearchDatasetLoader() {
    }

    public static List<CampusLocation> loadLocations(String filePath) throws IOException {
        List<CampusLocation> locations = new ArrayList<>();
        for (String[] row : readCsv(filePath)) {
            locations.add(new CampusLocation(row[0], row[1]));
        }
        return locations;
    }

    public static List<ServiceRequest> loadServiceRequests(String filePath) throws IOException {
        List<ServiceRequest> requests = new ArrayList<>();
        for (String[] row : readCsv(filePath)) {
            requests.add(new ServiceRequest(
                    row[0],
                    row[1],
                    row[2],
                    row[3],
                    row[4],
                    UrgencyLevel.fromString(row[5]),
                    RequestStatus.fromString(row[6]),
                    row[7]));
        }
        return requests;
    }

    public static List<ServiceResource> loadResources(String filePath) throws IOException {
        List<ServiceResource> resources = new ArrayList<>();
        for (String[] row : readCsv(filePath)) {
            resources.add(new ServiceResource(
                    row[0],
                    row[1],
                    row[2],
                    row[3],
                    row[4],
                    row[5],
                    Boolean.parseBoolean(row[6])));
        }
        return resources;
    }

    private static List<String[]> readCsv(String filePath) throws IOException {
        List<String[]> rows = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    continue;
                }
                rows.add(parseCsvLine(line));
            }
        }
        return rows;
    }

    private static String[] parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                fields.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        fields.add(current.toString());
        return fields.toArray(new String[0]);
    }
}
