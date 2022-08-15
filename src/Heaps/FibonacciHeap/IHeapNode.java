package Heaps.FibonacciHeap;

public interface IHeapNode{
    public int getKey();
    public InfoNode getInfo();
    public int getRank();
    public boolean isMark();
    public FHeapNode getChild();
    public FHeapNode getNext();
    public FHeapNode getPrev();
    public FHeapNode getParent();
    public void setKey(int k);
    public void setRank(int k);
    public void flipMark();
    public void setChild(FHeapNode c);
    public void setNext(FHeapNode n);
    public void setPrev(FHeapNode p);
    public void setParent(FHeapNode par);
    public void setInfo(InfoNode info);
}
