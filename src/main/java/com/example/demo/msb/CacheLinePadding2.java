package com.example.demo.msb;

/**
 * @author HP
 * @Description 可以避免缓存行伪共享，（前提缓存行为64个字节大小）
 * 缓存行对齐
 * @date 2020/8/25-17:53
 */
public class CacheLinePadding2 {

    private  static long[] x = new long[16];

    public static void main(String[] args) throws InterruptedException {
        long start = System.nanoTime();
        Thread t1 = new Thread(()->{
            for(long i= 0;i<=10000_0000L;i++) {
                x[0]=i;
            }
        });

        Thread t2 = new Thread(()->{
            for(long i= 0;i<=10000_0000L;i++) {
                x[8]=i;
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println((System.nanoTime()-start)/100_0000);

    }
}
