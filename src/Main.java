import model.Resource;
import model.ServiceRequest;
import service.RequestScheduler; 
import service.UndoManager;
import structures.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("   UG SMART CAMPUS OPTIMIZER - GROUP E DEMO       ");
        System.out.println("==================================================");

        // ----------------------------------------------------
        // 1. Test Low-Level Data Structures
        // ----------------------------------------------------
        System.out.println("\n--- [1] TESTING CUSTOM STRUCTURES ---");
        
        // Deque Test
        MyDeque<String> deque = new MyDeque<>();
        deque.addLast("Standard Route Patrol");
        deque.addFirst("EMERGENCY: Main Gate Gatehouse");
        System.out.println("Deque Front (Priority Dispatch): " + deque.removeFirst());

        // Circular Queue Test
        MyCircularQueue<String> ringBuffer = new MyCircularQueue<>(2);
        ringBuffer.enqueue("Log Entry 1");
        ringBuffer.enqueue("Log Entry 2");
        System.out.println("Circular Queue Full? " + ringBuffer.isFull());

        // ----------------------------------------------------
        // 2. Test Request Scheduler (Priority Queue + FIFO)
        // ----------------------------------------------------
        System.out.println("\n--- [2] TESTING REQUEST SCHEDULER ---");
        RequestScheduler scheduler = new RequestScheduler();

        // Register available resources
        Resource res1 = new Resource("RES001", "Maintenance Van", "CS Dept", 2);
        Resource res2 = new Resource("RES002", "Ambulance", "UG Hospital", 1);
        scheduler.registerResource(res1);
        scheduler.registerResource(res2);

        // Add incoming service requests
        ServiceRequest reqLow = new ServiceRequest("SR001", "Balme Library", "Cleaning", 1);
        ServiceRequest reqHigh = new ServiceRequest("SR002", "UG Hospital", "Medical Emergency", 3);
        
        scheduler.addPriorityRequest(reqLow);
        scheduler.addPriorityRequest(reqHigh);

        // High urgency request (SR002) should be dispatched first
        System.out.println(scheduler.dispatchNextPriorityRequest());

        // ----------------------------------------------------
        // 3. Test Undo Manager (Stack Operations)
        // ----------------------------------------------------
        System.out.println("\n--- [3] TESTING UNDO MANAGER ---");
        UndoManager undoManager = new UndoManager();

        ServiceRequest testReq = new ServiceRequest("SR099", "Legon Hall", "Plumbing", 2);
        Resource testRes = new Resource("RES010", "Plumbing Team", "Maintenance Workshop", 3);

        System.out.println("Initial Request Status: " + testReq.status);

        // Perform and record assignment
        undoManager.recordAssignment(testReq, testRes);
        System.out.println("Post-Assignment Request Status: " + testReq.status);
        System.out.println("Post-Assignment Resource Status: " + testRes.status);

        // Rollback the assignment
        System.out.println(undoManager.undo());
        System.out.println("Status After Undo: " + testReq.status);
        System.out.println("Resource Status After Undo: " + testRes.status);

        System.out.println("\n==================================================");
        System.out.println("   ALL GROUP E VERIFICATIONS EXECUTED SUCCESSFULLY ");
        System.out.println("==================================================");
    }
}