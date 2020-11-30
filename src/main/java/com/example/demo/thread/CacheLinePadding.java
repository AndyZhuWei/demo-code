package com.example.demo.thread;

/**
 * @Author: zhuwei
 * @Date:2020/4/30 16:40
 * @Description:
 */
public class CacheLinePadding {

    //arr[0]和arr[1]都是8字节，所以它们有可能在同一个缓存行
    public static volatile long[] arr = new long[2];

    public static void main(String[] args) throws Exception{
        Thread t1 = new Thread(()->{
            for(long j=0;j<10_0000_0000;j++) {
                arr[0]=j;
            }
        });

        Thread t2 = new Thread(()->{
            for(long j=0;j<10_0000_0000;j++) {
                arr[1]=j;
            }
        });
        final long start = System.nanoTime();
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println((System.nanoTime()-start)/100_0000);
    }
}
