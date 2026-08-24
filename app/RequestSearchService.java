package app;

import java.util.Comparator;
import java.util.List;

public class RequestSearchService {

    private final LinearSearch<ServiceRequest, String> linearSearch;
    private final BinarySearch<ServiceRequest, String> binarySearch;

    public RequestSearchService() {
        this.linearSearch = LinearSearch.forComparableKey(ServiceRequest::getRequestId);
        this.binarySearch = BinarySearch.forComparableKey(ServiceRequest::getRequestId);
    }

    public void sortByRequestId(List<ServiceRequest> requests) {
        requests.sort(Comparator.comparing(ServiceRequest::getRequestId));
    }

    public ServiceRequest searchLinear(List<ServiceRequest> requests, String requestId) {
        return linearSearch.findFirst(requests, requestId);
    }

    public ServiceRequest searchBinary(List<ServiceRequest> requests, String requestId) {
        return binarySearch.find(requests, requestId);
    }
}
