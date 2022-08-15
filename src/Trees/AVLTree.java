package Trees;


/**
 *
 * AVLTree
 * An implementation of aמ AVL Tree with
 * distinct integer keys and info.
 *
 */

public class AVLTree {
    private final IAVLNode EXTERNAL_NODE = new AVLNode(-1, null);;
    IAVLNode root;
    IAVLNode min;
    IAVLNode max;


    /**
     default constructor, O(1)
     */
    public AVLTree() {
        this.root = this.EXTERNAL_NODE;
    }

    private AVLTree(IAVLNode root){
        root.setParent(EXTERNAL_NODE);
        this.root = root;
        if (root.getSize() > 0){
            this.min = this.searchMin(root);
            this.max = this.searchMax(root);
        }
    }


    /**
     * public boolean empty()
     * Returns true if and only if the tree is empty.
     */
    public boolean empty() { //O(1)
        return !(root.isRealNode());
    }


    /**
     * public String search(int k)
     * Returns the info of an item with key k if it exists in the tree.
     * otherwise, returns null.
     */
    public String search(int k) {
        if (this.empty())
            return null;

        IAVLNode v = searchForNode(k); //fetching the node v holding the key k, if exists. O(log n)
        if (v == null) // v is not in the tree
            return null;
        return v.getValue(); // v in tree, return it's value
    }

    /**
     * special search within a key return the node who has that key
     * if there is such node, return null
     * @param k a node key
     * @return a node holding node.key == k
     */
    private IAVLNode searchForNode(int k){
        IAVLNode v = this.getRoot();
        if (v != null){
            while (v.isRealNode()){
                if (v.getKey() == k) // found k, return v
                    return v;
                if (k > v.getKey()) // go right
                    v = v.getRight();
                else // go left
                    v = v.getLeft();
            }
        }
        return null;
    }

    /**
     * public int insert(int k, String i)
     * Inserts an item with key k and info i to the AVL tree.
     * The tree must remain valid, i.e. keep its invariants.
     * Returns the number of re-balancing operations, or 0 if no re-balancing operations were necessary.
     * A promotion/rotation counts as one re-balance operation, double-rotation is counted as 2.
     * Returns -1 if an item with key k already exists in the tree.
     */
    public int insert(int k, String i) {

        IAVLNode newNode = new AVLNode(k, i);

        if (this.empty()) { //tree's empty
            this.insertToAnEmptyTree(newNode);
            return 0;
        }

        else { //tree isn't empty
            if (this.search(k) != null) //k is in the tree
                return -1;
            else { //k isn't in the tree
                this.insertToNonEmptyTree(newNode);
            }
        }

        // at this point, the new node is in the tree. Now we'll proceed to validate the tree into an AVL one:
        return rebalanceAfterInsert(newNode, false); //start-from-middle is for join, irrelevant to an ordinary insertion
    }

    private void insertToAnEmptyTree(IAVLNode newNode){
        this.root = newNode;
        this.min = newNode; //setting min and max
        this.max = newNode;
        newNode.setLeft(EXTERNAL_NODE);
        newNode.setRight(EXTERNAL_NODE);
        newNode.setParent(EXTERNAL_NODE); //parent of root is defined to be EXTERNAL NODE
        newNode.updateFields(); //updating height and rank
    }

    private void insertToNonEmptyTree(IAVLNode newNode){
        newNode.setLeft(EXTERNAL_NODE);
        newNode.setRight(EXTERNAL_NODE);
        this.updateTreeMinMax(newNode); //comparing new node's key, O(1)


        IAVLNode v = root;

        while (true){

            if (v.getKey() > newNode.getKey()){ //current key is less than v's key
                if (v.getLeft().isRealNode()){
                    v = v.getLeft();
                }


                else {//v is currently pointing to the desired parent
                    v.setLeft(newNode);
                    newNode.setParent(v);
                    newNode.updateFields();
                    break;
                }
            }
            else { //current key is greater than v's key
                if (v.getRight().isRealNode()){
                    v = v.getRight();
                }


                else { //v is currently pointing to the desired parent
                    v.setRight(newNode);
                    newNode.setParent(v);
                    newNode.updateFields();
                    break;
                }
            }
        }

        while (v.isRealNode()){ //updating sizes after insertion
            v.updateSize();
            v = v.getParent();
        }
    }

