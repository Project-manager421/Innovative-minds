package app;

public class ServiceResource {

    private final String resourceId;
    private final String name;
    private final String type;
    private final String category;
    private final String locationId;
    private final String locationName;
    private boolean available;

    public ServiceResource(String resourceId, String name, String type, String category,
                            String locationId, String locationName, boolean available) {
        this.resourceId = resourceId;
        this.name = name;
        this.type = type;
        this.category = category;
        this.locationId = locationId;
        this.locationName = locationName;
        this.available = available;
    }

    public String getResourceId() {
        return resourceId;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getCategory() {
        return category;
    }

    public String getLocationId() {
        return locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ServiceResource)) return false;
        ServiceResource other = (ServiceResource) obj;
        return resourceId.equals(other.resourceId);
    }

    @Override
    public int hashCode() {
        return resourceId.hashCode();
    }

    @Override
    public String toString() {
        return "ServiceResource{" +
                "resourceId='" + resourceId + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", locationName='" + locationName + '\'' +
                ", available=" + available +
                '}';
    }
}
