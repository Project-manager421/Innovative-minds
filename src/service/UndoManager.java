package service;

import model.Resource;
import model.ServiceRequest;
import structures.MyStack;

public class UndoManager {
    private MyStack<Action> historyStack;

    public UndoManager() {
        this.historyStack = new MyStack<>();
    }

    // Record a request status change
    public void recordStatusChange(ServiceRequest request, String previousStatus, String newStatus) {
        if (request != null) {
            request.status = newStatus;
            historyStack.push(new Action(request, previousStatus, newStatus));
        }
    }

    // Record a resource assignment
    public void recordAssignment(ServiceRequest request, Resource resource) {
        if (request != null && resource != null) {
            request.status = "Assigned";
            resource.status = "Assigned";
            historyStack.push(new Action(request, resource));
        }
    }

    // Undo the last performed action
    public String undo() {
        if (historyStack.isEmpty()) {
            return "Nothing to undo.";
        }

        Action lastAction = historyStack.pop();

        if (lastAction.type == Action.ActionType.STATUS_CHANGE) {
            String rolledBackStatus = lastAction.previousStatus;
            lastAction.request.status = rolledBackStatus;
            return "UNDO SUCCESS: Request [" + lastAction.request.requestId 
                    + "] status reverted back to '" + rolledBackStatus + "'.";
        } else if (lastAction.type == Action.ActionType.RESOURCE_ASSIGNMENT) {
            lastAction.request.status = "Pending";
            if (lastAction.resource != null) {
                lastAction.resource.status = "Available";
            }
            return "UNDO SUCCESS: Assignment between Request [" + lastAction.request.requestId 
                    + "] and Resource [" + (lastAction.resource != null ? lastAction.resource.resourceId : "N/A") 
                    + "] reverted.";
        }

        return "Undo operation completed.";
    }

    public boolean canUndo() {
        return !historyStack.isEmpty();
    }

public int historySize() {
        return historyStack.size();
    }

}