    private int rebalanceAfterInsert(IAVLNode newNode, boolean startFromMiddle) {
        IAVLNode v = newNode.getParent();
        int rebalanceSteps = 0;

        if (v.getHeight() == 0 || startFromMiddle) { //if newNode's parent was a leaf, as there are no issues when inserting to a unary node.
            while (v.isRealNode()) { //we'll climb up the tree until rebalance process is finished

                if (2 * v.getHeight() - v.getLeft().getHeight() - v.getRight().getHeight() == 1) { //promote is needed
                    rebalanceSteps++;
                    v.updateHeight();
                    v = v.getParent();
                } else { //no need for promote
                    int balanceFactor = v.getLeft().getHeight() - v.getRight().getHeight(); //using height diff to inspect which re-balance operation is needed
                    if (balanceFactor > 1 || balanceFactor < -1){ //if rotation is needed
                        rebalanceSteps += inspectRotationAndRotate_toInsert_(v, balanceFactor); //double or single rotation
                        if (this.root.getParent().isRealNode())
                            this.root = this.root.getParent();
                    }
                    break; //rotation is terminal
                }
            }
        }
        return rebalanceSteps;
    }

    private int inspectRotationAndRotate_toInsert_(IAVLNode v, int balanceFactor){

        if (balanceFactor == 2){ //right rotation or left-right
            if (v.getHeight() - v.getLeft().getLeft().getHeight() == 2) { //double rotation: left-right
                leftRotate(v.getLeft());
                rightRotate(v);
                return 5; //left rot, right rot, 2 demotes, 1 promote
            }
            else { //single right rotation
                rightRotate(v);
                return 2; // right rotation and demote
            }
        } else {
            if (balanceFactor == -2){
                if (v.getHeight() - v.getRight().getRight().getHeight() == 2){// double rotation: right-left
                    rightRotate(v.getRight());
                    leftRotate(v);
                    return 5;
                }
                else { //single left rotation
                    leftRotate(v);
                    return 2;
                }
            }
        }
        return -1; //we won't get here.
    }

    /**
     * public int delete(int k)
     * Deletes an item with key k from the binary tree, if it is there.
     * The tree must remain valid, i.e. keep its invariants.
     * Returns the number of re-balancing operations, or 0 if no re-balancing operations were necessary.
     * A promotion/rotation counts as one re-balance operation, double-rotation is counted as 2.
     * Returns -1 if an item with key k was not found in the tree.
     */

    public int delete(int k) {
        IAVLNode node = searchForNode(k); // finding node with key k
        if (node == null){ // node is not exist in tree
            return -1;
        }
        return deleteForNode(node); // delete for a certain node
    }

