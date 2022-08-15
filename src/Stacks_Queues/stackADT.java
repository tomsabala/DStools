package Stacks_Queues;

public interface stackADT<T> extends Iterable<T>{
    void push(T val);
    T pop();
    boolean isEmpty();
    int Size();
    T top();
}
