package Stacks_Queues;

import java.lang.reflect.Array;
import java.util.Arrays;

public class priorityQueue<T> implements priorityQueueADT<T> {
    protected int size=0;
    protected final int capacity;
    protected priorityQueueNode<T>[] elements;

    @SuppressWarnings("unchecked")
    public priorityQueue(int n) {
        capacity = n;
        elements = (priorityQueueNode<T>[]) Array.newInstance(priorityQueueNode.class, n);
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
        if(this.isFull()){
            return;
        }
        priorityQueueNode<T> item = new priorityQueueNode<>(val, p);
        elements[++size] = item;
        int pos = size;
        while (pos != 1 && item.priority > elements[pos/2].priority) {
            elements[pos] = elements[pos/2];
            pos /=2;
        }
        elements[pos] = item;
    }


    @Override
    public priorityQueueNode<T> dequeue() {
        int parent, child;
        priorityQueueNode<T> item, temp;
        if (isEmpty() ) {
            return null;
        }

        item = elements[1];
        temp = elements[size--];

        parent = 1;
        child = 2;
        while (child <= size) {
            if (child < size && elements[child].priority < elements[child + 1].priority)
                child++;
            if (temp.priority >= elements[child].priority)
                break;
            elements[parent] = elements[child];
            parent = child;
            child *= 2;
        }

        elements[parent] = temp;
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
        return size==capacity;
    }
}
