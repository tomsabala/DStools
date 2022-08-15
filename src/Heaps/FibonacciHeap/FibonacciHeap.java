package Heaps.FibonacciHeap;

public class FibonacciHeap
{
    private int size; // size of all heap
    private FHeapNode min; // pointer for the heap-node holding minimum key
    private FHeapNode first; // pointer to the first heap-node in the list
    private int trees; // total number of trees in the heap
    private int marked; // total number of marked heap-nodes in the heap
    private static int totalLinks; // total number of links operated
    private static int totalCuts; // total number of cuts operated

    /**
     * empty constructor
     * initializing default fields for an empty heap
     * placing size, trees and markedTrees to zero
     * min and first to null
     */
    public FibonacciHeap(){ // WC: O(1), AMORTIZED: O(1)
        setSize(0);
        this.first = null;
        setMin(null);
        setTrees(0);
        setMarked(0);
    }

    /**
     * @param k new number of cuts activated during the program, setting old one to the new integer k.
     * setting totalCuts static object to k.
     */
    private void setTotalCuts(int k){ // WC: O(1), AMORTIZED: O(1)
        totalCuts = k;
    }

    /**
     * @param k new number of links activated in the heap, usually will be old num + 1,
     * except for an empty constructor call.
     * setting totalLinks static object to k.
     */
    private void setTotalLinks(int k){ // WC: O(1), AMORTIZED: O(1)
        totalLinks = k;
    }

    /**
     * @param k a new number of marked heap-nodes setting to the heap
     * setting markedTrees to k.
     * amortized cost is O(1):
     * actual time is and change in potential is also one because k is always +- 1 old.marked
     */
    private void setMarked(int k){ // WC: O(1), AMORTIZED: O(1)
        this.marked = k;
    }

    /**
     * @return number of marked heap-nodes in the total heap
     * return the markedTrees sum by approaching this.marked.
     */
    private int getMarked(){ // WC: O(1), AMORTIZED: O(1)
        return this.marked;
    }

    /**
     * Addition
     * receive a new value of trees in the heap
     * @param k an integer, presents a new number of trees in the heap
     * setting overall trees number to k.
     *
     * amortized cost is O(1):
     */
    private void setTrees(int k){ // WC: O(1), AMORTIZED: O(1)
        this.trees = k;
    }

    /**
     * Addition
     * @return number of trees in the heap
     * return trees number by approaching to this.trees.
     */
    private int getTrees(){ // WC: O(1), AMORTIZED: O(1)
        return this.trees;
    }

    /**
     * Addition
     * setting first heap-node
     * setting first node to newNode
     * if heap is empty then newNode wiil be first and min
     * else, pushing the trees list to the left and inserting the newNode at first
     * setting pointers as needed
     */
    private void setFirst(FHeapNode newNode){ // WC: O(1), AMORTIZED: O(1)
        if (newNode != null){
            if (this.first != null){
                // setting newNode next and prev heaps
                newNode.setNext(this.first);
                newNode.setPrev(this.first.getPrev());
                // setting newNode as first heap
                this.first.setPrev(newNode);
                newNode.getPrev().setNext(newNode);
            }
            else {
                newNode.setNext(newNode);
                newNode.setPrev(newNode);
            }
            this.first = newNode;
        }
    }

    /**
     * Addition
     * setting a new size to the tree
     * setting size of the heap to k.
     */
    private void setSize(int k){ // WC: O(1), AMORTIZED: O(1)
        this.size = k;
    }

    /**
     * Addition
     * setting min-heap to newHeap
     * setting min heap-node to newHeap.
     */
    private void setMin(FHeapNode newHeap){ // WC: O(1), AMORTIZED: O(1)
        this.min = newHeap;
    }

    /**
     * public boolean isEmpty()
     * Returns true if and only if the heap is empty.
     * heap is empty iff size is 0.
     * checking whether heap is empty or not by approaching its size.
     */
    public boolean isEmpty() { // WC: O(1), AMORTIZED: O(1)
        return this.size == 0;
    }


