package com.example.demo.thread;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;

/**
 * @Author: zhuwei
 * @Date:2018/9/18 11:33
 * @Description:
 */
public class CyclicBarrierTest2 {
    public static void main(String[] args) {

        CyclicBarrier barrier = new CyclicBarrier(20, new Runnable() {
            @Override
            public void run() {
                System.out.println("满员，发车");
            }
        });

        for (int i=0; i < 100; i++) {
            Executors.newCachedThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        barrier.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }
}

