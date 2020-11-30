package com.example.demo.thread;

import java.lang.ref.WeakReference;

/**
 * @author HP
 * @Description Java四种引用 强 软 弱 虚
 * 1.强
 * 2.软
 *   大对象的缓存
 *   常用对象的缓存
 * 3.弱 弱应用只要垃圾回收器看到就会回收，这个东西的作用就是如果有一个强引用指向它的时候，这个强用
 * 消失掉后，我就不用管这个弱引用了。它应该就会被回收。一般用再容器
 *  缓存：没有容器引用指向的时候就需要清除的
 *  缓存
 *   ThreadLocal
 *   WeakHashMap
 * 4.虚
 *  管理堆外内存
 * @date 2020/9/26-23:03
 *
 *
 *
 *
 *
 *
 *
 */
public class WeakReferenceTest {
    public static void main(String[] args) {
        WeakReference<M> m = new WeakReference<>(new M());

        System.out.println(m.get());
        System.gc();
        System.out.println(m.get());

        ThreadLocal<M> t1 = new ThreadLocal<>();
        t1.set(new M());

    }


}