    /**
     delete with a pointer on IAVLNode x, assume x is in tree
     $ret = promotion/rotation operation count number
     functions uses the next co-operate functions:
     findSuccessor, findPredecessor, isLeaf, isBinary, deleteALeaf,
     deleteAUnary, re-balanceAfterDelete
     note: the functions keeps min/max value updated
     **/
    private int deleteForNode(IAVLNode x){
        if (this.size() == 1){ // x is the root and the only node in the tree
            this.root = EXTERNAL_NODE; // initial root as EXT. node
            // min and max refer to null
            this.min = null;
            this.max = null;
            return 0; // no operations needed
        }

        // if x is the min or max, update min or max
        if (min == x){min = findSuccessor(x);}
        if (max == x){max = findPredecessor(x);}

        int operations;
        if (isBinary(x)){ // if x has two sons, replace with successor, and delete successor
            IAVLNode successor = findSuccessor(x); // finding the successor
            if (successor != null){ // if successor exits
                // delete the successor node from the tree
                if (isUnary(successor)){deleteAUnary(successor);}
                else if (isLeaf(successor)){deleteALeaf(successor);}
                // replace x with the successor
                successor.setRight(x.getRight());
                successor.setLeft(x.getLeft());
                successor.setParent(x.getParent());
                x.getRight().setParent(successor);
                x.getLeft().setParent(successor);
                if (x == x.getParent().getLeft()){
                    x.getParent().setLeft(successor);
                }
                else {x.getParent().setRight(successor);}
                // checking if x is the root, if it is, then update successor to be the root
                if (x == this.root){
                    this.root = successor;
                }
                // re-balnce after delete and save operations number
                operations = rebalanceAfterDelete(successor);
                // update height and size for all parent above
                while (successor.isRealNode()){
                    successor.updateFields();
                    successor = successor.getParent();
                }
            }
            else { // successor is null, then we replace with the predecessor
                IAVLNode predecessor = findPredecessor(x); // finding predecessor
                // deleting predecessor from the tree
                assert predecessor != null;
                if (isUnary(predecessor)){deleteAUnary(predecessor);}
                else if (isLeaf(predecessor)){deleteALeaf(predecessor);}
                // replacing x with its predecessor
                predecessor.setRight(x.getRight());
                predecessor.setLeft(x.getLeft());
                predecessor.setParent(x.getParent());
                x.getRight().setParent(predecessor);
                x.getLeft().setParent(predecessor);
                if (x == x.getParent().getLeft()){
                    x.getParent().setLeft(predecessor);
                }
                else {x.getParent().setRight(predecessor);}
                if (x == this.root){ // checking weather x is the root, if it is update field
                    this.root = predecessor;
                }
                // re-balance and save operations number
                operations = rebalanceAfterDelete(predecessor);
                // update height and size for all nodes above
                while (predecessor.isRealNode()){
                    predecessor.updateFields();
                    predecessor = predecessor.getParent();
                }
            }
        }

        else if (isLeaf(x)){ // if x has no sons, replace him with EXT. node
            //replace parent left/right son (node) with EXT. node
            deleteALeaf(x);
            // re-balance after delete and save operations count
            operations = rebalanceAfterDelete(x);
            // update size and height from x.parent to above
            IAVLNode parent = x.getParent();
            while (parent.isRealNode()){
                parent.updateFields();
                parent = parent.getParent();
            }
        }

        else { // x is not binary or leaf then x is unary node
            // replace him with his only left/right son
            IAVLNode son = deleteAUnary(x);
            // checking if x is a root, if it is update field
            if (x == this.root){
                this.root = son;
            }
            // re-balance after delete and save operations count
            operations = rebalanceAfterDelete(son);
            // update size and height from son.parent to above
            IAVLNode parent = son.getParent();
            while (parent != null && parent.isRealNode()){
                parent.updateFields();
                parent = parent.getParent();
            }
        }
        return operations;
    }

    /**
     * receiving a node in the tree, assuming is a leaf and delete it
     */
    private void deleteALeaf(IAVLNode x){
        // if x is a right son
        if (x.getParent().getRight() == x){x.getParent().setRight(EXTERNAL_NODE);}
        // if x is a left son
        else {x.getParent().setLeft(EXTERNAL_NODE);}
    }
    /**
     * receiving a node in the tree, assuming is a unary node and delete it
     * function returns the node son
     */
    private IAVLNode deleteAUnary(IAVLNode x){
        if (x.getLeft().isRealNode()){ // if x has a left son
            if (x == x.getParent().getLeft()){ // if x is a left son itself
                // replacement
                x.getParent().setLeft(x.getLeft());
                x.getLeft().setParent(x.getParent());
            }
            else{ // x is a right son
                //replacement
                x.getParent().setRight(x.getLeft());
                x.getLeft().setParent(x.getParent());
            }
            return x.getLeft(); // return son
        }
        else { // if x has a right son
            if (x == x.getParent().getLeft()){ // if x is a left son
                //replacement
                x.getParent().setLeft(x.getRight());
                x.getRight().setParent(x.getParent());
            }
            else{ // x is a right son
                //replacement
                x.getParent().setRight(x.getRight());
                x.getRight().setParent(x.getParent());
            }
            return x.getRight();// return son
        }

    }

