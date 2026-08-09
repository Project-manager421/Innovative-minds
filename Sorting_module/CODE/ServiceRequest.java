import java.time.LocalDateTime;

/**
 * Represents a single service request submitted on the University of Ghana
 * Smart Campus Service Operations Optimizer.
 *
 * Fields mirror the "service_requests" entity in the joint project brief
 * (Section 4 - Minimum dataset and database requirements):
 *   requestId, source, destination, category, urgency, timeSubmitted,
 *   deadline, status.
 *
 * source / destination hold location IDs (e.g. "L007") from the team's
 * locations dataset (innovative_minds DATASET), e.g. a request raised at
 * Computer Science Department to be actioned at the Maintenance Workshop.
 */
public class ServiceRequest {

    public enum Urgency {
        HIGH(3), MEDIUM(2), LOW(1);

        private final int weight;

        Urgency(int weight) {
            this.weight = weight;
        }

        /** Higher weight = more urgent. Used so "High" sorts ahead of "Low". */
        public int getWeight() {
            return weight;
        }
    }

    public enum Status {
        PENDING, ASSIGNED, IN_PROGRESS, COMPLETED, CANCELLED
    }

    private final String requestId;
    private final String source;
    private final String destination;
    private final String category;
    private final Urgency urgency;
    private final LocalDateTime submissionTime;
    private final LocalDateTime deadline;
    private Status status;

    public ServiceRequest(String requestId, String source, String destination, String category,
                          Urgency urgency, LocalDateTime submissionTime, LocalDateTime deadline,
                          Status status) {
        this.requestId = requestId;
        this.source = source;
        this.destination = destination;
        this.category = category;
        this.urgency = urgency;
        this.submissionTime = submissionTime;
        this.deadline = deadline;
        this.status = status;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public String getCategory() {
        return category;
    }

    public Urgency getUrgency() {
        return urgency;
    }

    public LocalDateTime getSubmissionTime() {
        return submissionTime;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("%s | %s->%s | %-20s | %-6s | submitted=%s | deadline=%s | %s",
                requestId, source, destination, category, urgency, submissionTime, deadline, status);
    }
}
