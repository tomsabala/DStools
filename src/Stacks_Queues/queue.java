package Stacks_Queues;

import java.lang.reflect.Array;

public class queue<T> implements queueADT<T> {
    protected T[] elements;
    protected int size = 0;
    protected int capacity;
    private int first_elem;
    private int last_elem;

    @SuppressWarnings("unchecked")
    public queue(Class<T> clazz, int n) {
        this.capacity = n;
        this.elements = (T[]) Array.newInstance(clazz, n);
    }

    @Override
    public void enqueue(T val) {
        if(isFull()){
            return;
        }
        this.last_elem ++;
        this.last_elem %= this.capacity;
        this.elements[this.last_elem] = val;
        this.size++;
    }

    @Override
    public T dequeue() {
        if(isEmpty()){
            return null;
        }
        T res = this.elements[first_elem];
        this.first_elem ++;
        this.first_elem %= this.capacity;
        this.size --;
        return res;
    }

    @Override
    public T peek() {
        if(isEmpty()){
            return null;
        }
        return this.elements[this.first_elem];
    }

    @Override
    public int Size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public boolean isFull() {
        return this.size == capacity;
    }
}