    /**
     * public HeapNode insert(int key)
     * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap.
     * The added key is assumed not to already belong to the heap.
     * Returns the newly created node.
     * -----------------------
     * Implementation Analysis:
     * Lazy insertion, same as in Lazy Binomial Trees.
     * inserting a newNode to the heap with key value of k,
     * first creating the node, than
     * if heap is empty setting new node as first node and min,
     * else adding newNode to the first and checking whether to update min or not.
     * updating size and trees as needed.
     * function uses addToFirst() function.
     * As seen in class, Time-Complexity belongs to O(1) W.C.
     */
    public FHeapNode insert(int key) { // WC: O(1), AMORTIZED: O(1)
        // wrapping the new key inside a HeapNode
        FHeapNode newNode = new FHeapNode(key);

        //if heap isn't empty, lazy insert to heap left side
        if (!this.isEmpty()){
            this.addToFirst(newNode);


            // if new heap min is smaller than curren min, replace it
            if (newNode.getKey() < this.min.getKey()){
                this.setMin(newNode);
            }
        }
        else { //if current heap is empty, edge case (fields initializing)
            this.setMin(newNode);
            this.first = newNode;
            newNode.setPrev(newNode);
            newNode.setNext(newNode);
        }

        //heap fields maintenance
        this.setSize(this.size() + 1); // adding +1 to the heap size
        this.setTrees(this.getTrees() + 1); // adding +1 to the trees number

        return newNode;
    }

    /**
     * public void deleteMin
     * Deletes the node containing the minimum key.
     * ------------------------
     * Implementation Analysis:
     * Function differentiates between a heap which about to be empty,
     * to the complementary case.
     * DeleteMin asymptotic cost is amortized O(logn), and O(n) W.C.
     * The function first deletes the desired node, preserves valid links
     * between all the remaining nodes, and at last consolidates (successive-linking).
     */
    public void deleteMin() {
        //if there's a single tree in the heap
        if (this.getTrees() <= 1)
            this.deleteMinEdgeCases();

            //else, number of trees is greater than 1
        else
            this.deleteMin_In_NonEmptyHeap();

        //Successive-Linking:
        reorder_and_retrieveMin();
    }

    /**
     * deleteMinEdgeCases
     * Addition, part of DeleteMin
     * ------------------------
     * Implementation Analysis:
     * Handles a single node heap, or an empty one.
     * At the end, heap will be empty, so function detach the only node that
     * may be in it, and preserves its own attributes.
     * O(1) W.C. there's at most a single node in the list.
     */
    private void deleteMinEdgeCases(){
        //if an empty heap
        if (this.getTrees() == 0)
            return;

            //if a single tree:
        else {

            //if a single node:
            if (this.size == 1) {
                setSize(0);
                this.first = null;
                setMin(null);
                setTrees(0);
                setMarked(0);
                return;
            }


            else { //if min (which about to be deleted) has sons, linking them to the heap
                FHeapNode old_min = this.findMin();
                this.first = old_min.getChild(); //old min is the one we desire to delete
                this.trees += (old_min.getRank()-1); //heap fields maintenance
                this.size -= 1; //heap fields maintenance

                //updating previous root sons to be roots.
                FHeapNode v = this.findMin().getChild();
                while (v.getParent() == old_min){
                    if (v.isMark()) {
                        v.flipMark();
                        this.marked -= 1;
                    }
                    v.setParent(null);
                    v = v.getNext();
                }
                paralyzeNode(old_min); //detaching all links between old-min to the valid heap
            }
        }
    }

