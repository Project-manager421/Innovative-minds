package structures;

public class MyCircularQueue<T> {
    private Object[] data;
    private int front;
    private int rear;
    private int size;
    private int capacity;

    public MyCircularQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than zero");
        }
        this.capacity = capacity;
        this.data = new Object[capacity];
        this.front = 0;
        this.rear = -1;
        this.size = 0;
    }

    // Default constructor if capacity isn't specified
    public MyCircularQueue() {
        this(10);
    }

    // Add item to the back of the circular queue
    public boolean enqueue(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot insert null item");
        }
        if (isFull()) {
            return false; // Queue is full, item not added
        }

        rear = (rear + 1) % capacity; // Wrap around to index 0 when reaching end
        data[rear] = item;
        size++;
        return true;
    }

    // Remove item from the front of the circular queue
    @SuppressWarnings("unchecked")
    public T dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Circular Queue is empty");
        }

        T item = (T) data[front];
        data[front] = null; // Prevent memory leak
        front = (front + 1) % capacity; // Wrap around to index 0 when reaching end
        size--;
        return item;
    }

    // Look at the front item without removing it
    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Circular Queue is empty");
        }
        return (T) data[front];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull() {
        return size == capacity;
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return capacity;
    }

    @Override
    public String toString() {
        if (isEmpty()) return "[]";
        StringBuilder sb = new StringBuilder("[");
        int current = front;
        for (int i = 0; i < size; i++) {
            sb.append(data[current]);
            if (i < size - 1) sb.append(", ");
            current = (current + 1) % capacity;
        }
        sb.append("]");
        return sb.toString();
    }

}