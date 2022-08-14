package Stacks_Queues;

public interface queueADT<T> {
    void enqueue(T val);
    T dequeue();
    T peek();
    int Size();
    boolean isEmpty();
    boolean isFull();
}
