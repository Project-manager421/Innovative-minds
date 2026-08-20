package structures;

public class MyDeque<T> {
    
    // Custom Node class for doubly linked list
    private static class Node<T> {
        T data;
        Node<T> next;
        Node<T> prev;

        Node(T data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    public MyDeque() {
        head = null;
        tail = null;
        size = 0;
    }

    // Insert item at the front
    public void addFirst(T item) {
        if (item == null) throw new IllegalArgumentException("Cannot insert null item");
        
        Node<T> newNode = new Node<>(item);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }

    // Insert item at the back
    public void addLast(T item) {
        if (item == null) throw new IllegalArgumentException("Cannot insert null item");

        Node<T> newNode = new Node<>(item);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            newNode.prev = tail;
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    // Remove item from the front
    public T removeFirst() {
        if (isEmpty()) throw new IllegalStateException("Deque is empty");

        T item = head.data;
        head = head.next;
        
        if (head == null) {
            tail = null; // Deque became empty
        } else {
            head.prev = null;
        }
        
        size--;
        return item;
    }

    // Remove item from the back
    public T removeLast() {
        if (isEmpty()) throw new IllegalStateException("Deque is empty");

        T item = tail.data;
        tail = tail.prev;

        if (tail == null) {
            head = null; // Deque became empty
        } else {
            tail.next = null;
        }

        size--;
        return item;
    }

    // Look at the front item without removing
    public T peekFirst() {
        if (isEmpty()) throw new IllegalStateException("Deque is empty");
        return head.data;
    }

    // Look at the back item without removing
    public T peekLast() {
        if (isEmpty()) throw new IllegalStateException("Deque is empty");
        return tail.data;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }
}