    /**
     * deleteMin_In_NonEmptyHeap
     * Addition, part of DeleteMin
     * ------------------------
     * Implementation Analysis:
     * Handles a heap with at least two elements. Main Difference is that there'll be a valid
     * min and first nodes after the deletion.
     * W.C. is O(rank(min-node)) which is O(logn), as all ranks in fib heap are in O(logn)
     */
    private void deleteMin_In_NonEmptyHeap() {
        FHeapNode old_min = this.findMin(); //old-min is the min node we desire to delete
        FHeapNode prev = old_min.getPrev(); //keeping its prev
        FHeapNode next = old_min.getNext(); //its next

        //if min is a leaf
        if (old_min.getRank() == 0) {
            prev.setNext(next);
            next.setPrev(prev);
            if (this.first == old_min)
                this.first = next;
        }

        //else, min has sons:
        else {
            FHeapNode first_son = old_min.getChild();
            FHeapNode last_son = first_son.getPrev();
            prev.setNext(first_son); //connecting sons to the heap
            next.setPrev(last_son);
            first_son.setPrev(prev);
            last_son.setNext(next);

            //transforming old-min sons to be roots
            FHeapNode v = old_min.getChild();
            for (int i=0; i<old_min.getRank(); i++){
                v.setParent(null);
                if (v.isMark()){
                    v.flipMark(); //removing marks, as roots cannot be marked
                    this.marked -= 1;
                }
                v = v.getNext();
            }

            //if old-min was first
            if (this.first == old_min)
                this.first = first_son;
        }

        //updating heap fields
        this.trees += (old_min.getRank() - 1);
        this.size -= 1;

        //remove its pointers to the valid heap
        paralyzeNode(old_min);
    }

    /**
     * paralyzeNode
     * Addition
     * ------------------------
     * Implementation Analysis:
     * Removes all links of the input node to the heap, doesn't any other of the node's attributes.
     * W.C. is O(1)
     */
    private void paralyzeNode(FHeapNode v){
        //do not change its rank!
        v.setPrev(v);
        v.setNext(v);
        v.setParent(null);
        v.setChild(null);
    }

    /**
     * reorder_and_retrieveMin
     * Addition, part of DeleteMin. Successive Linking \ Consolidating.
     * ------------------------
     * Implementation Analysis:
     * Func initializes trees array, iterates over heap trees, and consolidates.
     * Time-Complexity is O(#Trees + logn) which is O(logn) amortized, and O(n) W.C.
     */
    private void reorder_and_retrieveMin(){
        if (this.getTrees() == 0) //empty edge case
            return;

        //In a Fibonacci heap containing n items, all ranks are at most 2log_2 n
        //trees array for reordering
        FHeapNode[] treesReorderArray = new FHeapNode[2*(int)(Math.log(this.size()+1) / Math.log(2))+2];


        FHeapNode v = this.first;
        int numOfTrees = this.getTrees();
        for (int i=0; i<numOfTrees; i++){

            //reorder
            //if there's an unlinked same ranked tree in the list
            while (treesReorderArray[v.getRank()] != null){
                //extracting the tree with the same rank already in the list
                FHeapNode v2 = treesReorderArray[v.getRank()];
                treesReorderArray[v.getRank()] = null;

                //assigning v to be the linked tree
                v = link(v, v2);
            }
            treesReorderArray[v.getRank()] = v;

            v = v.getNext(); //next tree
        }

        //Generating a valid heap from the trees array (post-linking)
        setMin(null);
        this.first = null;
        for (int i=treesReorderArray.length-1; i>=0; i--){ //for each tree (unique ranked)
            v = treesReorderArray[i];
            if (v != null) { // if current cell holds a valid tree's root (instead of null)
                if (this.first == null){ //if it is the first tree we intend to add
                    this.first = v;
                    v.setNext(v); //heap attributes maintenance
                    v.setPrev(v);
                    setMin(v);
                }
                else { //first is already initialized
                    this.addToFirst(v); //adding tree to the beginning
                    if (v.getKey() < this.min.getKey()) //min maintenance
                        setMin(v);
                }
            }
        }
    }

