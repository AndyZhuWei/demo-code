package com.example.demo.interview;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author HP
 * @Description TODO
 * @date 2020/9/25-15:35
 */
public class Test05<T> {

    final private LinkedList<T> lists = new LinkedList<>();
    final private int MAX = 10;
    private int count = 0;
    private static Lock lock = new ReentrantLock();
    private Condition notFull = lock.newCondition();
    private Condition notEmpty = lock.newCondition();
    


    public static void main(String[] args) {





        Test05<String> c = new Test05<>();
        for(int i=0;i<10;i++) {
            new Thread(()->{
                for(int j=0;j<5;j++) {
                    System.out.println(c.get());
                }
            },"c"+i).start();
        }

        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for(int i=0;i<2;i++) {
            new Thread(()->{
                for(int j=0;j<25;j++) {
                    c.put(Thread.currentThread().getName()+" "+j);
                }
            },"p"+i).start();
        }

    }

    public void put(T t) {
        lock.lock();
        try {
            while(lists.size() == MAX) {
                try {
                    notEmpty.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            notFull.signalAll();
            lists.add(t);
            ++count;
        } finally {
            lock.unlock();
        }

    }





    public T get()  {
        T t = null;
        lock.lock();
        try {
            while (lists.size() == 0) {
                try {
                    notFull.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            notEmpty.signalAll();
            t = lists.removeFirst();
            count --;
            return t;
        } finally {
            lock.unlock();
        }



    }
}
