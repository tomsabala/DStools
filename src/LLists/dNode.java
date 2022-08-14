package LLists;

public class dNode<T> extends Node<T>{
    private dNode<T> prev;

    public dNode(){
        super();
        this.prev = null;
    }

    public dNode(T val){
        super(val);
        this.prev = null;
    }

    public dNode<T> getPrev() {
        return prev;
    }
    public dNode<T> getNext() {
        return (dNode<T>) super.getNext();
    }

    public void setPrev(dNode<T> prev) {
        this.prev = prev;
    }
}
