package com.example.demo.utils;

import com.healthlink.unify.common.util.DateUtils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @Description TODO
 * @Author zhuwei
 * @Date 2021/2/4 20:35
 * 线程池主要有四个
 * newSingleThreadExecutor：单线程池，同时只有一个线程在跑。
 * newCachedThreadPool() ：回收型线程池，可以重复利用之前创建过的线程，运行线程最大数是Integer.MAX_VALUE。
 * newFixedThreadPool() ：固定大小的线程池，跟回收型线程池类似，只是可以限制同时运行的线程数量
 * ScheduledExecutorService 他的最大优点除了线程池的特性以外，可以实现循环或延迟任务。
 *
 *
 * ScheduledExecutorService 和 Timer 的区别
 * Timer的内部只有一个线程，如果有多个任务的话就会顺序执行，这样我们的延迟时间和循环时间就会出现问题。
 *
 * ScheduledExecutorService是线程池，所以就不会出现这个情况，在对延迟任务和循环任务要求严格的时候，就需要考虑使用ScheduledExecutorService了。
 *
 */
public class ScheduledExecutorServiceTest {

    public static void main(String[] args) {
        // 通过静态方法创建ScheduledExecutorService的实例
        ScheduledExecutorService mScheduledExecutorService = Executors.newScheduledThreadPool(4);

        // 延时任务
        mScheduledExecutorService.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("延迟任务");
            }
        }, 5, TimeUnit.SECONDS);

        // 循环任务，按照上一次任务的发起时间计算下一次任务的开始时间
        mScheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("循环任务，first："+ DateUtils.getCurrentDates());
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 1, 1, TimeUnit.SECONDS);



        // 循环任务，以上一次任务的结束时间计算下一次任务的开始时间
        mScheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                System.out.println("循环任务，scheduleWithFixedDelay："+DateUtils.getCurrentDates());
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 1, 1, TimeUnit.SECONDS);


    }
}
