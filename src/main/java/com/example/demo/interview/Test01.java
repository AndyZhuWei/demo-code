package com.example.demo.interview;

import lombok.SneakyThrows;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author HP
 * @Description
 * @date 2020/9/25-14:02
 */
public class Test01 {

    final static Object lock = new Object();

    static List list = new ArrayList<>();

    public void add(String s) {
        list.add(s);
    }

    public int size() {
        return list.size();
    }


    public static void main(String[] args) {

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    for(int i=0;i<10;i++) {
                        list.add(i);
                        System.out.println("add,"+i);
                        if(i == 5) {
                            try {
                                lock.notify();
                                lock.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        },"t1");


        Thread t2 = new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                synchronized (lock) {

                    while(true) {
                        if(list.size() != 5) {
                            lock.wait();
                        }
                        System.out.println("t2 break");
                        break;
                    }
                    lock.notify();
                }
            }
        },"t2");
        t2.start();

        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        t1.start();






    }
}
