package Stacks_Queues;

import java.lang.reflect.Array;

public class stack<T> implements stackADT<T>{
    static protected int N = 32;
    protected Class<T> c;
    protected int size=0;
    protected T[] elements;

    @SuppressWarnings("unchecked")
    public stack(Class<T> clazz, int capacity) {
        N = capacity;
        c = clazz;
        elements = (T[]) Array.newInstance(c, N);
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
        N *= 2;
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
        for(T val : this.elements){
            new_elements[index] = this.elements[index];
            index++;
        }
        this.elements = new_elements;
    }
}
