package com.example.demo.thread;

import java.util.concurrent.TimeUnit;

/**
 * @Author: zhuwei
 * @Date:2018/12/18 9:48
 * @Description: ThreadLocal的使用
 * 1.实际的通过ThreadLocal创建的副本是存储在每个线程自己的ThreadLocals中的
 * 2.为何threadLocals的类型ThreadLocalMap的键值为ThreadLocal对象，因为每个线程中可有多个threadLocal变量
 * 3.在进行get之前，必须先set,否则会报NPE.如果想在get之前不需要调用set就正常访问的话，必须重写initialValues方法。
 */
public class Profile {

    //第一次get()方法调用时会进行初始化(如果set方法没有调用)，每个线程会调用一次
    private static final ThreadLocal<Long> TIME_THREADLOCAL = new ThreadLocal<Long>() {
        @Override
        protected Long initialValue() {
            return System.currentTimeMillis();
        }
    };

    public static final void begin() {
        TIME_THREADLOCAL.set(System.currentTimeMillis());
    }

    public static final long end() {
        return System.currentTimeMillis()-TIME_THREADLOCAL.get();
    }

    public static void main(String[] args) throws Exception {
        Profile.begin();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("Cost: "+Profile.end()+" mills");
    }

}