    /**
     * link
     * Addition, part of DeleteMin.
     * ------------------------
     * Implementation Analysis:
     * Links 2 trees into a valid single one. Updates Heap class links count.
     * O(1) W.C.
     */
    private FHeapNode link(FHeapNode v, FHeapNode v2){
        FHeapNode next= v.getNext(); //the next tree for the successive linking function ("reorder_and...")
        this.trees -= 1; //heap fields
        totalLinks += 1; //class fields
        int k = v.getRank();

        //setting v2 to be the bigger tree
        if (v.getKey() > v2.getKey()){
            FHeapNode temp = v2;
            v2 = v;
            v = temp;
        }

        v.setNext(next); //for the general loop to be able to continue

        // edge case if rank is 0
        if (k != 0){ //rank is positive, at least one child
            FHeapNode rootsChild = v.getChild();
            FHeapNode lastChild = rootsChild.getPrev();

            rootsChild.setPrev(v2);
            v2.setNext(rootsChild);

            lastChild.setNext(v2);
            v2.setPrev(lastChild);
        } else { //if both trees have no children (leaves).
            v2.setNext(v2);
            v2.setPrev(v2);
        }
        v2.setParent(v);
        v.setChild(v2);

        v.setRank(k+1);

        return v;
    }

    /**
     * addToFirst
     * Addition
     * ------------------------
     * Implementation Analysis:
     * Adds a valid HeapNode (tree's root) to the heap beginning (left side).
     * O(1) W.C.
     */
    private void addToFirst(FHeapNode v){ // assumes heap isn't empty
        FHeapNode prevFirst = this.first;
        FHeapNode last = this.first.getPrev();

        v.setNext(prevFirst);
        v.setPrev(last);

        prevFirst.setPrev(v);
        last.setNext(v);

        this.first = v;
    }

    /**
     * public HeapNode findMin()
     * Returns the node of the heap whose key is minimal, or null if the heap is empty.
     * return min-node by approaching this.min.
     */
    public FHeapNode findMin() { // WC: O(1), AMORTIZED: O(1)
        // checking whether this heap is empty or not, and returning min tree as needed.
        if (this.isEmpty()) { return null;}
        return this.min;
    }

    /**
     * public void meld (FibonacciHeap heap2)
     * Melds heap2 with the current heap.
     * melding heap2 to original heap
     * if original heap is empty, setting the original heap exactly as heap2
     * else, adding heap2 trees from the last pointer in the original heap,
     * updating pointers as needed,
     * updating size, marked and trees as needed.
     */
    public void meld (FibonacciHeap heap2) { // WC: O(1), AMORTIZED: O(1)
        // checking heap2 is not empty
        if (!heap2.isEmpty()){
            // checking calling heap is not empty
            if (!this.isEmpty()){
                // setting heap2 first tree as the next tree in original heap
                this.first.getPrev().setNext(heap2.first);
                // setting next tree for the last tree in heap2 as first tree in original heap
                heap2.first.getPrev().setNext(this.first);
                // setting prev tree for the first tree in original heap as last tree in heap 2
                FHeapNode tmpHeapNode = this.first.getPrev();
                this.first.setPrev(heap2.first.getPrev());
                // setting prev tree for the first tree in heap2 as last tree in the original heap
                heap2.first.setPrev(tmpHeapNode);
                if (heap2.findMin().key < this.findMin().key){
                    this.setMin(heap2.findMin());
                }
            }
            else { // if calling heap is empty
                // assign min and first as heap2 min and first
                this.first = heap2.first;
                this.setMin(heap2.findMin());
            }
            // adding values of size, marked and trees to the existing values in original heap
            this.setSize(this.size + heap2.size);
            this.setMarked(this.getMarked() + heap2.getMarked());
            this.setTrees(this.getTrees() + heap2.getTrees());
        }

    }

    /**
     * public int size()
     * Returns the number of elements in the heap.
     * return size of the heap by approaching this.size.
     */
    public int size() { // WC: O(1), AMORTIZED: O(1)
        return this.size;
    }

