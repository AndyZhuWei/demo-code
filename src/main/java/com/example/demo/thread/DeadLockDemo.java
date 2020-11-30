package com.example.demo.thread;

/**
 * @Author: zhuwei
 * @Date:2020/2/19 15:07
 * @Description: 死锁现象
 * 通过jstatic vmid 可以检测出死锁线程
 */
public class DeadLockDemo {

    //资源1
    private static Object resource1 = new Object();
    //资源2
    private static Object resource2 = new Object();

    public static void main(String[] args) {
        new Thread(()->{
                    synchronized (resource1) {
                        System.out.println(Thread.currentThread().getName() +" get resource1");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println(Thread.currentThread().getName()+" waiting get resource2");
                        synchronized (resource2) {
                            System.out.println(Thread.currentThread().getName()+" get resource2");
                        }

                    }
                }
        ," 线程 1").start();

        new Thread(()->{
            synchronized (resource2) {
                System.out.println(Thread.currentThread().getName() +" get resource2");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+" waiting get resource1");
                synchronized (resource1) {
                    System.out.println(Thread.currentThread().getName()+" get resource1");
                }

            }
        }
        ," 线程 2").start();
    }

}
