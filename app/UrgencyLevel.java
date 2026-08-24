package app;

public enum UrgencyLevel {
    HIGH,
    MEDIUM,
    LOW;

    public static UrgencyLevel fromString(String value) {
        return UrgencyLevel.valueOf(value.trim().toUpperCase());
    }
}
