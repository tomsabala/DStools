package LLists;

import javax.swing.*;
import java.util.List;
import java.util.Objects;

public class llist<T> implements LinkedListADT<T>{
    //properties
    protected int length;
    protected Node<T> head;
    protected int id;
    protected static int ids;

    //constructors
    public llist() { // empty constructor
        this.length = 0;
        this.head = new Node<T>();
        id = getIds();
        setIds();
    }

    public llist(Node<T> head){ // construct from node
        this.length = head.countItems();
        this.head = head;
        id = getIds();
        setIds();
    }

    public llist(List<T> array){ // construct from array
        int n = array.size();
        this.length = n;
        id = getIds();
        setIds();
        if(n==0)  // empty array
        {
            this.head = new Node<>();
        } else { // create item for all values in array
            Node<T> head = new Node<>(array.get(0));
            Node<T> tmp = head;
            for (int i = 1; i < n; ++i) {
                Node<T> next = new Node<>(array.get(i)); // new item
                tmp.setNext(next); // point previous.next to new item
                tmp = next;
            }
            this.head = head;
        }
    }

    @Override
    public T prepend(T val) { // add new item at the start
        this.length ++;
        Node<T> new_item = new Node<>(val);
        if (isEmpty()) // if list was empty
        {
            setHead(new_item);
        }
        else
        { // list in not empty
            new_item.setNext(this.getHead().getNext()); // push items forward
            setHead(new_item);
        }
        return val;
    }

    @Override
    public T append(T val) { // at item at the end
        if (isEmpty()) // if list is empty
        {
            return prepend(val);
        }
        else
        { //list in not empty
            Node<T> pivot = this.getHead();
            while(pivot.getNext() != null){
                pivot = pivot.getNext();
            }
            add_on(pivot, val); // add item function on pivot location
            return val;
        }
    }

    @Override
    public Node<T> remove(Node<T> where) {
        // split cases for 0, other
        // list is not empty, search for item in it
        if (isEmpty()) { // list is empty
            return null;
        }
        Node<T> prev = null;
        Node<T> pivot = this.getHead();
        while (pivot != null && pivot != where) {
            prev = pivot;
            pivot = pivot.getNext();
        }
        if (pivot != null && prev != null) {
            prev.setNext(pivot.getNext());
            this.length--;
            return pivot;
        }
        return null;
    }

    @Override
    public boolean isEmpty() { // check if list is empty
        return this.length() == 0;
    }

    @Override
    public int length() { // return list length
        return this.length;
    }

    @Override
    public Node<T> get(int i) { // return the i-th item.
        if(i > length()){return null;}
        Node<T> pivot = this.getHead();
        int steps = 0;
        while(steps < i){
            pivot = pivot.getNext();
            steps++;
        }
        return pivot;
    }

    @Override
    public void set(Node<T> who, T val) {
        if(nodeWhich(who)){
            Node<T> pivot = this.getHead();
            while(pivot!= who){
                pivot = pivot.getNext();
            }
            pivot.setVal(val);
        }
    }

    public Node<T> getTail(){
        if(isEmpty()){return null;}
        Node<T>pivot = getHead();
        while(pivot.getNext() != null){
            pivot = pivot.getNext();
        }
        return pivot;
    }
    public Node<T> getHead() {
        return head;
    }

    public void setHead(Node<T> head) {
        this.head = head;
    }

    public int getId() {
        return id;
    }

    public static int getIds() {
        return ids;
    }

    public static void setIds() {
        llist.ids++;
    }

    public void add_on(Node<T> on, T val){
        if(on == null){return;}
        Node<T> new_item = new Node<>(val);
        Node<T> pivot = this.getHead();
        while(pivot != null && pivot != on){
            pivot = pivot.getNext();
        }
        if(pivot != null){
            new_item.setNext(pivot.getNext());
            pivot.setNext(new_item);
        }
        this.length ++;
    }

    public boolean nodeWhich(Node<T> node){
        Node<T> pivot = this.getHead();
        while(pivot != null){
            if(pivot == node){
                return true;
            }
            pivot = pivot.getNext();
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        llist<?> llist = (llist<?>) o;
        return id == llist.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
