package com.example.demo.thread;

import java.util.concurrent.TimeUnit;

/**
 * @Author: zhuwei
 * @Date:2018/12/18 9:29
 * @Description: Thread.join的使用
 * 如果一个线程A执行了thread.join语句，其含义是：当前线程A等待thread线程
 * 终止之后才从thread.join返回。
 * 这里涉及了等待/通知机制（等待前驱线程结束，接收前驱线程结束通知）
 *
 */
public class Join {

    public static void main(String[] args) throws Exception{
        Thread previous = Thread.currentThread();
        for(int i=0;i<10;i++) {
            //每个线程拥有前一个线程的引用，需要等待前一个线程终止，才能从等待中返回
            Thread thread = new Thread(new Domino(previous),String.valueOf(i));
            thread.start();
            previous = thread;
        }
        TimeUnit.SECONDS.sleep(5);
        System.out.println(Thread.currentThread().getName()+" terminate.");

    }

    static class Domino implements Runnable {
        private Thread thread;
        public Domino(Thread thread) {
            this.thread = thread;
        }

        @Override
        public void run() {
            try {
                thread.join();
            } catch (InterruptedException e) {
            }

            System.out.println(Thread.currentThread().getName()+" terminate." );
        }
    }

}
