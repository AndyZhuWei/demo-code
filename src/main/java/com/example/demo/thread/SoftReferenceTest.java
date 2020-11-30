package com.example.demo.thread;

import java.lang.ref.SoftReference;

/**
 * @author HP
 * @Description 做这个实验需要设置一下JVM的堆大小
 * -Xms20M -Xmx20M
 * 软引用只要堆空间不够了才会回收软引用，它的生成周期相对还是长一些
 * @date 2020/9/26-23:13
 */
public class SoftReferenceTest {
    public static void main(String[] args) {
        SoftReference<byte[]> m = new SoftReference<>(new byte[1024*1024*10]);

        System.out.println(m.get());
        System.gc();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(m.get());

        //再分配一个数组，heap将装不下，这时候系统会垃圾回收，先回收一次，如果不够，会把软引用干掉
        byte[] b = new byte[1024*1024*10];
        System.out.println(m.get());
    }
}