    /**
     recap next three functions:
     isBinary(node) checks whether node has two sons,
     isUnary(node) checks whether node has only one son,
     isLeaf(node) checks whether node has no sons.
     all three functions are with complexity of O(1)
     */
    private boolean isBinary(IAVLNode node){ //O(1)
        return !(this.isUnary(node) || this.isLeaf(node));
    }
    private boolean isLeaf(IAVLNode x){ //O(1)
        return x.getHeight() == 0;
    }
    private boolean isUnary(IAVLNode x){ //O(1)
        return x.getSize() == 2;
    }

    /**
     * finding the successor of x
     * @param x IAVlNode
     * @return the next bigger node after x
     */
    private IAVLNode findSuccessor(IAVLNode x){
        if (x == max){return null;} // in this case x has no successor
        IAVLNode successor = x.getRight();
        if (successor != null && successor.isRealNode()){ // x has a right son
            // go all the way down left
            while (successor != null && successor.getLeft().isRealNode()){ // while we can still turn left
                successor = successor.getLeft(); // turn left
            }
            return successor;
        }
        else { // if we can't go downward
            while (x.getParent().getLeft() != x){ // go all the way up left, and one right
                x = x.getParent(); // go up
            }
            return x.getParent();
        }
    }

    /**
     * finding the predecessor of x
     * @param x IAVLNode
     * @return the next smaller node after x
     */
    private IAVLNode findPredecessor(IAVLNode x){
        if (x == min){return null;} // in this case x has no predecessor
        IAVLNode predecessor = x.getLeft();
        if (predecessor != null && predecessor.isRealNode()){ // if x has a left son
            // go all the way down right
            while(predecessor != null && predecessor.getRight() != EXTERNAL_NODE){predecessor = predecessor.getRight();}
            return predecessor;
        }
        else{ // if we can't go downward
            // go all the way up right and one left
            while (x.getParent().getRight() != x){
                x = x.getParent(); // go up
            }
            return x.getParent();
        }
    }

    /**
     * re-balancing after deletion
     * @param node is deleted node parent.
     * @return number of operations formed on tree
     */
    private int rebalanceAfterDelete(IAVLNode node){
        if (node == null){return 0;}
        while(node!= EXTERNAL_NODE && isBalnced(node)){ // find the node who is not balanced
            node = node.getParent();
        }
        if (node == EXTERNAL_NODE){return 0;} // if all nodes are balanced
        // searching for the un-balanced case, (2 on 2)type or (3 on 1)'s type
        if(unbalanced2on2(node)){ // if node is unbalanced and from type 2 on 2
            node.setHeight(node.getHeight()-1); // demote height
            return rebalanceAfterDelete(node.getParent()) + 1; // rec-call with the parent
        }
        // if not from type 2 on 2, then must be from type 3 on 1
        switch (unbalanced3on1(node)){ // unvbalanced3on1 returns a integer from 0 to 3
            // solving under case level
            case 1:{
                // node is 3 on 1 type, and son is 1 on 1
                if (node.getHeight()-node.getLeft().getHeight() == 3){
                    leftRotate(node);
                }
                else{
                    rightRotate(node);
                }
                return 3; // one rotate plus 2 demoting
            }
            case 2:{
                // node is 3 on 1 type and son is 2 on 1 first type
                if (node.getHeight()-node.getLeft().getHeight() == 3){
                    IAVLNode tmpNode = node.getParent();
                    leftRotate(node);
                    return 3 + rebalanceAfterDelete(tmpNode);
                }
                else {
                    IAVLNode tmpNode = node.getParent();
                    rightRotate(node);
                    return 3 + rebalanceAfterDelete(tmpNode);
                }
            }
            case 3: {
                // last case
                if (node.getHeight()-node.getLeft().getHeight() == 3){
                    IAVLNode tmpNode = node.getParent();
                    rightRotate(node.getRight());
                    leftRotate(node);
                    return 6 + rebalanceAfterDelete(tmpNode);
                }
                else {
                    IAVLNode tmpNode = node.getParent();
                    leftRotate(node.getLeft());
                    rightRotate(node);
                    return 6 + rebalanceAfterDelete(tmpNode);
                }
            }
            default: return 0; // never gets here unless, tree is balanced
        }
    }

