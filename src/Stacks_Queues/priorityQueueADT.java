package Stacks_Queues;

public interface priorityQueueADT<T> extends Iterable<T>{
    void enqueue(T val, int p);
    priorityQueueNode<T> dequeue();
    priorityQueueNode<T> peek();
    int Size();
    boolean isEmpty();
    boolean isFull();
}
