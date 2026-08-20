package service;

import model.Resource;
import model.ServiceRequest;

public class Action {
    
    // Enum representing the type of operation performed
    public enum ActionType {
        STATUS_CHANGE,
        RESOURCE_ASSIGNMENT
    }

    public ActionType type;
    public ServiceRequest request;
    public Resource resource;
    public String previousStatus;
    public String newStatus;

    // Constructor 1: Tracks request status changes (e.g., Pending -> In Progress)
    public Action(ServiceRequest request, String previousStatus, String newStatus) {
        this.type = ActionType.STATUS_CHANGE;
        this.request = request;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
    }

    // Constructor 2: Tracks resource assignments (e.g., Request assigned to Van)
    public Action(ServiceRequest request, Resource resource) {
        this.type = ActionType.RESOURCE_ASSIGNMENT;
        this.request = request;
        this.resource = resource;
    }
}