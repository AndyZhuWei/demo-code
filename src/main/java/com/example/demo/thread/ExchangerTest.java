package com.example.demo.thread;

import java.util.concurrent.Exchanger;

/**
 * @author HP
 * @Description TODO
 * @date 2020/9/24-23:02
 */
public class ExchangerTest {

    public static void main(String[] args) {
        Exchanger<String> exchager = new Exchanger<>();

        new Thread(new Runnable() {
            String s = "s1";
            @Override
            public void run() {
                try {
                    s = exchager.exchange(s);
                    System.out.println(Thread.currentThread().getName()+" "+s);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"t1").start();

        new Thread(new Runnable() {
            String s = "s2";
            @Override
            public void run() {
                try {
                    s = exchager.exchange(s);
                    System.out.println(Thread.currentThread().getName()+" "+s);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"t2").start();
    }
}
