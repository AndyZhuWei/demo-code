package com.example.demo.interview;

import java.util.concurrent.TimeUnit;

/**
 * @author HP
 * @Description TODO
 * @date 2020/9/25-16:19
 */
public class Test04 {

    //flag ==0 num线程输出，否则letter输出
    static int flag = 0;

    public static void main(String[] args) {

        Object lock = new Object();


        new Thread(() -> {
            for (int i = 1; i <= 26; i++) {
                synchronized (lock) {
                    while(flag !=0 ) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.print(Thread.currentThread().getName() + "-" + i);
                    try {
                        flag = 1;
                        lock.notify();
                        TimeUnit.MILLISECONDS.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "num").start();


        new Thread(() -> {
            for (int i = 1; i <= 26; i++) {
                synchronized (lock) {
                    while(flag ==0 ) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.print(Thread.currentThread().getName() + "-" + (char)(i+96));
                    System.out.println();
                    try {
                        flag = 0;
                        lock.notify();
                        TimeUnit.MILLISECONDS.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "letter").start();


    }
}
