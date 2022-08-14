package LLists;

import java.util.Iterator;

interface LinkedListADT<T> extends Iterable<T>{
    T prepend(T val); // add new item in the beginning
    T append(T val); // add new item in the end
    Node<T> remove(int index); // remove given item from data struct.
    boolean isEmpty(); // check whether data struct. is empty.
    int length(); // return how many items are in the data struct.
    Node<T> get(int i); // return the i-th item.
    void set(Node<T> who, T val); //set item value.
}