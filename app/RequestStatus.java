package app;

public enum RequestStatus {
    PENDING,
    ASSIGNED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED;

    public static RequestStatus fromString(String value) {
        return RequestStatus.valueOf(value.trim().toUpperCase().replace(" ", "_"));
    }
}
