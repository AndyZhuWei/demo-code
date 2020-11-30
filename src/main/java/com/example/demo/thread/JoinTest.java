package com.example.demo.thread;

/**
 * @Author: zhuwei
 * @Date:2020/2/1 15:02
 * @Description: 演示三个线程 T1、T2、T3分别执行。T2在T1执行完后执行，T3在T2执行完成后执行
 */
public class JoinTest {

    public static void main(String[] args) {
        /*Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("t1执行体");
            }
        });
        t1.start();

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    t1.join();
                    Thread.sleep(1000);
                    System.out.println("t2执行体");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t2.start();

        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    t2.join();
                    System.out.println("t3执行体");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        t3.start();*/

        //扩展到多个线程
        Thread previous = Thread.currentThread();
        for(int i=0;i<10;i++) {
            //每个线程拥有前一个线程的引用，需要等待前一个线程终止，才能从等待中返回
            Thread thread = new Thread(new Domino(previous),String.valueOf(i));
            thread.start();
            previous = thread;
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+" terminate.");
        }
    }
}
