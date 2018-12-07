package com.example.demo.thread;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: zhuwei
 * @Date:2018/9/18 11:33
 * @Description:
 */
public class CyclicBarrierTest {
    public static void main(String[] args) {

        //如果将参数改为4，但是下面只是加入了3个选手，这将永远等等下去。
        CyclicBarrier barrier = new CyclicBarrier(3);

        ExecutorService executor = Executors.newFixedThreadPool(3);
        executor.submit(new Thread(new Runner(barrier,"1号选手")));
        executor.submit(new Thread(new Runner(barrier,"2号选手")));
        executor.submit(new Thread(new Runner(barrier,"3号选手")));

        executor.shutdown();
    }
}

class Runner implements Runnable {

    //一个同步辅助类，它允许一组线程相互等待，直到到达某个公共屏障点(common barrier point)
    private CyclicBarrier barrier;

    private String name;

    public Runner(CyclicBarrier barrier,String name) {
        super();
        this.barrier = barrier;
        this.name = name;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000*(new Random().nextInt(80)));
            System.out.println(name+"准备好了....");
            //barrier的await方法，在所有参与者都已经在此barrier上调用await方法之前，将一直等待
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        System.out.println(name+" 起包！");
    }
}
