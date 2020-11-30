package com.example.demo.interview;

import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author HP
 * @Description TODO
 * @date 2020/9/25-14:57
 */
public class Test02 {

    //容量固定在10
    static List<String> list = new ArrayList<>();

    static Object lock  = new Object();


    public static void main(String[] args) {

        for(int i=0;i<8;i++) {
            new Thread(new Producer()).start();
        }

        for(int i=0;i<2;i++) {
            new Thread(new Consumer()).start();
        }
    }



    static class Producer implements Runnable {
        @SneakyThrows
        @Override
        public void run() {
            while(true) {
                synchronized (lock) {
                    while(list.size() == 10) {
                        System.out.println(Thread.currentThread().getName()+","+"已经满了，休息");
                        try {
                            lock.notifyAll();
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    TimeUnit.MILLISECONDS.sleep(1000);
                    list.add("1");
                    System.out.println(Thread.currentThread().getName()+",已经添加了,目前："+list.size()+"个");

                }
            }

        }
    }

    static class Consumer implements Runnable {
        @SneakyThrows
        @Override
        public void run() {
            while(true) {
                synchronized (lock) {
                    while(list.size() == 0) {
                        System.out.println(Thread.currentThread().getName()+","+"没有了，休息");
                        try {
                            lock.notifyAll();
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    TimeUnit.MILLISECONDS.sleep(1000);
                    list.remove(list.size()-1);
                    System.out.println(Thread.currentThread().getName()+",已经消耗1个,目前："+list.size()+"个");
                }
            }

        }
    }
}
