package model;

public class Resource {
    public String resourceId;
    public String type;
    public String homeLocation;
    public int capacity;
    public String status; // "Available", "Assigned", "Busy", "Offline"

    public Resource(String resourceId, String type, String homeLocation, int capacity) {
        this.resourceId = resourceId;
        this.type = type;
        this.homeLocation = homeLocation;
        this.capacity = capacity;
        this.status = "Available";
    }
}