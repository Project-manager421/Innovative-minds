package datastructures;

public class MyMinHeap<T> {

    private HeapNode<T>[] heap;
    private int size;

    @SuppressWarnings("unchecked")
    public MyMinHeap(int capacity) {
        heap = (HeapNode<T>[]) new HeapNode[capacity];
        size = 0;
    }

    public void insert(T data, double priority) {

        if (size == heap.length) {
            resize();
        }

        heap[size] = new HeapNode<>(data, priority);

        heapifyUp(size);

        size++;
    }

    public HeapNode<T> extractMin() {

        if (size == 0) {
            return null;
        }

        HeapNode<T> min = heap[0];

        size--;

        heap[0] = heap[size];
        heap[size] = null;

        if (size > 0) {
            heapifyDown(0);
        }

        return min;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void heapifyUp(int index) {

        while (index > 0) {

            int parent = (index - 1) / 2;

            if (heap[parent].priority <= heap[index].priority) {
                break;
            }

            swap(parent, index);

            index = parent;
        }
    }

    private void heapifyDown(int index) {

        while (true) {

            int left = 2 * index + 1;
            int right = 2 * index + 2;

            int smallest = index;

            if (left < size &&
                    heap[left].priority < heap[smallest].priority) {

                smallest = left;
            }

            if (right < size &&
                    heap[right].priority < heap[smallest].priority) {

                smallest = right;
            }

            if (smallest == index) {
                break;
            }

            swap(index, smallest);

            index = smallest;
        }
    }

    private void swap(int i, int j) {

        HeapNode<T> temp = heap[i];

        heap[i] = heap[j];
        heap[j] = temp;
    }

    @SuppressWarnings("unchecked")
    private void resize() {

        HeapNode<T>[] newHeap =
                (HeapNode<T>[]) new HeapNode[heap.length * 2];

        for (int i = 0; i < heap.length; i++) {
            newHeap[i] = heap[i];
        }

        heap = newHeap;
    }

    public static class HeapNode<T> {

        private T data;
        private double priority;

        public HeapNode(T data, double priority) {
            this.data = data;
            this.priority = priority;
        }

        public T getData() {
            return data;
        }

        public double getPriority() {
            return priority;
        }
    }
}