    /**
     checking for unbalanced situ. if height diff. is 2 on both sides.
     * @param node a node inside to tree
     * @return boolean type, true iff the node is unbalanced and with rank difference 2 on 2
     */
    private boolean unbalanced2on2(IAVLNode node){
        // compute rank difference from sons
        int leftDiff = node.getHeight()-node.getLeft().getHeight();
        int rightDiff = node.getHeight()-node.getRight().getHeight();
        return leftDiff == 2 && rightDiff == 2; // if the two differ with 2
    }

    /**
     check for unbalanced situ. if height diff. is 3 on 1
     return integer as to the brother sub-tree case,
     case 1: 1 on 1, case 2: 1 on 2 (2 is close to the 3 side), case 3: (2 is far from 3 side) 2 on 1.
     * @param node a node inside to tree
     * @return integer type, from 0 to 3, according to the instructions above
     */
    private int unbalanced3on1(IAVLNode node){
        // node differences
        int leftDiff = node.getHeight() - node.getLeft().getHeight();
        int rightDiff = node.getHeight() - node.getRight().getHeight();
        if(leftDiff == 3 && rightDiff == 1 ){
            // sub right tree differences
            int rleftDiff = node.getRight().getHeight() - node.getRight().getLeft().getHeight();
            int rrightDiff = node.getRight().getHeight() - node.getRight().getRight().getHeight();
            // return statement by case...
            if (rleftDiff == 1 && rrightDiff == 1){return 1;}
            else if(rleftDiff == 2 && rrightDiff == 1){return 2;}
            else{return 3;}
        }
        else if(leftDiff == 1 && rightDiff == 3 ){
            // sub left tree differences
            int lleftDiff = node.getLeft().getHeight() - node.getLeft().getLeft().getHeight();
            int lrightDiff = node.getLeft().getHeight() - node.getLeft().getRight().getHeight();
            // return statement by case...
            if (lleftDiff == 1 && lrightDiff == 1){return 1;}
            else if(lleftDiff == 1 && lrightDiff == 2){return 2;}
            else{return 3;}
        }
        return 0;
    }

    /**
     * checking if the node is balanced
     * @param node a node inside to tree
     * @return boolean type, true iff the node is balanced
     */
    private boolean isBalnced(IAVLNode node){ // simple boolean func. for checking node balance.
        int leftDiff = node.getHeight() - node.getLeft().getHeight(); // left height difference
        int rightDiff = node.getHeight() - node.getRight().getHeight(); // right height difference
        // all valid cases
        boolean case1 = leftDiff==1 && rightDiff==1;
        boolean case2 = leftDiff==1 && rightDiff==2;
        boolean case3 = leftDiff==2 && rightDiff==1;
        // return true iff one of the above is true
        return case1 || case2 || case3;
    }

    /**
     * public String min()
     *
     * Returns the info of the item with the smallest key in the tree,
     * or null if the tree is empty.
     */
    public String min() {
        if (this.empty())
            return null;
        return min.getValue();
    }

    /**
     * public String max()
     * Returns the info of the item with the largest key in the tree,
     * or null if the tree is empty.
     */
    public String max() {
        if (this.empty())
            return null;
        return max.getValue();
    }

    /**
     * public int[] keysToArray()
     * Returns a sorted array which contains all keys in the tree,
     * or an empty array if the tree is empty.
     */
    public int[] keysToArray() {
        int n = this.size();
        int[] keysArray = new int[n];
        int[] index = {0};
        keysToArrayRec(this.root, keysArray, index);

        return keysArray;
    }

