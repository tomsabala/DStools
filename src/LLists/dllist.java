package LLists;

import java.util.List;

public class dllist<T> extends llist<T>{

    private dNode<T> head;
    //constructors
    public dllist() { // empty constructor
        super();
    }

    public dllist(dNode<T> head){ // construct from node
        super(head);
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
}
