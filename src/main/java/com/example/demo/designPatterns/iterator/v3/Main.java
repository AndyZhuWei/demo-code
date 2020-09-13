package com.example.demo.designPatterns.iterator.v3;


/**
 * @author HP
 * @Description 可以替换底层的实现
 * @date 2020/9/13-16:51
 */
public class Main {

    public static void main(String[] args) {
        Collection_ list = new LinkedList_();
        for (int i = 0; i < 15; i++) {
            list.add(new String("s"+i));
        }
        System.out.println(list.size());
    }
}

//相比数组，这个容器不用考虑边界问题，可以动态扩展
class ArrayList_ implements Collection_{
    Object[] objects = new Object[10];
    //objects中下一个空的位置在哪儿，或者说目前容器中有多少个元素
    private int index = 0;
    public void add(Object o) {
        if(index == objects.length) {
            Object[] newObjects = new Object[objects.length*2];
            System.arraycopy(objects,0,newObjects,0,objects.length);
            objects = newObjects;
        }

        objects[index]=o;
        index++;
    }

    public int size(){return index;}

}

//相比数组，这个容器不用考虑边界问题，可以动态扩展
class LinkedList_ implements Collection_{
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

interface Collection_ {
    int size();
    void add(Object o);
}


