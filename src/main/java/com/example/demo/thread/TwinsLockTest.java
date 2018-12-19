package com.example.demo.thread;

import java.util.concurrent.locks.Lock;

/**
 * @Author: zhuwei
 * @Date:2018/12/19 15:18
 * @Description:
 */
public class TwinsLockTest {

    public static final Lock lock = new TwinsLock();

    public static void main(String[] args) {
        //启动10个线程
        for(int i=0;i<10;i++) {
            Thread thread  = new Thread(new Worker(),"WorkerThread-"+i);
            thread.setDaemon(true);
            thread.start();
        }
        //每隔1秒换行
        for(int i=0;i<10;i++) {
            SleepUtils.second(1);
            System.out.println();
        }
    }

    static class Worker implements Runnable {
        @Override
        public void run() {
            while(true) {
                lock.lock();
                try {
                    SleepUtils.second(1);
                    System.out.println(Thread.currentThread().getName());
                    SleepUtils.second(1);
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}
