package app;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class LocationSearchService {

    private final LinearSearch<CampusLocation, String> locationByIdLinear;
    private final BinarySearch<CampusLocation, String> locationByIdBinary;
    private final LinearSearch<CampusLocation, String> locationByNameLinear;

    private final LinearSearch<ServiceRequest, String> requestByLocationLinear;
    private final BinarySearch<ServiceRequest, String> requestByLocationBinary;

    private final LinearSearch<ServiceResource, String> resourceByLocationLinear;
    private final BinarySearch<ServiceResource, String> resourceByLocationBinary;

    public LocationSearchService() {
        this.locationByIdLinear = LinearSearch.forComparableKey(CampusLocation::getLocationId);
        this.locationByIdBinary = BinarySearch.forComparableKey(CampusLocation::getLocationId);
        this.locationByNameLinear = LinearSearch.forComparableKey(CampusLocation::getName);

        this.requestByLocationLinear = LinearSearch.forComparableKey(ServiceRequest::getLocationName);
        this.requestByLocationBinary = BinarySearch.forComparableKey(ServiceRequest::getLocationName);

        this.resourceByLocationLinear = LinearSearch.forComparableKey(ServiceResource::getLocationName);
        this.resourceByLocationBinary = BinarySearch.forComparableKey(ServiceResource::getLocationName);
    }

    public void sortLocationsById(List<CampusLocation> locations) {
        locations.sort(Comparator.comparing(CampusLocation::getLocationId));
    }

    public CampusLocation findLocationByIdLinear(List<CampusLocation> locations, String locationId) {
        return locationByIdLinear.findFirst(locations, locationId);
    }

    public CampusLocation findLocationByIdBinary(List<CampusLocation> locations, String locationId) {
        return locationByIdBinary.find(locations, locationId);
    }

    public CampusLocation findLocationByName(List<CampusLocation> locations, String name) {
        return locationByNameLinear.findFirst(locations, name);
    }

    public void sortRequestsByLocation(List<ServiceRequest> requests) {
        requests.sort(Comparator.comparing(ServiceRequest::getLocationName));
    }

    public List<ServiceRequest> findRequestsAtLocationLinear(List<ServiceRequest> requests, String locationName) {
        return requestByLocationLinear.findAll(requests, locationName);
    }

    public List<ServiceRequest> findRequestsAtLocationBinary(List<ServiceRequest> sortedRequests, String locationName) {
        return expandAroundMatch(sortedRequests, requestByLocationBinary.findIndex(sortedRequests, locationName),
                r -> r.getLocationName().equals(locationName));
    }

    public void sortResourcesByLocation(List<ServiceResource> resources) {
        resources.sort(Comparator.comparing(ServiceResource::getLocationName));
    }

    public List<ServiceResource> findResourcesAtLocationLinear(List<ServiceResource> resources, String locationName) {
        return resourceByLocationLinear.findAll(resources, locationName);
    }

    public List<ServiceResource> findResourcesAtLocationBinary(List<ServiceResource> sortedResources, String locationName) {
        return expandAroundMatch(sortedResources, resourceByLocationBinary.findIndex(sortedResources, locationName),
                r -> r.getLocationName().equals(locationName));
    }

    private <T> List<T> expandAroundMatch(List<T> sortedData, int anchor, Predicate<T> matches) {
        List<T> results = new ArrayList<>();
        if (anchor == -1) {
            return results;
        }
        int left = anchor;
        while (left >= 0 && matches.test(sortedData.get(left))) {
            left--;
        }
        int right = anchor;
        while (right < sortedData.size() && matches.test(sortedData.get(right))) {
            right++;
        }
        for (int i = left + 1; i < right; i++) {
            results.add(sortedData.get(i));
        }
        return results;
    }
}
