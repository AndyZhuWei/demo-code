package com.example.demo.thread;

import java.io.IOException;
import java.util.concurrent.locks.Lock;


public class TwinsLockTest {

    public static final Lock lock = new TwinsLock();

    public static void main(String[] args) throws IOException {
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
