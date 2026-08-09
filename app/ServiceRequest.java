package app;

public class ServiceRequest {

    private final String requestId;
    private final String category;
    private final String description;
    private final String locationId;
    private final String locationName;
    private final UrgencyLevel urgency;
    private RequestStatus status;
    private final String submissionTime;

    public ServiceRequest(String requestId, String category, String description,
                           String locationId, String locationName, UrgencyLevel urgency,
                           RequestStatus status, String submissionTime) {
        this.requestId = requestId;
        this.category = category;
        this.description = description;
        this.locationId = locationId;
        this.locationName = locationName;
        this.urgency = urgency;
        this.status = status;
        this.submissionTime = submissionTime;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getLocationId() {
        return locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public UrgencyLevel getUrgency() {
        return urgency;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public String getSubmissionTime() {
        return submissionTime;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ServiceRequest)) return false;
        ServiceRequest other = (ServiceRequest) obj;
        return requestId.equals(other.requestId);
    }

    @Override
    public int hashCode() {
        return requestId.hashCode();
    }

    @Override
    public String toString() {
        return "ServiceRequest{" +
                "requestId='" + requestId + '\'' +
                ", category='" + category + '\'' +
                ", locationName='" + locationName + '\'' +
                ", urgency=" + urgency +
                ", status=" + status +
                ", submissionTime='" + submissionTime + '\'' +
                '}';
    }
}
