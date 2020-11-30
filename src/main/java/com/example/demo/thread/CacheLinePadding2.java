package com.example.demo.thread;

/**
 * @Author: zhuwei
 * @Date:2020/4/30 16:40
 * @Description:
 */
public class CacheLinePadding2 {

    //arr[0]和arr[8]它们不会存在于同一缓存行
    //缓存行是64字节 arr[0]+...+arr[7]就已经是64字节了
    public static volatile long[] arr = new long[16];

    public static void main(String[] args) throws Exception{
        Thread t1 = new Thread(()->{
            for(long j=0;j<10_0000_0000;j++) {
                arr[0]=j;
            }
        });

        Thread t2 = new Thread(()->{
            for(long j=0;j<10_0000_0000;j++) {
                arr[8]=j;
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
