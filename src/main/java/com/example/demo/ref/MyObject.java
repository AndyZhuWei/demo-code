package com.example.demo.ref;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;

/**
 * @Author zhuwei
 * @Date 2020/7/30 9:09
 * @Description: 软引用实例
 */
public class MyObject {

    public static ReferenceQueue softQueue;

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        Reference<MyObject> obj = null;
        try {
            obj = (Reference<MyObject>)softQueue.remove();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(obj);
        if(obj!=null) {
            System.out.println("Object for SoftReference is"+obj.get());
        }
        //只有当GC回收时才会输出
        System.out.println("MyObject's finalize called");
    }

    @Override
    public String toString() {
        return "I am MyObject";
    }

    public static void main(String[] args) {
        //强引用
        MyObject obj = new MyObject();
        //创建引用那个队列
        softQueue = new ReferenceQueue<MyObject>();
        //创建软引用
        SoftReference<MyObject> softRef = new SoftReference<MyObject>(obj,softQueue);
        System.out.println(obj);
        //删除强引用
        obj = null;
        System.gc();
        System.out.println(obj);
        System.out.println("After GC:Soft Get="+softRef.get());
        System.out.println("分配大块内存");
        //分配一块较大内存区，强迫GC
        byte[] b = new byte[1000*1024*925];
        System.out.println("After new byte[]:Soft Get="+softRef.get());
        System.out.println(obj);
    }
}
