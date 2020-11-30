package com.example.demo.designPatterns.iterator.v6;


/**
 * @author HP
 * @Description 实现泛型版本
 * @date 2020/9/13-16:51
 */
public class Main {

    public static void main(String[] args) {
        Collection_<String> list = new LinkedList_<>();
        for (int i = 0; i < 15; i++) {
            list.add(new String("s"+i));
        }
        System.out.println(list.size());
        //这个接口的调用方式
        Iterator_<String> it = list.iterator();
        while(it.hasNext()) {
            String next = it.next();
            System.out.println(next);
        }
    }
}

//相比数组，这个容器不用考虑边界问题，可以动态扩展
class ArrayList_<E> implements Collection_<E> {
    E[] objects = (E[])new Object[10];
    //objects中下一个空的位置在哪儿，或者说目前容器中有多少个元素
    private int index = 0;

    public void add(E o) {
        if (index == objects.length) {
            E[] newObjects = (E[])new Object[objects.length * 2];
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
class LinkedList_<E> implements Collection_<E> {
    Node<E> head = null;
    Node<E> tail = null;
    //目前容器中有多少个元素
    private int size = 0;

    public void add(E o) {
        Node<E> n = new Node(o);
        n.next = null;

        if (head == null) {
            head = n;
            tail = n;
        }

        tail.next = n;
        tail = n;
        size++;
    }

    private class Node<E> {
        private E o;
        Node<E> next;

        public Node(E o) {
            this.o = o;
        }
    }

    public int size() {
        return size;
    }

    @Override
    public Iterator_<E> iterator() {
        return new LinkedListIterator();
    }

    private class LinkedListIterator implements Iterator_<E>{

        private int currentIndex = 0;


        @Override
        public boolean hasNext() {
            if (currentIndex >= size) return false;
            return true;
        }

        @Override
        public E next() {
            if(currentIndex == 0) {
                currentIndex++;
                return head.o;
            }

            int nextTime = 0;
            Node<E> n = head;

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

interface Collection_<E> {
    int size();

    void add(E o);

    Iterator_<E> iterator();
}

interface Iterator_<E> {
    boolean hasNext();

    E next();
}


