package campus;

/** Represents one maintenance/service request. */
public class ServiceRequest {
    private final String requestId;
    private final String category;
    private final String description;
    private final String location;
    private final String urgency;
    private final String status;
    private final String submittedTime;
    private final String deadline;
    private final String assignedResource;

    public ServiceRequest(String requestId, String category, String description,
                          String location, String urgency, String status,
                          String submittedTime, String deadline, String assignedResource) {
        this.requestId = requestId;
        this.category = category;
        this.description = description;
        this.location = location;
        this.urgency = urgency;
        this.status = status;
        this.submittedTime = submittedTime;
        this.deadline = deadline;
        this.assignedResource = assignedResource;
    }

    public String getRequestId() { return requestId; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public String getUrgency() { return urgency; }
    public String getStatus() { return status; }
    public String getSubmittedTime() { return submittedTime; }
    public String getDeadline() { return deadline; }
    public String getAssignedResource() { return assignedResource; }

    public String toString() {
        return requestId + " | " + category + " | " + location
                + " | urgency=" + urgency + " | status=" + status;
    }
}