    /**
     * public int[] countersRep()
     * Return an array of counters. The i-th entry contains the number of trees of order i in the heap.
     * Note: The size of the array depends on the maximum order of a tree, and an empty heap returns an empty array.
     * if heap is empty, return an empty array.
     * else, first we find the maximum rank inside the heap,
     * then setting array size to the maximum rank we found,
     * then for every tree in the heap setting array[tree.rank] + 1
     */
    public int[] countersRep() { // WC: O(n), AMORTIZED: O(n)
        int [] arr; // initial a new array of integers
        if (this.isEmpty()) { // if heap is empty
            // return an empty array
            arr = new int[0];
            return arr;
        }
        // heap is not empty
        // finding maximum rank from all trees
        int maxRank = 0;
        int currRank;
        FHeapNode currTree = this.first; // stepper tree
        for (int i=0; i<this.getTrees(); i++){ // loop over all trees in the heap
            currRank = currTree.getRank();
            if (currRank > maxRank){
                maxRank = currRank;
            }
            currTree = currTree.getNext();
        }

        // arrSize points on the maximum rank of all trees
        arr = new int[maxRank+1];
        currTree = this.first;
        for (int i=0; i<this.getTrees(); i++){ // loop over all trees in the heap
            // for every tree,  +1 to the -i- index fitting to the tree rank
            arr[currTree.getRank()] ++;
            currTree = currTree.getNext();
        }
        return arr;
    }

    /**
     * public void delete(HeapNode x)
     * Deletes the node x from the heap.
     * It is assumed that x indeed belongs to the heap.
     * -----------------------
     * Implementation Analysis:
     * Implemented as decrease-key to a special value (set to mininf, robust to any int values in the tree).
     * Deletes min.
     * Time Complexity is same as the deleteMin - O(logn) amortized and O(n) W.C.
     */
    public void delete(FHeapNode x) {
        x.setKey_ToMinInf();
        this.decreaseKey(x, 0);
        this.deleteMin();

        return;
    }

    /**
     * public void decreaseKey(HeapNode x, int delta)
     * Decreases the key of the node x by a non-negative value delta. The structure of the heap should be updated
     * to reflect this change (for example, the cascading cuts procedure should be applied if needed).
     * -----------------------
     * Implementation Analysis:
     * Decrease key, checks for heap invariants. If preserved, updates min and leaves.
     * Else, Cascading cuts.
     * Time-Complexity: as the number of cuts - O(logn) W.C. and O(1) amortized.
     */
    public void decreaseKey(FHeapNode x, int delta) {
        if (delta <= 0 && !x.isMin()) //if no need for decrement (ismin is for delete(), checks for special value)
            return;


        x.setKey(x.getKey()-delta); //updating key

        if (x.getParent() != null && x.getParent().isBiggerThan(x)){ //if heap invariants are broken
            FHeapNode parent = x.getParent();
            this.detach(x);
            this.addToFirst(x); //cut and add to first

            while (parent.isMark()){ //while parent is marked, detach. Cascading cuts.
                FHeapNode temp = parent.getParent();
                this.detach(parent);
                this.addToFirst(parent);
                parent = temp;
            }

            if (parent.getParent() != null) { //at this point parent isn't marked. if not root, mark.
                parent.flipMark();
                this.marked += 1;
            }
        }

        if (this.min.isBiggerThan(x)) //isBigger compares nodes with attention to the key special value in delete()
            setMin(x);

        return;
    }


    /**
     * detach (cut)
     * Addition
     * -----------------------
     * Implementation Analysis:
     * Cuts the input HeapNode from its parent, and moves it to the heap beginning.
     * Time-Complexity: O(1)
     */
    private void detach(FHeapNode x){
        totalCuts += 1; //Class field
        this.trees += 1; //Heap field
        if (x.isMark()){ //roots can't be marked
            x.flipMark();
            this.marked -= 1;
        }

        FHeapNode parent = x.getParent(); //parent-son disconnecting
        x.setParent(null);
        int k = parent.getRank();
        parent.setRank(k-1);

        if (k == 1){ //if parent has only 1 child
            parent.setChild(null);

        } else { //parent has more than one child
            if (parent.getChild() == x){ //if x is its first child, update first
                parent.setChild(x.getNext());
            }
            FHeapNode x_next = x.getNext(); //keeping children pointers
            FHeapNode x_prev = x.getPrev();
            x_next.setPrev(x_prev);
            x_prev.setNext(x_next);
        }
    }

