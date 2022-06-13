package LLists;

import java.util.Objects;

public class Node<T>{
    private T val;
    private final int id;
    private Node<T> next;


    private static int ids;
    // constructors
    public Node(){
        this.val = null;
        this.next = null;
        this.id = getIds();
        setIds();
    }

    public Node(T val){
        this.val = val;
        this.next = null;
        this.id = getIds();
        setIds();
    }

    // getter & setter
    public T getVal() {
        return val;
    }

    public void setVal(T val) {
        this.val = val;
    }

    public Node<T> getNext() {
        return next;
    }

    public void setNext(Node<T> next) {
        this.next = next;
    }

    public int getId() {
        return id;
    }

    public static int getIds() {
        return ids;
    }

    public static void setIds() {
        Node.ids ++;
    }

    // count items
    public int countItems(){
        Node<T> tmp = this;
        int counter  = 0;
        while(tmp != null){
            counter ++;
            tmp = tmp.getNext();
        }
        return counter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node<?> node = (Node<?>) o;
        return getId() == node.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getVal(), getId(), getNext());
    }
}
