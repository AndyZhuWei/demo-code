package com.example.demo.msb;

import sun.misc.Contended;

/**
 * @author HP
 * @Description 缓存行伪共享（前提缓存行为64个字节）
 * @date 2020/8/25-17:53
 */
public class CacheLinePadding1 {

    //JDK1.8中添加的注解
    @Contended
    private  static long x;
    @Contended
    private  static long y;

    public static void main(String[] args) throws InterruptedException {
        long start = System.nanoTime();
        Thread t1 = new Thread(()->{
            for(long i= 0;i<=10000_0000L;i++) {
                x=i;
            }
        });

        Thread t2 = new Thread(()->{
            for(long i= 0;i<=10000_0000L;i++) {
                y=i;
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println((System.nanoTime()-start)/100_0000);

    }
}
