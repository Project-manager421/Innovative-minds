import model.Resource;
import model.ServiceRequest;
import service.RequestScheduler;
import service.UndoManager;
import structures.*;

/**
 * Self-contained test harness for Group E's data structures
 * (Queue, Circular Queue, Deque, Stack, Priority Queue,
 *  Request Scheduling, Undo).
 *
 * No JUnit / external dependencies required - just run:
 *   javac -d out src/model/*.java src/service/*.java src/structures/*.java src/StructureTester.java
 *   java -cp out StructureTester
 *
 * Each section is independent, so you can comment out calls in main()
 * to test one structure at a time during development, then run the
 * whole file for the full regression pass before your defense.
 */
public class StructureTester {

    private static int passCount = 0;
    private static int failCount = 0;

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("   GROUP E - CUSTOM DATA STRUCTURE TEST SUITE");
        System.out.println("==================================================");

        testMyQueue();
        testMyCircularQueue();
        testMyDeque();
        testMyStack();
        testMyPriorityQueue();
        testRequestScheduler();
        testUndoManager();

        System.out.println("\n==================================================");
        System.out.println("   RESULTS: " + passCount + " passed, " + failCount + " failed");
        System.out.println("==================================================");
        if (failCount > 0) {
            System.exit(1); // non-zero exit code is useful if you wire this into CI
        }
    }

    // ============================================================
    // 1) MyQueue (unbounded FIFO)
    // ============================================================
    private static void testMyQueue() {
        System.out.println("\n--- [1] MyQueue (FIFO) ---");
        MyQueue<String> q = new MyQueue<>();

        check("New queue isEmpty()", q.isEmpty());

        q.enqueue("SR001");
        q.enqueue("SR002");
        q.enqueue("SR003");
        check("size() after 3 enqueues", q.size() == 3);
        check("peek() shows front without removing", q.peek().equals("SR001"));
        check("size() unchanged after peek()", q.size() == 3);

        check("dequeue() #1 returns SR001 (FIFO order)", q.dequeue().equals("SR001"));
        check("dequeue() #2 returns SR002 (FIFO order)", q.dequeue().equals("SR002"));
        check("size() after 2 dequeues", q.size() == 1);

        // Resize test: initial backing array is 20, push well past that
        MyQueue<Integer> big = new MyQueue<>();
        for (int i = 1; i <= 50; i++) big.enqueue(i);
        boolean orderOk = true;
        for (int i = 1; i <= 50; i++) {
            if (big.dequeue() != i) { orderOk = false; break; }
        }
        check("resize() preserves FIFO order across 50 items (>20 capacity)", orderOk);
        check("queue empty after draining all 50 items", big.isEmpty());

        check("dequeue() on empty queue throws IllegalStateException",
                throwsException(() -> new MyQueue<String>().dequeue(), IllegalStateException.class));
        check("peek() on empty queue throws IllegalStateException",
                throwsException(() -> new MyQueue<String>().peek(), IllegalStateException.class));
    }

    // ============================================================
    // 2) MyCircularQueue (fixed-capacity FIFO)
    // ============================================================
    private static void testMyCircularQueue() {
        System.out.println("\n--- [2] MyCircularQueue (fixed capacity) ---");
        MyCircularQueue<String> cq = new MyCircularQueue<>(3);

        check("New circular queue isEmpty()", cq.isEmpty());
        check("enqueue #1 succeeds", cq.enqueue("A"));
        check("enqueue #2 succeeds", cq.enqueue("B"));
        check("enqueue #3 succeeds", cq.enqueue("C"));
        check("isFull() true at capacity", cq.isFull());
        check("enqueue #4 rejected (returns false) when full", !cq.enqueue("D"));

        check("peek() shows front item A", cq.peek().equals("A"));
        check("dequeue() returns A", cq.dequeue().equals("A"));
        check("isFull() false after one dequeue", !cq.isFull());
        check("enqueue after dequeue succeeds (wraparound)", cq.enqueue("D"));
        check("contents wrap correctly: [B, C, D]", cq.toString().equals("[B, C, D]"));

       // Stress the wraparound pointer across many cycles
        MyCircularQueue<Integer> ring = new MyCircularQueue<>(4);
        boolean wrapOk = true;
        int nextExpected = 1;
        for (int round = 0; round < 20; round++) {
            ring.enqueue(round);
            if (ring.isFull()) {
                if (ring.dequeue() != nextExpected - 1 && round != nextExpected - 1) {
                    wrapOk = false; // <-- We now use the variable to flag a failure
                }
                nextExpected++;
            }
        }
        // <-- We now pass wrapOk into the check instead of hardcoding 'true'
        check("circular queue survives 20 enqueue/dequeue cycles and maintains order", wrapOk);

        check("dequeue() on empty circular queue throws IllegalStateException",
                throwsException(() -> new MyCircularQueue<String>(2).dequeue(), IllegalStateException.class));
        check("constructor with capacity 0 throws IllegalArgumentException",
                throwsException(() -> new MyCircularQueue<String>(0), IllegalArgumentException.class));
        check("enqueue(null) throws IllegalArgumentException",
                throwsException(() -> new MyCircularQueue<String>(2).enqueue(null), IllegalArgumentException.class));
    }

    // ============================================================
    // 3) MyDeque (double-ended queue)
    // ============================================================
    private static void testMyDeque() {
        System.out.println("\n--- [3] MyDeque (double-ended) ---");
        MyDeque<String> dq = new MyDeque<>();

        check("New deque isEmpty()", dq.isEmpty());

        dq.addLast("Routine Patrol");          // [Routine Patrol]
        dq.addFirst("EMERGENCY: Main Gate");    // [EMERGENCY, Routine Patrol]
        dq.addLast("Standard Cleaning");        // [EMERGENCY, Routine Patrol, Standard Cleaning]

        check("size() after 3 inserts", dq.size() == 3);
        check("peekFirst() shows emergency job pushed to front", dq.peekFirst().equals("EMERGENCY: Main Gate"));
        check("peekLast() shows most recently appended job", dq.peekLast().equals("Standard Cleaning"));

        check("removeFirst() pops the emergency job first", dq.removeFirst().equals("EMERGENCY: Main Gate"));
        check("removeLast() pops the cleaning job", dq.removeLast().equals("Standard Cleaning"));
        check("only Routine Patrol left", dq.peekFirst().equals("Routine Patrol") && dq.size() == 1);

        dq.removeFirst();
        check("deque empty after removing last element", dq.isEmpty());

        check("removeFirst() on empty deque throws IllegalStateException",
                throwsException(() -> new MyDeque<String>().removeFirst(), IllegalStateException.class));
        check("removeLast() on empty deque throws IllegalStateException",
                throwsException(() -> new MyDeque<String>().removeLast(), IllegalStateException.class));
        check("addFirst(null) throws IllegalArgumentException",
                throwsException(() -> new MyDeque<String>().addFirst(null), IllegalArgumentException.class));
    }

    // ============================================================
    // 4) MyStack (LIFO)
    // ============================================================
    private static void testMyStack() {
        System.out.println("\n--- [4] MyStack (LIFO) ---");
        MyStack<String> st = new MyStack<>();

        check("New stack isEmpty()", st.isEmpty());

        st.push("Status: Pending");
        st.push("Status: Assigned");
        st.push("Status: In Progress");

        check("size() after 3 pushes", st.size() == 3);
        check("peek() shows most recent push without removing", st.peek().equals("Status: In Progress"));
        check("pop() #1 returns last pushed item (LIFO)", st.pop().equals("Status: In Progress"));
        check("pop() #2 returns second-to-last item", st.pop().equals("Status: Assigned"));
        check("size() after 2 pops", st.size() == 1);

        // Resize test: initial backing array is 20
        MyStack<Integer> big = new MyStack<>();
        for (int i = 1; i <= 50; i++) big.push(i);
        boolean orderOk = true;
        for (int i = 50; i >= 1; i--) {
            if (big.pop() != i) { orderOk = false; break; }
        }
        check("resize() preserves LIFO order across 50 items (>20 capacity)", orderOk);

        check("pop() on empty stack throws IllegalStateException",
                throwsException(() -> new MyStack<String>().pop(), IllegalStateException.class));
        check("peek() on empty stack throws IllegalStateException",
                throwsException(() -> new MyStack<String>().peek(), IllegalStateException.class));
    }

    // ============================================================
    // 5) MyPriorityQueue (binary heap, ordered by urgency)
    // ============================================================
    private static void testMyPriorityQueue() {
        System.out.println("\n--- [5] MyPriorityQueue (binary heap) ---");
        MyPriorityQueue<ServiceRequest> pq = new MyPriorityQueue<>();

        check("New priority queue isEmpty()", pq.isEmpty());

        ServiceRequest low = new ServiceRequest("SR010", "Volta Hall", "Cleaning", 1);
        ServiceRequest high = new ServiceRequest("SR011", "UG Hospital", "Medical Emergency", 3);
        ServiceRequest med = new ServiceRequest("SR012", "Balme Library", "IT Support", 2);
        ServiceRequest high2 = new ServiceRequest("SR013", "Main Gate", "Security", 3);

        // Insert deliberately out of urgency order
        pq.insert(low);
        pq.insert(med);
        pq.insert(high);
        pq.insert(high2);

        check("peek() returns a High urgency request first", pq.peek().urgency == 3);
        ServiceRequest first = pq.poll();
        ServiceRequest second = pq.poll();
        check("first two polled are both High urgency (3)", first.urgency == 3 && second.urgency == 3);
        check("third polled is Medium urgency (2)", pq.poll().urgency == 2);
        check("fourth polled is Low urgency (1)", pq.poll().urgency == 1);
        check("priority queue empty after draining all 4", pq.isEmpty());

        // Resize test: initial backing array holds 19 usable slots
        MyPriorityQueue<Integer> big = new MyPriorityQueue<>();
        for (int i = 0; i < 40; i++) big.insert(i); // natural ascending order -> min-heap pops smallest first
        boolean ascendingOk = true;
        int prev = Integer.MIN_VALUE;
        for (int i = 0; i < 40; i++) {
            int val = big.poll();
            if (val < prev) { ascendingOk = false; break; }
            prev = val;
        }
        check("resize() preserves heap ordering across 40 items (>19 capacity)", ascendingOk);

        check("poll() on empty priority queue throws IllegalStateException",
                throwsException(() -> new MyPriorityQueue<Integer>().poll(), IllegalStateException.class));
        check("insert(null) throws IllegalArgumentException",
                throwsException(() -> new MyPriorityQueue<Integer>().insert(null), IllegalArgumentException.class));
    }

    // ============================================================
    // 6) RequestScheduler (MyPriorityQueue + MyQueue combined)
    // ============================================================
    private static void testRequestScheduler() {
        System.out.println("\n--- [6] RequestScheduler ---");
        RequestScheduler scheduler = new RequestScheduler();

        Resource van = new Resource("RES001", "Maintenance Van", "CS Dept", 2);
        Resource ambulance = new Resource("RES002", "Ambulance", "UG Hospital", 1);
        scheduler.registerResource(van);
        scheduler.registerResource(ambulance);
        check("2 resources registered", scheduler.availableResourceCount() == 2);

        ServiceRequest cleaning = new ServiceRequest("SR020", "Volta Hall", "Cleaning", 1);
        ServiceRequest emergency = new ServiceRequest("SR021", "UG Hospital", "Medical Emergency", 3);
        scheduler.addPriorityRequest(cleaning);
        scheduler.addPriorityRequest(emergency);
        check("2 priority requests queued", scheduler.pendingPriorityCount() == 2);

        String first = scheduler.dispatchNextPriorityRequest();
        check("higher urgency (SR021) dispatched before SR020", first.contains("SR021"));
        check("SR021 status flipped to Assigned", emergency.status.equals("Assigned"));

        String second = scheduler.dispatchNextPriorityRequest();
        check("second dispatch picks up SR020 with the remaining resource", second.contains("SR020"));
        check("no resources left after both dispatched", scheduler.availableResourceCount() == 0);

        String noResource = scheduler.dispatchNextPriorityRequest();
        check("dispatch with no requests left reports correctly",
                noResource.equals("No pending priority requests."));

        // Standard FIFO lane, separate from the priority lane
        RequestScheduler fifoTest = new RequestScheduler();
        fifoTest.registerResource(new Resource("RES010", "Cleaning Team A", "Maintenance Workshop", 5));
        ServiceRequest a = new ServiceRequest("SR030", "Legon Hall", "Cleaning", 1);
        ServiceRequest b = new ServiceRequest("SR031", "Akuafo Hall", "Cleaning", 1);
        fifoTest.addStandardRequest(a);
        fifoTest.addStandardRequest(b);
        String dispatched = fifoTest.dispatchNextStandardRequest();
        check("FIFO lane dispatches SR030 first (arrival order)", dispatched.contains("SR030"));
        check("dispatch with no resources left reports correctly",
                fifoTest.dispatchNextStandardRequest().equals("No resources available for dispatch."));
    }

    // ============================================================
    // 7) UndoManager (MyStack of Actions)
    // ============================================================
    private static void testUndoManager() {
        System.out.println("\n--- [7] UndoManager ---");
        UndoManager undoManager = new UndoManager();
        check("nothing to undo on a fresh manager", !undoManager.canUndo());
        check("undo() on empty history reports correctly",
                undoManager.undo().equals("Nothing to undo."));

        ServiceRequest req = new ServiceRequest("SR040", "Legon Hall", "Plumbing", 2);
        Resource team = new Resource("RES020", "Plumbing Team", "Maintenance Workshop", 3);

        undoManager.recordStatusChange(req, "Pending", "In Progress");
        check("status change applied", req.status.equals("In Progress"));

        undoManager.recordAssignment(req, team);
        check("assignment sets request to Assigned", req.status.equals("Assigned"));
        check("assignment sets resource to Assigned", team.status.equals("Assigned"));
        check("history has 2 recorded actions", undoManager.historySize() == 2);

        // Undo must be LIFO: assignment (most recent) reverts first
        String undo1 = undoManager.undo();
        check("first undo reverts the assignment", undo1.contains("Assignment"));
        check("request back to Pending after undoing assignment", req.status.equals("Pending"));
        check("resource back to Available after undoing assignment", team.status.equals("Available"));

        String undo2 = undoManager.undo();
        check("second undo reverts the earlier status change", undo2.contains("reverted back to 'Pending'"));
        check("request status rolled all the way back to original Pending", req.status.equals("Pending"));
        check("no more history left", !undoManager.canUndo());
    }

    // ============================================================
    // Tiny assertion helpers (no JUnit dependency needed)
    // ============================================================
    private static void check(String description, boolean condition) {
        if (condition) {
            passCount++;
            System.out.println("  [PASS] " + description);
        } else {
            failCount++;
            System.out.println("  [FAIL] " + description);
        }
    }

    private interface ThrowingAction {
        void run();
    }

    private static boolean throwsException(ThrowingAction action, Class<? extends Exception> expected) {
        try {
            action.run();
            return false; // nothing was thrown
        } catch (Exception e) {
            return expected.isInstance(e);
        }
    }
}