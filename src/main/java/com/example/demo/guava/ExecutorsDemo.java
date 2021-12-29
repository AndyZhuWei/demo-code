package com.example.demo.guava;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.tomcat.jni.Time;

import java.util.Random;
import java.util.concurrent.*;

/**
 * @Description 使用guava提供的ThreadFactoryBuilder来创建线程池
 * @Author zhuwei
 * @Date 2021/3/15 9:46
 */
public class ExecutorsDemo {

    //使用guava构建的ThreadFactory对象，自定义线程名称，更加方便的出错的时候溯源
    private static ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("demo-pool-%d").build();

    private static ExecutorService pool = new ThreadPoolExecutor(5,200,0L,
            TimeUnit.MILLISECONDS,new LinkedBlockingDeque<>(1024),namedThreadFactory,new ThreadPoolExecutor.AbortPolicy());


    public static void main(String[] args) {
        for(int i=0;i<Integer.MAX_VALUE;i++) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            pool.execute(()->{
                System.out.format("线程：%s,值：%s",Thread.currentThread().getName(),new Random().nextInt());
                System.out.println();
            });
        }
    }


}
