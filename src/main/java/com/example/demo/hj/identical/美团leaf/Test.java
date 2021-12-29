package com.example.demo.hj.identical.美团leaf;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description TODO
 * @Author zhuwei
 * @Date 2021/3/31 14:21
 */
public class Test {

    private static Integer THREADS = 100;

    private ExecutorService executorService = Executors.newCachedThreadPool();

    public static void main(String[] args) {

    }


    public void testSnowFlake() {
        ConcurrentHashMap<String,String> idMap = new ConcurrentHashMap<>();
        CountDownLatch countDownLatch = new CountDownLatch(THREADS);
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                countDownLatch.countDown();
                //LeadfU

            }
        });






    }


}