    private void keysToArrayRec(IAVLNode v, int[] keysArray, int[] index){
        if (v.isRealNode()){
            keysToArrayRec(v.getLeft(), keysArray, index);
            keysArray[index[0]++] = v.getKey();
            keysToArrayRec(v.getRight(), keysArray, index);
        }
    }

    /**
     * public String[] infoToArray()
     * Returns an array which contains all info in the tree,
     * sorted by their respective keys,
     * or an empty array if the tree is empty.
     */
    public String[] infoToArray() //in order, O(n)
    {
        int n = this.size();
        String[] infoArray = new String[n];
        int[] index = {0};
        infoToArrayRec(this.root, infoArray, index);

        return infoArray;
    }

    private void infoToArrayRec(IAVLNode v, String[] infoArray, int[] index){
        if (v.isRealNode()){
            infoToArrayRec(v.getLeft(), infoArray, index);
            infoArray[index[0]++] = v.getValue();
            infoToArrayRec(v.getRight(), infoArray, index);
        }
    }

    /**
     * public int size()
     * Returns the number of nodes in the tree.
     */
    public int size() { return root.getSize(); }

    /**
     * public int getRoot()
     * Returns the root AVL node, or null if the tree is empty
     */
    public IAVLNode getRoot() {
        if(this.empty())
            return null;
        return this.root;
    }

    /**
     * public AVLTree[] split(int x)
     * splits the tree into 2 trees according to the key x.
     * Returns an array [t1, t2] with two AVL trees. keys(t1) < x < keys(t2).
     * precondition: search(x) != null (i.e. you can also assume that the tree is not empty)
     * postcondition: none
     */
    public AVLTree[] split(int x) {
        IAVLNode xNode = this.searchForNode(x);

        assert xNode != null;
        AVLTree t1 = new AVLTree(xNode.getLeft()); //initializing t1 (smaller)

        AVLTree t2 = new AVLTree(xNode.getRight()); //initializing t2 (bigger)


        int lastKey = x;
        IAVLNode keepParent; //always keeping a link to the original tree


        IAVLNode v = xNode.getParent();
        while (v.isRealNode()) {
            keepParent = v.getParent();

            if (v.getLeft().getKey() == lastKey){ //v's parent key is bigger than x
                AVLTree tempTree = new AVLTree(v.getRight());

                t2.join(v, tempTree);

            } else { //v's parent key is smaller than x
                AVLTree tempTree = new AVLTree(v.getLeft());

                t1.join(v, tempTree);
            }

            lastKey = v.getKey(); //updating last key seen
            v = keepParent; //advancing v toward the general tree's root
        }

        return (new AVLTree[]{t1, t2});
    }

    /**
     * public int join(IAVLNode x, AVLTree t)
     * joins t and x with the tree.
     * Returns the complexity of the operation (|tree.rank - t.rank| + 1).
     * precondition: keys(t) < x < keys() or keys(t) > x > keys(). t/tree might be empty (rank = -1).
     * postcondition: none
     */
    public int join(IAVLNode x, AVLTree t)
    {
        if (t.empty() || this.empty()){ //if at least one of the trees is empty, O(1)
            return joinWithEmptyTree(x, t);
        }
        else { //both trees aren't empty
            setThisTreeToBeTheTallest(t); //O(1), swapping the trees.
        }

        int heightDiff = this.getRoot().getHeight() - t.getRoot().getHeight();

        if (this.getRoot().getKey() < x.getKey()) //this tree carries the smaller key set
            joinBiggerNonEmptyTree(x,t); //O(logn), ordinary join
        else
            joinSmallerNonEmptyTree(x,t); //O(logn)

        return Math.abs(heightDiff) + 1;
    }

    private int joinWithEmptyTree(IAVLNode x, AVLTree t) {
        if (this.empty()) { //if this tree is the empty
            if (t.empty()) { //if both are empty
                this.root = x;
                this.max = x;
                this.min = x;
                x.setParent(EXTERNAL_NODE);
                x.setRight(EXTERNAL_NODE);
                x.setLeft(EXTERNAL_NODE);
                x.updateFields();
                return 1;
            } else { //if only the current tree is empty
                this.root = t.getRoot(); //swapping this tree to be the non empty one
                this.max = t.max;
                this.min = t.min;
            }
        }
        //at this point, current tree isn't empty
        insertToNonEmptyTree(x); //regular insertion, O(logn)
        rebalanceAfterInsert(x, false);

        return t.getRoot().getHeight() +2;
    }


