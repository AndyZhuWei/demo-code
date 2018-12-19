package com.example.demo.thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @Author: zhuwei
 * @Date: 2018/12/19 13:26
 * @Description:
 * 示例总结：
 * Thread.interrupted()调用返回值后会清理状态标识
 * thread.isInterrupted()不会清理状态标识
 *
 */
public class Interrupted2 {
    public static void main(String[] args) throws Exception{
       // test1();

//        test2();

//        test3();

//        test4();

//        test5();

//        test6();
    }

    public static void test1() {
        Thread thread = new Thread(new BusyRunner(),"BusyRunner");
        thread.start();
        thread.interrupt();
        System.out.println("第一次调用返回值："+Thread.interrupted());//第一次调用返回值：false
        System.out.println("第二次调用返回值："+Thread.interrupted());//第二次调用返回值：false
        System.out.println("===================end==============");
    }

    public static void test2() {
        Thread thread = new Thread(new BusyRunner(),"BusyRunner");
        thread.start();
        thread.interrupt();
        Thread.currentThread().interrupt();
        System.out.println("第一次调用返回值："+Thread.interrupted());//第一次调用返回值：true
        System.out.println("第二次调用返回值："+Thread.interrupted());//第二次调用返回值：false
        System.out.println("===================end==============");
    }

    public static void test3() {
        Thread thread = new Thread(new BusyRunner(),"BusyRunner");
        thread.start();
        thread.interrupt();
        System.out.println("第一次调用返回值："+thread.isInterrupted());//第一次调用返回值：true
        System.out.println("第二次调用返回值："+thread.isInterrupted());//第二次调用返回值：true
        System.out.println("===================end==============");
    }


    public static void test4() {
        Thread thread = new Thread(new BusyRunner(),"BusyRunner");
        thread.start();
        thread.interrupt();
        System.out.println("第一次调用返回值："+Thread.currentThread().isInterrupted());//第一次调用返回值：false
        System.out.println("第二次调用返回值："+Thread.currentThread().isInterrupted());//第二次调用返回值：false
        System.out.println("===================end==============");
    }

    public static void test5() {
        Thread thread = new Thread(new BusyRunner(),"BusyRunner");
        thread.start();
        Thread.currentThread().interrupt();
        System.out.println("第一次调用返回值："+Thread.currentThread().isInterrupted());//第一次调用返回值：true
        System.out.println("第二次调用返回值："+Thread.currentThread().isInterrupted());//第二次调用返回值：true
        System.out.println("===================end==============");
    }

    public static void test6() throws InterruptedException {
        Thread thread = new Thread(new LockSupportRunner(),"LockSupportRunner");
        thread.start();
        TimeUnit.SECONDS.sleep(1);
        thread.interrupt();
    }



    static class BusyRunner implements Runnable {
        @Override
        public void run() {
            while(true){
            }
        }
    }



    static class LockSupportRunner implements Runnable {
        @Override
        public void run() {
            while(true){
                System.out.println("LockSupport start");
                LockSupport.park(this);
                //TODO 这里如果把Thread.interrupted()注释掉下次LockSupport.park(this)将不会阻塞当前线程
                //原因有待查证
               // System.out.println("第一次调用中断状态："+Thread.interrupted());//第一次调用中断状态：true
                //System.out.println("第二次调用中断状态："+Thread.interrupted());//第二次调用中断状态：false
                System.out.println("LockSupport end");
            }
        }
    }
}
