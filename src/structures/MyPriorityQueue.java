package structures;

public class MyPriorityQueue<T extends Comparable<T>> {
    private Object[] heap;
    private int size;

    public MyPriorityQueue() {
        // Index 0 is unused in 1-based heap arrays
        heap = new Object[20];
        size = 0;
    }

    public void insert(T item) {
        if (item == null) throw new IllegalArgumentException("Cannot insert null item");
        if (size == heap.length - 1) resize();
        
        heap[++size] = item;
        swim(size);
    }

    @SuppressWarnings("unchecked")
    public T poll() {
        if (isEmpty()) throw new IllegalStateException("Priority Queue is empty");
        
        T root = (T) heap[1];
        swap(1, size);
        heap[size] = null; // Prevent memory leak
        size--;
        
        if (size > 0) {
            sink(1);
        }
        
        return root;
    }

    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) throw new IllegalStateException("Priority Queue is empty");
        return (T) heap[1];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    private void swim(int k) {
        // Move child (k) up if it has higher priority than parent (k / 2)
        while (k > 1 && compare(k, k / 2) < 0) {
            swap(k, k / 2);
            k = k / 2;
        }
    }

    private void sink(int k) {
        while (2 * k <= size) {
            int j = 2 * k; // Left child
            
            // Pick the higher-priority child between left (j) and right (j + 1)
            if (j < size && compare(j + 1, j) < 0) {
                j++;
            }
            
            // If parent (k) is already higher priority than highest child (j), stop
            if (compare(k, j) <= 0) break;
            
            swap(k, j);
            k = j;
        }
    }

    @SuppressWarnings("unchecked")
    private int compare(int i, int j) {
        return ((T) heap[i]).compareTo((T) heap[j]);
    }

    private void swap(int i, int j) {
        Object tmp = heap[i];
        heap[i] = heap[j];
        heap[j] = tmp;
    }

    private void resize() {
        Object[] newHeap = new Object[heap.length * 2];
        System.arraycopy(heap, 0, newHeap, 0, heap.length);
        heap = newHeap;
    }
}