    private void setThisTreeToBeTheTallest(AVLTree t){
        if (t.getRoot().getHeight() > this.getRoot().getHeight()){ //if t is taller than this
            IAVLNode tempRoot = this.getRoot();
            IAVLNode tempMax = this.max;
            IAVLNode tempMin = this.min;

            this.root = t.root;
            this.min = t.min;
            this.max = t.max;

            t.root = tempRoot;
            t.max = tempMax;
            t.min = tempMin;
        }
    }

    //Assumption - this.Height() >= t.Height(), t keys are bigger, both trees aren't empty
    //O(|height diff| + 1)
    private void joinBiggerNonEmptyTree(IAVLNode x, AVLTree t){
        int k = t.getRoot().getHeight();
        IAVLNode v = this.getRoot();

        while (v.getHeight() > k) //fist node with rank <= k
            v = v.getRight();

        //fields maintenance
        x.setParent(v.getParent());
        x.setRight(t.getRoot());
        x.setLeft(v);
        this.setAParentToPointOverB(v,x);
        x.updateFields();
        t.getRoot().setParent(x);
        v.setParent(x);

        if (this.root.getParent().isRealNode()) //root pointer maintenance
            this.root = this.root.getParent();

        //starting rebalancing from x.
        if (x.getParent().isRealNode()){ //else, x is the root and rebalncing won't be needed.
            if (v.getHeight() == k) { //one-one edge case after join
                leftRotate(x.getParent());
            }
            else {
                rebalanceAfterInsert(x, true); //rebalancing starting from x, up to the root
            }
            if (this.root.getParent().isRealNode())
                this.root = this.root.getParent();
        }

        this.max = t.max;
    }

    //comments are equals to joinBiggerNonEmptyTree func.
    private void joinSmallerNonEmptyTree(IAVLNode x, AVLTree t){ //Assumption - this.Height() >= t.Height(), t keys are smaller, both trees aren't empty
        int k = t.getRoot().getHeight();
        IAVLNode v = this.getRoot();

        while (v.getHeight() > k)
            v = v.getRight();

        x.setParent(v.getParent());
        x.setLeft(t.getRoot());
        x.setRight(v);
        this.setAParentToPointOverB(v,x);
        x.updateFields();
        t.getRoot().setParent(x);
        v.setParent(x);
        if (this.root.getParent().isRealNode())
            this.root = this.root.getParent();

        if (x.getParent().isRealNode()){ //else, x is the root and rebalncing won't be needed.
            if (v.getHeight() == k) { //one-one edge case after join
                rightRotate(x.getParent());
            }
            else {
                rebalanceAfterInsert(x, true); //rebalancing starting from x, up to the root
            }
            if (this.root.getParent().isRealNode())
                this.root = this.root.getParent();
        }

        this.min = t.min;
    }

    private void updateTreeMinMax(IAVLNode v){
        int k = v.getKey();
        if (this.size() > 0){
            if (k > max.getKey())
                max = v;
            else if (k < min.getKey())
                min = v;
        } else {
            min = v;
            max = v;
        }
    }

    private IAVLNode searchMax(IAVLNode root) {
        if (root.getSize() > 0){
            IAVLNode v = root;
            while (v.getRight().isRealNode())
                v = v.getRight();
            return v;
        }
        return null;
    }

    private IAVLNode searchMin(IAVLNode root) {
        if (root.getSize() > 0){
            IAVLNode v = root;
            while (v.getLeft().isRealNode())
                v = v.getLeft();
            return v;
        }
        return null;
    }

