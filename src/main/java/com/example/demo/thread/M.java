package com.example.demo.thread;

/**
 * @author HP
 * @Description TODO
 * @date 2020/9/26-23:10
 */
public class M {
    //垃圾回收会自动调用这个方法
    @Override
    protected void finalize() throws Throwable {
        System.out.println("finalize");
    }
}
