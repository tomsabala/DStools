package Stacks_Queues;

import LLists.Node;
import LLists.llist;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Objects;
import java.util.Stack;

public class stack<T> implements stackADT<T>{
    protected int N = 32;
    protected Class<T> c;
    protected int size=0;
    protected T[] elements;

    private final int id;
    static private int ids=1;



    @SuppressWarnings("unchecked")
    public stack(Class<T> clazz, int capacity) {
        N = capacity;
        c = clazz;
        elements = (T[]) Array.newInstance(c, N);
        this.id = ids;
        ids++;
    }

    @Override
    public void push(T val) {
        int n = this.Size();
        if(n == N){
            this.extend();
        }
        this.elements[n] = val;
        this.size++;
    }

    @Override
    public T pop() {
        int n = this.Size();
        T res = this.elements[n-1];
        this.elements[n-1] = null;

        this.size--;
        if(this.size <= N/2) {
            narrow();
        }

        return res;
    }

    @Override
    public boolean isEmpty() {
        return this.Size() == 0;
    }

    @Override
    public int Size() {
        return size;
    }

    @Override
    public T top() {
        return this.elements[size-1];
    }

    @SuppressWarnings("unchecked")
    private void extend() {
        N = Math.max(2, N * 2);
        T[] new_elements = (T[])Array.newInstance(c, N);
        int index = 0;
        for(T val : this.elements){
            new_elements[index] = this.elements[index];
            index++;
        }
        this.elements = new_elements;
    }

    @SuppressWarnings("unchecked")
    private void narrow() {
        N /= 2;
        T[] new_elements = (T[])Array.newInstance(c, N);
        int index = 0;
        while(index < N ){
            new_elements[index] = this.elements[index];
            index++;
        }
        this.elements = new_elements;
    }
    @SuppressWarnings("unchecked")
    public T[] toArray() {
        T[] ans = (T[])Array.newInstance(this.c, this.size);
        int index = 0;
        stack<T> cache = new stack<>(this.c, this.N);
        while(!this.isEmpty()){
            T val = this.pop();
            ans[index++] = val;
            cache.push(val);
        }
        while(!cache.isEmpty()){
            this.push(cache.pop());
        }
        return ans;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        stack<?> stack = (stack<?>) o;
        return id == stack.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    @Override
    public Iterator<T> iterator() {
        return new stackIterator<>(this);
    }
}

class stackIterator<T> implements Iterator<T> {

    private int index;
    private final T[] stack;

    // constructor
    public stackIterator(stack<T> stack) {
        index = 0;
        this.stack = stack.toArray();
    }

    // Checks if the next element exists
    public boolean hasNext() {
        return index<stack.length;
    }

    // moves the cursor/iterator to next element
    public T next() {
        return stack[index++];
    }
}
