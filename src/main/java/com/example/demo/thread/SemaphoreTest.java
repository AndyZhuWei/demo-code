package com.example.demo.thread;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author HP
 * @Description TODO
 * @date 2020/9/24-22:59
 */
public class SemaphoreTest {

    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(2);


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    semaphore.acquire();
                    System.out.println("t1 start");
                    TimeUnit.MILLISECONDS.sleep(200);
                    System.out.println("t1 end");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }

            }
        },"t1").start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    semaphore.acquire();
                    System.out.println("t2 start");
                    TimeUnit.MILLISECONDS.sleep(200);
                    System.out.println("t2 end");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }

            }
        },"t2").start();
    }
}
