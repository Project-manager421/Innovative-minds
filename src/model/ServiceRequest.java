package model;

public class ServiceRequest implements Comparable<ServiceRequest> {
    public String requestId;
    public String source;
    public String category;
    public int urgency;   // 3 = High, 2 = Medium, 1 = Low
    public String status;    // "Pending", "Assigned", "Completed"

    public ServiceRequest(String id, String source, String category, int urgency) {
        this.requestId = id;
        this.source = source;
        this.category = category;
        this.urgency = urgency;
        this.status = "Pending";
    }

    @Override
    public int compareTo(ServiceRequest other) {
        // High urgency comes first
        return Integer.compare(other.urgency, this.urgency);
    }
}