package data;


import graph.Graph;
import model.Vertex;
import model.Edge;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CSVLoader {

    // Loads all locations from locations.csv
    public static void loadLocations(
            String filePath,
            Graph graph) throws IOException {

        BufferedReader reader =
                new BufferedReader(
                        new FileReader(filePath)
                );

        // Skip CSV header
        reader.readLine();

        String line;
        int count = 0;

        while ((line = reader.readLine()) != null) {

            if (line.trim().isEmpty()) {
                continue;
            }

            String[] data = line.split(",");

            String id = data[0].trim();
            String name = data[1].trim();

            Vertex vertex =
                    new Vertex(id, name);

            graph.addVertex(vertex);

            count++;
        }

        reader.close();

        System.out.println(
                "Locations loaded: " + count
        );
    }


    // Loads all roads from roads.csv
    public static void loadRoads(
            String filePath,
            Graph graph) throws IOException {

        BufferedReader reader =
                new BufferedReader(
                        new FileReader(filePath)
                );

        // Skip CSV header
        reader.readLine();

        String line;
        int count = 0;

        while ((line = reader.readLine()) != null) {

            if (line.trim().isEmpty()) {
                continue;
            }

            String[] data = line.split(",");

            String edgeId = data[0].trim();
            String fromName = data[1].trim();
            String toName = data[2].trim();

            double distance =
                    Double.parseDouble(data[3].trim());

            int travelTime =
                    Integer.parseInt(data[4].trim());

            Vertex from =
                    graph.findVertexByName(fromName);

            Vertex to =
                    graph.findVertexByName(toName);

            if (from == null || to == null) {

                System.out.println(
                        "Warning: " + edgeId
                        + " has a missing location."
                );

                continue;
            }

            Edge edge =
                    new Edge(
                            edgeId,
                            from,
                            to,
                            distance,
                            travelTime
                    );

            graph.addEdge(edge);

            count++;
        }

        reader.close();

        System.out.println(
                "Roads loaded: " + count
        );
    }
}