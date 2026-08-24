package app;

public class CampusLocation {

    private final String locationId;
    private final String name;

    public CampusLocation(String locationId, String name) {
        this.locationId = locationId;
        this.name = name;
    }

    public String getLocationId() {
        return locationId;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof CampusLocation)) return false;
        CampusLocation other = (CampusLocation) obj;
        return locationId.equals(other.locationId);
    }

    @Override
    public int hashCode() {
        return locationId.hashCode();
    }

    @Override
    public String toString() {
        return locationId + " - " + name;
    }
}
