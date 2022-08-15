package Heaps.FibonacciHeap;

public class InfoNode {
    private int info;
    private int key;
    private FHeapNode node;

    public InfoNode(int k, FHeapNode source) {
        this.info = -1;
        this.key = k;
        this.node = source;
    }

    public int getInfo() {
        return this.info;
    }

    public void setInfo(int info) {
        this.info = info;
    }

    public int getKey() {
        return key;
    }

    public FHeapNode getNode() {
        return node;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public void setNode(FHeapNode node) {
        this.node = node;
    }
}
