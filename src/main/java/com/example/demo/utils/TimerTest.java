package com.example.demo.utils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @Description
 * @Author zhuwei
 * @Date 2021/2/4 20:29
 *
 * 使用ScheduledExecutorService代替Timer
 * 因为多线程并行处理定时任务时，TImer运行多个TimerTask时，只要其中之一没有
 * 捕获抛出的异常，其他任务便会自动终止运行，使用scheduledExecutorService则没有这个问题
 *
 */
public class TimerTest {

    public static void main(String[] args) {
        Timer timer = new Timer("a");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("sss");
            }
        },1000,5000);
    }
}
