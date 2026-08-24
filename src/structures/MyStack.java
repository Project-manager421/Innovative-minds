package structures;

public class MyStack<T> {
    private Object[] elements;
    private int top;
    private static final int INITIAL_CAPACITY = 20;

    public MyStack() {
        elements = new Object[INITIAL_CAPACITY];
        top = -1;
    }

    public void push(T item) {
        if (top == elements.length - 1) resize();
        elements[++top] = item;
    }

    @SuppressWarnings("unchecked")
    public T pop() {
        if (isEmpty()) throw new IllegalStateException("Stack is empty");
        T item = (T) elements[top];
        elements[top--] = null;
        return item;
    }

    public boolean isEmpty() {
        return top == -1;
    }

    private void resize() {
        Object[] newArray = new Object[elements.length * 2];
        System.arraycopy(elements, 0, newArray, 0, elements.length);
        elements = newArray;
    }


    public int size() {
        return top + 1;
    }

    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) throw new IllegalStateException("Stack is empty");
        return (T) elements[top];
    }


}