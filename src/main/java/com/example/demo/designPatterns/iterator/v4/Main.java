package com.example.demo.designPatterns.iterator.v4;


/**
 * @author HP
 * @Description 如果对容器进行遍历, 添加一个Iterator接口
 * @date 2020/9/13-16:51
 */
public class Main {

    public static void main(String[] args) {
        Collection_ list = new LinkedList_();
        for (int i = 0; i < 15; i++) {
            list.add(new String("s"+i));
        }
        System.out.println(list.size());
        //这个接口的调用方式
        Iterator_ it = list.iterator();
        while(it.hasNext()) {
            System.out.println(it.next());
        }
    }
}

//相比数组，这个容器不用考虑边界问题，可以动态扩展
class ArrayList_ implements Collection_ {
    Object[] objects = new Object[10];
    //objects中下一个空的位置在哪儿，或者说目前容器中有多少个元素
    private int index = 0;

    public void add(Object o) {
        if (index == objects.length) {
            Object[] newObjects = new Object[objects.length * 2];
            System.arraycopy(objects, 0, newObjects, 0, objects.length);
            objects = newObjects;
        }

        objects[index] = o;
        index++;
    }

    public int size() {
        return index;
    }

    @Override
    public Iterator_ iterator() {
        return new ArrayListIterator();
    }

    private class ArrayListIterator implements Iterator_ {

        private int currentIndex = 0;


        @Override
        public boolean hasNext() {
            if (currentIndex >= index) return false;
            return true;
        }

        @Override
        public Object next() {
            Object o = objects[currentIndex];
            currentIndex++;
            return o;
        }
    }
}

//相比数组，这个容器不用考虑边界问题，可以动态扩展
class LinkedList_ implements Collection_ {
    Node head = null;
    Node tail = null;
    //目前容器中有多少个元素
    private int size = 0;

    public void add(Object o) {
        Node n = new Node(o);
        n.next = null;

        if (head == null) {
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

        public Node(Object o) {
            this.o = o;
        }
    }

    public int size() {
        return size;
    }

    @Override
    public Iterator_ iterator() {
        return new LinkedListIterator();
    }

    private class LinkedListIterator implements Iterator_ {

        private int currentIndex = 0;


        @Override
        public boolean hasNext() {
            if (currentIndex >= size) return false;
            return true;
        }

        @Override
        public Object next() {
            if(currentIndex == 0) {
                currentIndex++;
                return head.o;
            }

            int nextTime = 0;
            Node n = head;

            while(n.next != null) {
                ++nextTime;
                if(currentIndex == nextTime) {
                    currentIndex++;
                    return n.next.o;
                }
                n = n.next;
            }
            return null;
        }
    }
}

interface Collection_ {
    int size();

    void add(Object o);

    Iterator_ iterator();
}

interface Iterator_ {
    boolean hasNext();

    Object next();
}