    /**
     * public int potential()
     * This function returns the current potential of the heap, which is:
     * Potential = #trees + 2*#marked
     * In words: The potential equals to the number of trees in the heap
     * plus twice the number of marked nodes in the heap.
     * compute the potential by approaching the relevant values and return it.
     */
    public int potential() { // WC: O(1), AMORTIZED: O(1)
        // return the potential of the heap
        return getTrees() + 2 * getMarked();
    }

    /**
     * public static int totalLinks()
     * This static function returns the total number of link operations made during the
     * run-time of the program. A link operation is the operation which gets as input two
     * trees of the same rank, and generates a tree of rank bigger by one, by hanging the
     * tree which has larger value in its root under the other tree.
     * return totalLinks static object.
     */
    public static int totalLinks() { // WC: O(1), AMORTIZED: O(1)
        return totalLinks; // return totalLinks operated in the heap
    }

    /**
     * public static int totalCuts()
     * This static function returns the total number of cut operations made during the
     * run-time of the program. A cut operation is the operation which disconnects a subtree
     * from its parent (during decreaseKey/delete methods).
     * return totalCuts static object
     */
    public static int totalCuts() { // WC: O(1), AMORTIZED: O(1)
        return totalCuts; // return totalCuts operated in the heap
    }

    /**
     * Algorithm
     * 1) first creating an array of size -k-
     * 2) creating a heap-node to save all minimal optional nodes
     * 3) inserting root-node as the first min heap-node to arr
     * 4) inserting all min-node son's to the candidates heap-node
     * 5) delete-min from candidates and start over again
     * public static int[] kMin(FibonacciHeap H, int k)
     * This static function returns the k smallest elements in a Fibonacci heap that contains a single tree.
     * The function should run in O(k*deg(H)). (deg(H) is the degree of the only tree in H.)
     * ###CRITICAL### : you are NOT allowed to change H.
     * functions usesinsertList function.
     */
    public static int[] kMin(FibonacciHeap H, int k) { // WC: O(k*deg(H)),
        // AMORTIZED: O(k*deg(H)) there is no change in potential
        //// to check:: what can we assume about k
        if (H.isEmpty()) { return new int[0];}
        int[] arr = new int[k]; // min nodes array
        // candidates heap to store in all possible min nodes at each level
        FibonacciHeap candidates = new FibonacciHeap();
        arr[0] = H.min.getKey(); // setting first minimum
        insertList(candidates, H.findMin().getChild()); // inserting all minimum son's into candidates heap
        int ind = 1; // index initialize
        while (ind < k){
            FHeapNode currMin = candidates.findMin(); // find the min node from candidates
            candidates.deleteMin(); // deleting it from candidates
            arr[ind++] = currMin.getKey(); // inserting new minimum into array
            FHeapNode currMinSource = currMin.getInfo().getNode();
            if (currMinSource.getChild() != null) { // if currMinSource has no children
                insertList(candidates, currMin.getInfo().getNode().getChild()); // inserting his son's to the candidate heap
            }
        }
        return arr;
    }

    /**
     * receive a list of heap nodes, pointing at each other.
     * func. insert all nodes as single heap-node to H.
     * @param H a Fibonacci heap
     * @param m a pointer to the first heap-node of a list
     */
    private static void insertList(FibonacciHeap H, FHeapNode m){ // WC: O(n), while n - size of the list
        // AMORTIZED O(n), there is no change in potential.
        // stepper heap-node to run over all heap-nods related to m
        FHeapNode stepper = m;
        FHeapNode lastStep = m.getPrev();
        while (stepper != lastStep){
            // creating valid info-node for the current heap-node
            InfoNode stepperInfo = new InfoNode(stepper.getKey(), stepper);
            H.insert(stepper.getKey()).setInfo(stepperInfo); // insert new node to H
            stepper = stepper.getNext(); // moving on to the next heap-node
        }
        // inserting last heap-node in
        InfoNode lastInfo = new InfoNode(stepper.getKey(), stepper);
        H.insert(stepper.getKey()).setInfo(lastInfo);
    }

    FHeapNode getFirst(){
        return this.first;
    }
}
