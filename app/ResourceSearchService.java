package app;

import java.util.Comparator;
import java.util.List;

public class ResourceSearchService {

    private final LinearSearch<ServiceResource, String> linearSearch;
    private final BinarySearch<ServiceResource, String> binarySearch;

    public ResourceSearchService() {
        this.linearSearch = LinearSearch.forComparableKey(ServiceResource::getResourceId);
        this.binarySearch = BinarySearch.forComparableKey(ServiceResource::getResourceId);
    }

    public void sortByResourceId(List<ServiceResource> resources) {
        resources.sort(Comparator.comparing(ServiceResource::getResourceId));
    }

    public ServiceResource searchLinear(List<ServiceResource> resources, String resourceId) {
        return linearSearch.findFirst(resources, resourceId);
    }

    public ServiceResource searchBinary(List<ServiceResource> resources, String resourceId) {
        return binarySearch.find(resources, resourceId);
    }
}
