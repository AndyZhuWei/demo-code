package com.example.demo.designPatterns.iterator.v2;


/**
 * @author HP
 * @Description TODO
 * @date 2020/9/13-17:05
 */
public class Main {

    public static void main(String[] args) {
        LinkedList list = new LinkedList();
        for (int i = 0; i < 15; i++) {
            list.add(new String("s"+i));
        }
        System.out.println(list.size());
    }

}

//相比数组，这个容器不用考虑边界问题，可以动态扩展
class LinkedList {
    Node head = null;
    Node tail = null;
    //目前容器中有多少个元素
    private int size = 0;

    public void add(Object o) {
        Node n = new Node(o);
        n.next = null;

        if(head == null) {
            head = n;
            tail = n;
        }

        tail.next = n;
        tail = n;
        size++;
    }

    private class Node {
        private Object o;
        Node next;

        public Node(Object o){this.o=o;}
    }

    public int size() {
        return size;
    }
}
