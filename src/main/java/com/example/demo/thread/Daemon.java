package com.example.demo.thread;

/**
 * @Author: zhuwei
 * @Date:2018/12/13 11:45
 * @Description: Daemon线程是一种支持性线程。但是在Java虚拟机退出时Daemon
 * 线程中的finally块并不一定会执行
 */
public class Daemon {

    public static void main(String[] args) {
        Thread thread = new Thread(new DaemonRunner(),"DaemonRunner");
        //Daemon属性需要在启动之前设置，不能在启动之后设置
        thread.setDaemon(true);
        thread.start();
    }

    static class DaemonRunner implements Runnable {
        @Override
        public void run() {
            try {
                SleepUtils.second(10);
            } finally {
                System.out.println("DaemonThread finally run.");
            }
        }
    }
}
