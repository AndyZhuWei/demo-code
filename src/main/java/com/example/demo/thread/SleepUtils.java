package com.example.demo.thread;

import java.util.concurrent.TimeUnit;

/**
 * @Author: zhuwei
 * @Date:2018/12/13 11:24
 * @Description:
 */
public class SleepUtils {
    public static final void second(long seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e){
        }
    }
}
