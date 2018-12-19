package com.example.demo.thread;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: zhuwei
 * @Date:2018/12/19 16:42
 * @Description:
 */
public class FairAndUnfairTest {
    private static Lock fairLock = new ReentrantLock2(true);
    private static Lock unfairLock = new ReentrantLock2(false);

    public static void main(String[] args) {
        //testLock(unfairLock);
        testLock(fairLock);
    }

    public static void testLock(Lock lock) {
        for(int i=0;i<5;i++) {
            new Thread(new Job(lock),""+i).start();
        }
    }

    private static class Job extends Thread {
        private Lock lock;
        public Job(Lock lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            //连续2次打印当前的Thread的同步队列中的Thread
            for(int i=0;i<2;i++) {
                lock.lock();
                try {
                    Collection<Thread> threads = ((ReentrantLock2)lock).getQueuedThreads();
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println("Lock by['"+Thread.currentThread().getName()+"'],Waiting by "+threads);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }


    private static class ReentrantLock2 extends ReentrantLock {
        public ReentrantLock2(boolean fair) {
            super(fair);
        }

        @Override
        public Collection<Thread> getQueuedThreads() {
            List<Thread> arrayList = new ArrayList<Thread>(super.getQueuedThreads());
            Collections.reverse(arrayList);
            return arrayList;
        }
    }
}
