package com.example.demo.thread;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.LinkedList;
import java.util.List;

/**
 * @author HP
 * @Description 虚引用,基本没用。这个是给写虚拟机的人用的.它存在的目的就是如果虚引用被回收了，会收到一个通知。通知会放到队列中也就是下班的QUEUE
 * 还可以处理堆外内存，如果检测到QUEUE，就会收到一个通知,然后处理堆外内存。见phantomReference.png
 * 需要设置jvm参数-Xms20M -Xmx20M
 * @date 2020/9/26-23:53
 */
public class PhantomReferenceTest {

    private static final List<Object> LIST = new LinkedList<>();
    private static final ReferenceQueue<M> QUEUE = new ReferenceQueue<>();

    public static void main(String[] args) {
        PhantomReference<M> phantomReference = new PhantomReference<>(new M(),QUEUE);

        new Thread(()->{
            LIST.add(new byte[1024*1024]);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
            System.out.println(phantomReference.get());
        }).start();

        new Thread(()->{
            while(true) {
                Reference<? extends M> poll = QUEUE.poll();
                if(poll != null) {
                    System.out.println("--- 虚拟机对象被jvm回收了---"+poll);
                }
            }
        }).start();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
