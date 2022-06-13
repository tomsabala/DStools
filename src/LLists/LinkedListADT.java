package LLists;

interface LinkedListADT<T> {
    public T prepend(T val); // add new item in the beginning
    public T append(T val); // add new item in the end
    public Node<T> remove(Node<T> where); // remove given item from data struct.
    public boolean isEmpty(); // check whether data struct. is empty.
    public int length(); // return how many items are in the data struct.
    public Node<T> get(int i); // return the i-th item.
    public void set(Node<T> who, T val); //set item value.
}