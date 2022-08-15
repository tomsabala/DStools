package Heaps.FibonacciHeap;

/**
 * public class HeapNode
 *
 * If you wish to implement classes other than FibonacciHeap
 * (for example HeapNode), do it in this file, not in another file.
 *
 */
public class FHeapNode implements IHeapNode {

    public int key;
    private InfoNode info;
    private int rank;
    private boolean mark;
    private FHeapNode child;
    private FHeapNode next;
    private FHeapNode prev;
    private FHeapNode parent;
    private boolean min_inf; //for delete (special value)

    public FHeapNode(int key) {
        this.key = key;
        this.rank = 0;
        this.mark = false;
        this.child = null;
        this.next = this;
        this.prev = this;
        this.parent = null;
        this.min_inf = false;
        this.info = new InfoNode(key, this);

    }
    @Override
    public int getKey() {return this.key;}

    @Override
    public InfoNode getInfo() {
        return this.info;
    }

    @Override
    public int getRank() {return this.rank;}

    @Override
    public boolean isMark() {return this.mark;}

    @Override
    public FHeapNode getChild() {return this.child;}

    @Override
    public FHeapNode getNext() {return this.next;}

    @Override
    public FHeapNode getPrev() {return this.prev;}

    @Override
    public FHeapNode getParent() {return this.parent;}

    @Override
    public void setKey(int k) {this.key = k;}

    @Override
    public void setRank(int r) {this.rank = r;}

    @Override
    public void flipMark() {this.mark = !this.mark;}

    @Override
    public void setChild(FHeapNode c) {this.child = c;}

    @Override
    public void setNext(FHeapNode n) {this.next = n;}

    @Override
    public void setPrev(FHeapNode p) {this.prev = p;}

    @Override
    public void setParent(FHeapNode par) {this.parent = par;}


    protected boolean isBiggerThan(FHeapNode v2){ //compares nodes considering special value
        if (v2.isMin())
            return true;
        if (this.isMin())
            return false;

        return ((this.getKey() - v2.getKey()) > 0);
    }

    protected void setKey_ToMinInf(){
        this.min_inf = true;
    } //for delete(), special value

    protected boolean isMin(){
        return this.min_inf;
    }

    boolean getMarked(){
        return this.mark;
    }

    @Override
    public void setInfo(InfoNode info) {
        this.info.setInfo(info.getInfo());
        this.info.setKey(info.getKey());
        this.info.setNode(info.getNode());
    }
}

