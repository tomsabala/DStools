package Stacks_Queues;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Objects;

public class priorityQueue<T> implements priorityQueueADT<T> {
    protected int size=0;
    protected final int capacity;
    protected priorityQueueNode<T>[] elements;
    static private int ids = 1;
    private final int id;

    @SuppressWarnings("unchecked")
    public priorityQueue(int n) {
        capacity = n+1;
        elements = (priorityQueueNode<T>[]) Array.newInstance(priorityQueueNode.class, capacity);
        this.id = ids;
        ids++;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCapacity() {
        return capacity;
    }

    public void enqueue(T val, int p) {
        priorityQueueNode<T> new_item = new priorityQueueNode<>(val, p);
        enqueue(new_item);
    }

    public void enqueue(priorityQueueNode<T> item) {
        this.elements[++size] = item;
        int pos = size;

        while (pos != 1 && item.priority > this.elements[pos/2].priority) {
            this.elements[pos] = this.elements[pos/2];
            pos /=2;
        }

        this.elements[pos] = item;
    }

    @Override
    public priorityQueueNode<T> dequeue() {
        int parent, child;
        priorityQueueNode<T> item, temp;

        if (isEmpty() ) {
            return null;
        }

        item = this.elements[1];
        temp = this.elements[size--];
        parent = 1;
        child = 2;
        while (child <= size) {
            if (child < size && this.elements[child].priority < this.elements[child + 1].priority)
                child++;
            if (temp.priority >= this.elements[child].priority)
                break;
            this.elements[parent] = this.elements[child];
            parent = child;
            child *= 2;
        }

        this.elements[parent] = temp;
        return item;
    }

    @Override
    public priorityQueueNode<T> peek() {
        if(isEmpty()) {
            return null;
        }
        return elements[1];
    }

    @Override
    public int Size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size==0;
    }

    @Override
    public boolean isFull() {
        return size==capacity-1;
    }

    @SuppressWarnings("unchecked")
    public priorityQueueNode<T>[] toArray() {
        priorityQueueNode<T>[] ans = (priorityQueueNode<T>[]) Array.newInstance(priorityQueueNode.class, this.size);
        priorityQueue<T> cache = new priorityQueue<>(this.capacity);
        int index = 0;
        while(!this.isEmpty()){
            priorityQueueNode<T> node = this.dequeue();
            ans[index++] = node;
            cache.enqueue(node);
        }

        while(!cache.isEmpty()){
            this.enqueue(cache.dequeue());
        }

        return ans;
    }

    @Override
    public Iterator<T> iterator() {
        return new priorityQueueIterator<>(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        priorityQueue<?> that = (priorityQueue<?>) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

class priorityQueueIterator<T> implements Iterator<T> {

    private int index;
    private final priorityQueueNode<T>[] pQueue;

    // constructor
    public priorityQueueIterator(priorityQueue<T> queue) {
        index = 0;
        this.pQueue = queue.toArray();
    }

    // Checks if the next element exists
    public boolean hasNext() {
        return index<pQueue.length;
    }

    // moves the cursor/iterator to next element
    public T next() {
        return pQueue[index++].getVal();
    }
}