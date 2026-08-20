package service;

import model.Resource;
import model.ServiceRequest;
import structures.MyPriorityQueue;
import structures.MyQueue;

public class RequestScheduler {
    private MyPriorityQueue<ServiceRequest> priorityQueue;
    private MyQueue<ServiceRequest> fifoQueue;
    private MyQueue<Resource> availableResources;

    public RequestScheduler() {
        this.priorityQueue = new MyPriorityQueue<>();
        this.fifoQueue = new MyQueue<>();
        this.availableResources = new MyQueue<>();
    }

    public void addPriorityRequest(ServiceRequest request) {
        if (request != null) {
            priorityQueue.insert(request);
        }
    }

    public void addStandardRequest(ServiceRequest request) {
        if (request != null) {
            fifoQueue.enqueue(request);
        }
    }

    public void registerResource(Resource resource) {
        if (resource != null) {
            availableResources.enqueue(resource);
        }
    }

    public String dispatchNextPriorityRequest() {
        if (priorityQueue.isEmpty()) {
            return "No pending priority requests.";
        }
        if (availableResources.isEmpty()) {
            return "No resources available for dispatch.";
        }

        ServiceRequest request = priorityQueue.poll();
        Resource resource = availableResources.dequeue();

        request.status = "Assigned";
        resource.status = "Assigned";

        return "DISPATCHED: Request [" + request.requestId + "] assigned to Resource [" + resource.resourceId + "]";
    }

    public String dispatchNextStandardRequest() {
        if (fifoQueue.isEmpty()) {
            return "No pending standard requests.";
        }
        if (availableResources.isEmpty()) {
            return "No resources available for dispatch.";
        }

        ServiceRequest request = fifoQueue.dequeue();
        Resource resource = availableResources.dequeue();

        request.status = "Assigned";
        resource.status = "Assigned";

        return "DISPATCHED (FIFO): Request [" + request.requestId + "] assigned to Resource [" + resource.resourceId + "]";
    }
// Add this to service/RequestScheduler.java
    public int availableResourceCount() {
        return availableResources.size();
    }

    public int pendingPriorityCount() {
        return priorityQueue.size();
    }


}