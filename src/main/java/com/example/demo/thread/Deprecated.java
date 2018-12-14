package com.example.demo.thread;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @Author: zhuwei
 * @Date:2018/12/13 14:40
 * @Description: 演示过期的一些线程api:suspend()\resume()\stop()
 * 之所以是过期的是因为：以suspend()为例，在调用后，线程不会释放已经占有的资源（比如锁），而是占有着资源进入睡眠状态，这样容易
 * 引发死锁问题。
 * 同样stop()方法在终结一个线程时不会保证线程的资源正常释放,通常是没有给予线程完成资源释放工作的机会，因此会导致程序可能工作在不正确状态下
 * 而暂停和恢复操作可以用等待/通知机制来代替
 */
public class Deprecated {
    public static void main(String[] args) throws Exception {

        DateFormat format  = new SimpleDateFormat("HH:mm:ss");
        Thread printThread = new Thread(new Runner(),"PrintThread");
        printThread.setDaemon(true);
        printThread.start();
        TimeUnit.SECONDS.sleep(3);
        //将PrintThrad进行暂停、输出内容工作停止
        printThread.suspend();
        System.out.println("main suspend PrintThread at "+ format.format(new Date()));
        TimeUnit.SECONDS.sleep(3);
        //将PrintThread进行恢复，输出内容继续
        printThread.resume();
        System.out.println("main resume PrintThread at "+ format.format(new Date()));
        TimeUnit.SECONDS.sleep(3);
        //将PrintThread进行终止，输出内容停止
        printThread.stop();
        System.out.println("main stop PrintThread at "+ format.format(new Date()));
        TimeUnit.SECONDS.sleep(3);




    }

    static class Runner implements Runnable {
        @Override
        public void run() {
            DateFormat format = new SimpleDateFormat("HH:mm:ss");
            while(true) {
                System.out.println(Thread.currentThread().getName()+" Run at "+format.format(new Date()));
                SleepUtils.second(1);
            }
        }
    }
}
