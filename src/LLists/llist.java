package LLists;

import java.util.Iterator;
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
        this.head = null;
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

    public T append(Node<T> node){
        if(isEmpty()) {
            return prepend(node);
        } else {
            Node<T> pivot = this.getHead();
            while(pivot.getNext() != null){
                pivot = pivot.getNext();
            }
            add_on(pivot, node); // add item function on pivot location
            return node.getVal();
        }
    }

    public T prepend(Node<T> node){
        if(isEmpty()){
            setHead(node);
        } else {
            node.setNext(this.getHead());
            setHead(node);
        }
        this.length++;
        return node.getVal();
    }

    @Override
    public T prepend(T val) { // add new item at the start
        Node<T> new_item = new Node<>(val);
        return this.prepend(new_item);
    }

    @Override
    public T append(T val) { // at item at the end
        Node<T> new_item = new Node<>(val);
        return this.append(new_item);
    }

    @Override
    public Node<T> remove(int index) {
        // split cases for 0, other
        // list is not empty, search for item in it
        if (isEmpty() || index>this.length || index<=0) { // list is empty
            return null;
        }
        Node<T> prev = null;
        Node<T> pivot = this.getHead();
        int iter = 1;
        while (pivot != null && iter < index) {
            prev = pivot;
            pivot = pivot.getNext();
            iter ++;
        }
        if(prev == null){
            Node<T> res = this.getHead();
            this.setHead(res.getNext());
            this.length--;
            return res;
        }
        if (pivot != null) {
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
        /* insert a node with value val, after given node */
        Node<T> new_item = new Node<>(val);
        add_on(on, new_item);
    }

    public void add_on(Node<T> on, Node<T> node){
        /* insert a node with value val, after given node */
        if(on == null){return;}
        Node<T> pivot = this.getHead();
        while(pivot != null && pivot != on){
            pivot = pivot.getNext();
        }
        if(pivot != null){
            node.setNext(pivot.getNext());
            pivot.setNext(node);
        }
        this.length ++;
    }

    public boolean nodeWhich(Node<T> node){
        /* check weather node is in the list */
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


    @Override
    public Iterator<T> iterator() {
        return new llistIterator<>(this);
    }
}

class llistIterator<T> implements Iterator<T> {

    private Node<T> current;
    private llist<T> lst;
    int index = 0;

    // constructor
    public llistIterator(llist<T> lst) {
        current = lst.getHead();
        this.lst = lst;
    }

    // Checks if the next element exists
    public boolean hasNext() {
        return index<lst.length();
    }

    // moves the cursor/iterator to next element
    public T next() {
        Node<T> ans = current;
        index++;
        current = current.getNext();
        return ans.getVal();
    }
}
