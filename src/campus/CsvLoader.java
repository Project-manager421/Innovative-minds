package campus;

/** Minimal CSV reader for the project's service_requests.csv file. */
public class CsvLoader {
    public static ServiceRequest[] loadServiceRequests(String path) throws Exception {
        java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(path));
        String line = reader.readLine();
        int count = 0;
        while ((line = reader.readLine()) != null) {
            if (!line.trim().isEmpty()) count++;
        }
        reader.close();

        ServiceRequest[] result = new ServiceRequest[count];
        reader = new java.io.BufferedReader(new java.io.FileReader(path));
        reader.readLine();
        int index = 0;
        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) continue;
            String[] fields = parseLine(line);
            if (fields.length >= 9) {
                result[index++] = new ServiceRequest(fields[0], fields[1], fields[2], fields[3],
                        fields[4], fields[5], fields[6], fields[7], fields[8]);
            }
        }
        reader.close();
        return result;
    }

    private static String[] parseLine(String line) {
        String[] fields = new String[20];
        int fieldCount = 0;
        StringBuilder current = new StringBuilder();
        boolean quoted = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') quoted = !quoted;
            else if (c == ',' && !quoted) {
                fields[fieldCount++] = current.toString();
                current.setLength(0);
            } else current.append(c);
        }
        fields[fieldCount++] = current.toString();

        String[] result = new String[fieldCount];
        for (int i = 0; i < fieldCount; i++) result[i] = fields[i];
        return result;
    }
}
