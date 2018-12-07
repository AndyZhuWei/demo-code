package com.example.demo.thread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: zhuwei
 * @Date:2018/9/18 11:54
 * @Description:
 */
public class CountDownLatchTest {
    public static void main(String[] args) throws InterruptedException {
        AtomicInteger count = new AtomicInteger(0);
        int taskNum = 10;
        CountDownLatch ready_latch = new CountDownLatch(taskNum);
        CountDownLatch begin_latch = new CountDownLatch(1);
        CountDownLatch end_latch = new CountDownLatch(taskNum);
        Executor executor = Executors.newCachedThreadPool();
        System.out.println("主线程-->开始分发任务...");
        for (int i = 1; i <= taskNum; i++) {
            Task task = new Task(String.valueOf(i), ready_latch, begin_latch, end_latch, count);
            executor.execute(task);
        }
        System.out.println("主线程-->等待所有子任务准备就绪。。。");
        ready_latch.await();
        System.out.println("主线程-->所有子任务已经准备就绪，通知子任务执行。。。");
        begin_latch.countDown();
        end_latch.await();
        System.out.println("主线程-->所有子任务执行完毕，获得结果:" + count.get());

    }
}

class Task implements Runnable {


    private String taskCode;

    private AtomicInteger count;

    private CountDownLatch ready_latch;

    private CountDownLatch begin_latch;

    private CountDownLatch end_latch;

    public Task(String taskCode, CountDownLatch ready_latch, CountDownLatch begin_latch, CountDownLatch end_latch, AtomicInteger count) {
        super();
        this.taskCode = taskCode;
        this.begin_latch = begin_latch;
        this.end_latch = end_latch;
        this.ready_latch = ready_latch;
        this.count = count;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
            System.out.println("子线程：任务" + taskCode + " 准备就绪。。。");
            ready_latch.countDown();//已准备的任务+1
            begin_latch.await();//等待开始信号
            System.out.println("子线程：任务" + taskCode + " 开始执行。。。");
            count.addAndGet(Integer.valueOf(taskCode));
            Thread.sleep(1000);
            System.out.println("子线程：任务" + taskCode + " 执行完成。。。");

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            end_latch.countDown();//已完成的任务+1
        }
    }
}
