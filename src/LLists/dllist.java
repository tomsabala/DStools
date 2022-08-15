package LLists;

import java.util.Iterator;
import java.util.List;

public class dllist<T> extends llist<T>{

    private dNode<T> head;
    //constructors
    public dllist() { // empty constructor
        super();
    }

    public dllist(dNode<T> head){ // construct from node
        super(head);
        this.head = head;
    }

    public dllist(List<T> array){ // construct from array
        int n = array.size();
        this.length = n;
        id = getIds();
        setIds();
        if(n==0)  // empty array
        {
            this.head = new dNode<>();
        } else { // create item for all values in array
            dNode<T> head = new dNode<>(array.get(0));
            dNode<T> tmp = head;
            for (int i = 1; i < n; ++i) {
                dNode<T> next = new dNode<>(array.get(i)); // new item
                tmp.setNext(next); // point previous.next to new item
                next.setPrev((dNode<T>)tmp);
                tmp = next;
            }
            tmp.setNext(head);
            head.setPrev(tmp);
            this.head = head;
        }
    }

    @Override
    public dNode<T> getHead() {
        return head;
    }

    public void setHead(dNode<T> head) {
        this.head = head;
    }

    public T prepend(dNode<T> node){
        if (isEmpty()) // if list was empty
        {
            this.length ++;
            setHead(node);
        }
        else
        { // list in not empty
            dNode<T> last = this.head.getPrev();
            node.setNext(this.getHead()); // push items forward
            this.getHead().setPrev(node);
            node.setPrev((last));
            last.setNext(node);
            setHead(node);
        }
        return node.getVal();
    }

    public T append(dNode<T> node) {
        if (isEmpty()) // if list is empty
        {
            prepend(node);
            dNode<T> head = this.getHead();
            head.setNext(head);
            head.setPrev(head);
            return node.getVal();
        }
        else
        { //list in not empty
            dNode<T> last = this.getHead().getPrev();
            node.setNext(this.getHead());
            node.setPrev(last);
            this.getHead().setPrev(node);
            last.setNext(node);

        }
        this.length++;
        return node.getVal();
    }
    @Override
    public T prepend(T val) {
        dNode<T> new_item = new dNode<>(val);
        return this.prepend(new_item);
    }

    @Override
    public T append(T val) { // at item at the end
        dNode<T> new_item = new dNode<>(val);
        return this.append(new_item);
    }

    @Override
    public dNode<T> remove(int index){
        // split cases for 0, other
        // list is not empty, search for item in it
        if (isEmpty() || index>this.length || index<=0) { // list is empty
            return null;
        }
        dNode<T> pivot =  this.getHead();
        int iter = 1;
        while (pivot != null && iter < index) {
            pivot = pivot.getNext();
            iter ++;
        }
        dNode<T> ans = pivot;
        if(ans == null)
            return null;
        ans.getPrev().setNext(ans.getNext());
        ans.getNext().setPrev(ans.getPrev());
        this.length--;
        if(index == 1 && this.length()!=0){
            this.setHead(this.getHead().getNext());
        }
        if(this.length()==0){
            this.setHead(null);
        }
        return ans;
    }

    @Override
    public Iterator<T> iterator() {
        return new llistIterator<>(this);
    }
}
