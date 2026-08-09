package app;

import java.io.IOException;
import java.util.List;

public class SearchDemo {

    public static void main(String[] args) throws IOException {
        List<CampusLocation> locations = SearchDatasetLoader.loadLocations("dataset/locations.csv");
        List<ServiceRequest> requests = SearchDatasetLoader.loadServiceRequests("dataset/service_requests.csv");
        List<ServiceResource> resources = SearchDatasetLoader.loadResources("dataset/resources.csv");

        System.out.println("Loaded " + locations.size() + " locations");
        System.out.println("Loaded " + requests.size() + " service requests");
        System.out.println("Loaded " + resources.size() + " resources");

        RequestSearchService requestSearch = new RequestSearchService();
        System.out.println("\nLinear search SR150 -> " + requestSearch.searchLinear(requests, "SR150"));
        requestSearch.sortByRequestId(requests);
        System.out.println("Binary search SR150 -> " + requestSearch.searchBinary(requests, "SR150"));

        ResourceSearchService resourceSearch = new ResourceSearchService();
        System.out.println("\nLinear search RES010 -> " + resourceSearch.searchLinear(resources, "RES010"));
        resourceSearch.sortByResourceId(resources);
        System.out.println("Binary search RES010 -> " + resourceSearch.searchBinary(resources, "RES010"));

        LocationSearchService locationSearch = new LocationSearchService();
        System.out.println("\nLocation L006 -> " + locationSearch.findLocationByIdLinear(locations, "L006"));
        System.out.println("Location by name \"Balme Library\" -> "
                + locationSearch.findLocationByName(locations, "Balme Library"));

        System.out.println("\nRequests at Balme Library (linear) -> "
                + locationSearch.findRequestsAtLocationLinear(requests, "Balme Library").size());
        locationSearch.sortRequestsByLocation(requests);
        System.out.println("Requests at Balme Library (binary) -> "
                + locationSearch.findRequestsAtLocationBinary(requests, "Balme Library").size());

        System.out.println("\nPerformance comparison:");
        SearchPerformanceComparator comparator = new SearchPerformanceComparator();
        for (SearchPerformanceComparator.Result r : comparator.runComparison(300, 1_000, 10_000, 100_000)) {
            System.out.println(r);
        }
    }
}
