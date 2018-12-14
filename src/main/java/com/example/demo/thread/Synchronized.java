package com.example.demo.thread;

/**
 * @Author: zhuwei
 * @Date:2018/12/13 15:29
 * @Description: 通过使用javap工具观察Synchronized.class文件信息
 *
 * 任意线程对Object(Object由synchronized保护)的访问，首先要获得Object的监视器。如果获取失败，线程进入同步队列，线程状态变为BLOCKED。
 * 当访问Object的前驱（获得了锁的线程）释放了锁，则该释放锁操作唤醒阻塞在同步队列中的线程，使其重新尝试对监视器的获取。
 */
public class Synchronized {
    public static void main(String[] args) {
        //对Synchronized Class对象进行加锁
        //对于同步块的实现使用了monitorenter、monitorexit指令。
        synchronized(Synchronized.class) {

        }
        //静态同步方法，对Synchronized Class对象进行加锁
        m();
    }

    //同步方法依靠方法修饰符上的ACC_SYNCHRONIZED来完成，无论采用哪种方式，其本质是对一个对象的监视器（monitor）
    //进行获取，二这个获取过程是排他的。
    public static synchronized void m(){

    }
}
