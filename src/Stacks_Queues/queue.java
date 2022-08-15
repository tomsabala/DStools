package Stacks_Queues;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Objects;
import java.util.Queue;

public class queue<T> implements queueADT<T> {
    protected T[] elements;
    protected int size = 0;
    protected int capacity;

    protected Class<T> c;
    private int first_elem;
    private int last_elem;

    private final int id;
    static private int ids=1;

    @SuppressWarnings("unchecked")
    public queue(Class<T> clazz, int n) {
        this.capacity = n;
        this.c = clazz;
        this.elements = (T[]) Array.newInstance(c, n);
        this.id=ids;
        ids++;
    }

    @Override
    public void enqueue(T val) {
        if(isFull()){
            return;
        }
        if(isEmpty()){
            this.elements[0] = val;
            this.first_elem = 0;
            this.last_elem = 0;
            this.size++;
        } else {
            this.last_elem++;
            this.last_elem %= this.capacity;
            this.elements[this.last_elem] = val;
            this.size++;
        }
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
    @SuppressWarnings("unchecked")
    public T[] toArray() {
        T[] ans = (T[])Array.newInstance(this.c, this.size);
        queue<T> cache = new queue<>(this.c, this.capacity);
        int index = 0;
        while(!this.isEmpty()){
            T val = this.dequeue();
            ans[index++] = val;
            cache.enqueue(val);
        }

        while(!cache.isEmpty()){
            this.enqueue(cache.dequeue());
        }

        return ans;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        queue<?> queue = (queue<?>) o;
        return id == queue.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public Iterator<T> iterator() {
        return new queueIterator<>(this);
    }
}

class queueIterator<T> implements Iterator<T> {

    private int index;
    private final T[] queue;

    // constructor
    public queueIterator(queue<T> queue) {
        index = 0;
        this.queue = (T[]) queue.toArray();
    }

    // Checks if the next element exists
    public boolean hasNext() {
        return index<queue.length;
    }

    // moves the cursor/iterator to next element
    public T next() {
        return queue[index++];
    }
}
