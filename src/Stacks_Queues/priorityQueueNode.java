package Stacks_Queues;

public class priorityQueueNode<T> {
    protected T val;
    protected int priority;

    public priorityQueueNode(T val, int p){
        this.val = val;
        this.priority = p;
    }

    public T getVal() {
        return val;
    }

    public void setVal(T val) {
        this.val = val;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
