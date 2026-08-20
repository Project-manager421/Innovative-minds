package structures;



public class MyQueue<T> {
    private Object[] elements;
    private int head, tail, size;

    public MyQueue() {
        elements = new Object[20];
        head = 0; tail = 0; size = 0;
    }

    public void enqueue(T item) {
        if (size == elements.length) resize();
        elements[tail] = item;
        tail = (tail + 1) % elements.length;
        size++;
    }

    @SuppressWarnings("unchecked")
    public T dequeue() {
        if (isEmpty()) throw new IllegalStateException("Queue is empty");
        T item = (T) elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        size--;
        return item;
    }

    public boolean isEmpty() { return size == 0; }

    private void resize() {
        Object[] newArr = new Object[elements.length * 2];
        for (int i = 0; i < size; i++) {
            newArr[i] = elements[(head + i) % elements.length];
        }
        elements = newArr; head = 0; tail = size;
    }

public int size() { 
        return size; 
    }

    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) throw new IllegalStateException("Queue is empty");
        return (T) elements[head];
    }

}