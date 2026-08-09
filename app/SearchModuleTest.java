package app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SearchModuleTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) throws IOException {
        testLinearSearchFindsElementRegardlessOfOrder();
        testLinearSearchReturnsNullWhenMissing();
        testLinearSearchFindAllReturnsEveryMatch();
        testLinearSearchHandlesEmptyAndNullInputs();

        testBinarySearchFindsElementInSortedList();
        testBinarySearchReturnsNullWhenMissing();
        testBinarySearchDetectsUnsortedList();
        testBinarySearchAgreesWithLinearSearch();

        testRequestSearchServiceLinearAndBinary();
        testResourceSearchServiceLinearAndBinary();
        testLocationSearchServiceById();
        testLocationSearchServiceRequestsAtLocation();

        testDatasetLoaderLoadsRealFiles();

        testPerformanceComparatorProducesResults();

        System.out.println();
        System.out.println(passed + " passed, " + failed + " failed");
        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void check(String testName, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("PASS - " + testName);
        } else {
            failed++;
            System.out.println("FAIL - " + testName);
        }
    }

    private static List<ServiceRequest> sampleRequests() {
        List<ServiceRequest> requests = new ArrayList<>();
        requests.add(new ServiceRequest("SR003", "Plumbing", "Broken pipe", "L030", "Akuafo Hall",
                UrgencyLevel.LOW, RequestStatus.PENDING, "2026-01-10 09:00"));
        requests.add(new ServiceRequest("SR001", "Electrical", "Power outage", "L007", "Computer Science Department",
                UrgencyLevel.HIGH, RequestStatus.PENDING, "2026-01-06 08:00"));
        requests.add(new ServiceRequest("SR004", "IT Support", "Wi-Fi failure", "L006", "Balme Library",
                UrgencyLevel.MEDIUM, RequestStatus.ASSIGNED, "2026-01-11 10:00"));
        requests.add(new ServiceRequest("SR002", "IT Support", "Printer malfunction", "L006", "Balme Library",
                UrgencyLevel.HIGH, RequestStatus.IN_PROGRESS, "2026-01-07 12:00"));
        return requests;
    }

    private static void testLinearSearchFindsElementRegardlessOfOrder() {
        LinearSearch<ServiceRequest, String> search = LinearSearch.forComparableKey(ServiceRequest::getRequestId);
        ServiceRequest found = search.findFirst(sampleRequests(), "SR004");
        check("linearSearch.findsElementRegardlessOfOrder", found != null && found.getRequestId().equals("SR004"));
    }

    private static void testLinearSearchReturnsNullWhenMissing() {
        LinearSearch<ServiceRequest, String> search = LinearSearch.forComparableKey(ServiceRequest::getRequestId);
        check("linearSearch.returnsNullWhenMissing", search.findFirst(sampleRequests(), "SR999") == null);
    }

    private static void testLinearSearchFindAllReturnsEveryMatch() {
        LinearSearch<ServiceRequest, String> search = LinearSearch.forComparableKey(ServiceRequest::getLocationName);
        List<ServiceRequest> matches = search.findAll(sampleRequests(), "Balme Library");
        check("linearSearch.findAllReturnsEveryMatch", matches.size() == 2);
    }

    private static void testLinearSearchHandlesEmptyAndNullInputs() {
        LinearSearch<ServiceRequest, String> search = LinearSearch.forComparableKey(ServiceRequest::getRequestId);
        boolean emptyOk = search.findFirst(new ArrayList<>(), "SR001") == null;
        boolean nullOk = search.findIndex(null, "SR001") == -1;
        check("linearSearch.handlesEmptyAndNullInputs", emptyOk && nullOk);
    }

    private static List<ServiceRequest> sortedSampleRequests() {
        List<ServiceRequest> requests = sampleRequests();
        requests.sort(Comparator.comparing(ServiceRequest::getRequestId));
        return requests;
    }

    private static void testBinarySearchFindsElementInSortedList() {
        BinarySearch<ServiceRequest, String> search = BinarySearch.forComparableKey(ServiceRequest::getRequestId);
        ServiceRequest found = search.find(sortedSampleRequests(), "SR003");
        check("binarySearch.findsElementInSortedList", found != null && found.getRequestId().equals("SR003"));
    }

    private static void testBinarySearchReturnsNullWhenMissing() {
        BinarySearch<ServiceRequest, String> search = BinarySearch.forComparableKey(ServiceRequest::getRequestId);
        check("binarySearch.returnsNullWhenMissing", search.find(sortedSampleRequests(), "SR999") == null);
    }

    private static void testBinarySearchDetectsUnsortedList() {
        BinarySearch<ServiceRequest, String> search = BinarySearch.forComparableKey(ServiceRequest::getRequestId);
        check("binarySearch.detectsUnsortedList", !search.isSorted(sampleRequests()));
        check("binarySearch.confirmsSortedList", search.isSorted(sortedSampleRequests()));
    }

    private static void testBinarySearchAgreesWithLinearSearch() {
        List<ServiceRequest> sorted = sortedSampleRequests();
        LinearSearch<ServiceRequest, String> linear = LinearSearch.forComparableKey(ServiceRequest::getRequestId);
        BinarySearch<ServiceRequest, String> binary = BinarySearch.forComparableKey(ServiceRequest::getRequestId);

        boolean allMatch = true;
        for (ServiceRequest request : sorted) {
            ServiceRequest a = linear.findFirst(sorted, request.getRequestId());
            ServiceRequest b = binary.find(sorted, request.getRequestId());
            if (a != b) {
                allMatch = false;
                break;
            }
        }
        check("binarySearch.agreesWithLinearSearch", allMatch);
    }

    private static void testRequestSearchServiceLinearAndBinary() {
        List<ServiceRequest> requests = sampleRequests();
        RequestSearchService service = new RequestSearchService();

        ServiceRequest linearResult = service.searchLinear(requests, "SR002");
        service.sortByRequestId(requests);
        ServiceRequest binaryResult = service.searchBinary(requests, "SR002");

        check("requestSearchService.linearAndBinaryAgree",
                linearResult != null && binaryResult != null
                        && linearResult.getRequestId().equals(binaryResult.getRequestId()));
        check("requestSearchService.unknownIdReturnsNull", service.searchBinary(requests, "SR404") == null);
    }

    private static void testResourceSearchServiceLinearAndBinary() {
        List<ServiceResource> resources = new ArrayList<>();
        resources.add(new ServiceResource("RES014", "IT Technician John", "IT Technician", "IT Support",
                "L007", "Computer Science Department", true));
        resources.add(new ServiceResource("RES002", "Ambulance", "Ambulance", "Medical",
                "L024", "UG Hospital", true));
        resources.add(new ServiceResource("RES009", "Plumber Kojo", "Plumber", "Maintenance",
                "L030", "Akuafo Hall", false));

        ResourceSearchService service = new ResourceSearchService();
        ServiceResource linearResult = service.searchLinear(resources, "RES009");
        service.sortByResourceId(resources);
        ServiceResource binaryResult = service.searchBinary(resources, "RES009");

        check("resourceSearchService.linearAndBinaryAgree",
                linearResult != null && binaryResult != null
                        && linearResult.getResourceId().equals(binaryResult.getResourceId()));
        check("resourceSearchService.unavailableFlagPreserved", !linearResult.isAvailable());
    }

    private static void testLocationSearchServiceById() {
        List<CampusLocation> locations = new ArrayList<>();
        locations.add(new CampusLocation("L006", "Balme Library"));
        locations.add(new CampusLocation("L001", "Main Gate"));
        locations.add(new CampusLocation("L007", "Computer Science Department"));

        LocationSearchService service = new LocationSearchService();
        CampusLocation byId = service.findLocationByIdLinear(locations, "L007");
        CampusLocation byName = service.findLocationByName(locations, "Main Gate");

        check("locationSearchService.findsById", byId != null && byId.getName().equals("Computer Science Department"));
        check("locationSearchService.findsByName", byName != null && byName.getLocationId().equals("L001"));
    }

    private static void testLocationSearchServiceRequestsAtLocation() {
        List<ServiceRequest> requests = sampleRequests();
        LocationSearchService service = new LocationSearchService();

        List<ServiceRequest> linearResults = service.findRequestsAtLocationLinear(requests, "Balme Library");
        service.sortRequestsByLocation(requests);
        List<ServiceRequest> binaryResults = service.findRequestsAtLocationBinary(requests, "Balme Library");

        check("locationSearchService.linearFindsAllAtLocation", linearResults.size() == 2);
        check("locationSearchService.binaryFindsAllAtLocation", binaryResults.size() == 2);
    }

    private static void testDatasetLoaderLoadsRealFiles() throws IOException {
        List<CampusLocation> locations = SearchDatasetLoader.loadLocations("dataset/locations.csv");
        check("datasetLoader.loadsFiftyLocations", locations.size() == 50);

        List<ServiceRequest> requests = SearchDatasetLoader.loadServiceRequests("dataset/service_requests.csv");
        check("datasetLoader.loadsServiceRequests", requests.size() > 0);

        List<ServiceResource> resources = SearchDatasetLoader.loadResources("dataset/resources.csv");
        check("datasetLoader.loadsResources", resources.size() > 0);
    }

    private static void testPerformanceComparatorProducesResults() {
        SearchPerformanceComparator comparator = new SearchPerformanceComparator();
        List<SearchPerformanceComparator.Result> results = comparator.runComparison(100, 1000, 10000);
        check("performanceComparator.producesOneResultPerSize", results.size() == 3);

        boolean allNonNegative = true;
        for (SearchPerformanceComparator.Result r : results) {
            if (r.linearNanos < 0 || r.binaryNanos < 0) {
                allNonNegative = false;
            }
        }
        check("performanceComparator.timingsNonNegative", allNonNegative);
    }
}
