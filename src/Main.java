import LLists.Node;
import LLists.dllist;
import LLists.llist;
import Stacks_Queues.queue;
import Stacks_Queues.priorityQueue;
import Stacks_Queues.stack;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        priorityQueue<Integer> s = new priorityQueue<>(4);
        s.enqueue(1, 1);
        s.enqueue(2,4);
        s.enqueue(3,2);
        s.enqueue(4, 10);

        for(int num: s){
            System.out.println(num);
        }

        for(int num: s){
            System.out.println(num);
        }
    }
}