    private void rightRotate(IAVLNode y){ //input is subtree's *root*
        IAVLNode x = y.getLeft();
        IAVLNode t2 = x.getRight(); //subtree's are denoted in t'i

        if (this.root == y)
            this.root = x;

        //promoting x to be the new root:
        x.setParent(y.getParent());
        this.setAParentToPointOverB(y,x);
        x.setRight(y);

        //right rotation to y
        y.setParent(x);
        y.setLeft(t2);
        y.updateFields();
        x.updateFields();

        t2.setParent(y);
    }

    private void leftRotate(IAVLNode x){ //input is subtree's *root*
        IAVLNode y = x.getRight();
        IAVLNode t2 = y.getLeft(); //subtrees are denoted in t'i

        if (this.root == x)
            this.root = y;

        //promoting y to be the new root:
        y.setParent(x.getParent());
        this.setAParentToPointOverB(x,y);
        y.setLeft(x);

        //left rotation to x
        x.setParent(y);
        x.setRight(t2);
        x.updateFields();
        y.updateFields();

        t2.setParent(x);

    }

    //O(1), only a few assignations
    private void setAParentToPointOverB(IAVLNode a, IAVLNode b){
        if (a.getParent().isRealNode()){
            IAVLNode parent = a.getParent();
            if (parent.getRight() == a)
                parent.setRight(b);
            else
                parent.setLeft(b);
        }
    }


    /**
     * public interface IAVLNode
     * ! Do not delete or modify this - otherwise all tests will fail !
     */
    public interface IAVLNode{
        public int getKey(); // Returns node's key (for virtual node return -1).
        public String getValue(); // Returns node's value [info], for virtual node returns null.
        public void setLeft(IAVLNode node); // Sets left child.
        public IAVLNode getLeft(); // Returns left child, if there is no left child returns null.
        public void setRight(IAVLNode node); // Sets right child.
        public IAVLNode getRight(); // Returns right child, if there is no right child return null.
        public void setParent(IAVLNode node); // Sets parent.
        public IAVLNode getParent(); // Returns the parent, if there is no parent return null.
        public boolean isRealNode(); // Returns True if this is a non-virtual AVL node.
        public void setHeight(int height); // Sets the height of the node.
        public int getHeight(); // Returns the height of the node (-1 for virtual nodes).
        public int getSize();
        public void updateSize();
        public void updateHeight();
        public void updateFields();
    }

    /**
     * public class AVLNode
     * If you wish to implement classes other than AVLTree
     * (for example AVLNode), do it in this file, not in another file.
     * This class can and MUST be modified (It must implement IAVLNode).
     */
    public static class AVLNode implements IAVLNode{ //all methods take O(1).

        private int key;
        private String value;
        private int size = 0;
        private int height = -1; //AVL node's rank ==  its height

        private IAVLNode parent;
        private IAVLNode left;
        private IAVLNode right;


        public AVLNode(int key, String value){
            this.key = key;
            this.value = value;
        }

        @Override
        public int getKey() { return this.key; }

        @Override
        public String getValue() { return this.value; }

        @Override
        public void setLeft(IAVLNode node) { this.left = node; }

        @Override
        public IAVLNode getLeft() { return this.left; }

        @Override
        public void setRight(IAVLNode node) { this.right = node; }

        @Override
        public IAVLNode getRight() { return this.right; }

        @Override
        public void setParent(IAVLNode node) { if (this.isRealNode()) this.parent = node; }

        @Override
        public IAVLNode getParent() { return this.parent; }

        @Override
        public boolean isRealNode() { return (this.getValue() != null); }

        @Override
        public void setHeight(int height) { this.height = height; } //ERROR PRONE

        @Override
        public int getHeight() { return this.height; }

        @Override
        public int getSize() { return this.size; }

        @Override
        public void updateSize() {
            if ( isRealNode() )
                this.size = right.getSize() + left.getSize() + 1;
        }

        @Override
        public void updateHeight(){
            if (isRealNode())
                this.setHeight( Math.max(this.getLeft().getHeight(), this.getRight().getHeight()) + 1);
        }

        @Override
        public void updateFields(){
            this.updateHeight();
            this.updateSize();
        }

    }

}
