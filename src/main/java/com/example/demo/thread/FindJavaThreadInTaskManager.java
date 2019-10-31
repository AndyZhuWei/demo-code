package com.example.demo.thread;

/**
 * @Author: zhuwei
 * @Date:2019/10/22 11:03
 * @Description: 运用jstack查找CPU利用率高的问题
 * 加入程序部署在linux环境中
 * 步骤：
 * 1.使用top命令查询CPU使用率较高的进程，加入是7443
 * 2.ps p 7443 -L -o pcpu,pid,tid,time,tname,cmd 通过这个命令查询此进程中使用率较高的线程tid(十进制)，比如是7453
 * 3.printf "%x\n" 7453 转换成十六进制 是 0x1d1d
 * 4.通过jstack -l 7443命令查询线程 dump信息，输出如下信息：
 *
 * "Thread-0" #8 prio=5 os_prio=0 tid=0x00007f052c0f0800 nid=0x1d1d runnable [0x00007f051b339000]
 *    java.lang.Thread.State: RUNNABLE
 * 	at java.io.FileOutputStream.writeBytes(Native Method)
 * 	at java.io.FileOutputStream.write(FileOutputStream.java:326)
 * 	at java.io.BufferedOutputStream.flushBuffer(BufferedOutputStream.java:82)
 * 	at java.io.BufferedOutputStream.flush(BufferedOutputStream.java:140)
 * 	- locked <0x00000000f5d69a68> (a java.io.BufferedOutputStream)
 * 	at java.io.PrintStream.write(PrintStream.java:482)
 * 	- locked <0x00000000f5d60cc0> (a java.io.PrintStream)
 * 	at sun.nio.cs.StreamEncoder.writeBytes(StreamEncoder.java:221)
 * 	at sun.nio.cs.StreamEncoder.implFlushBuffer(StreamEncoder.java:291)
 * 	at sun.nio.cs.StreamEncoder.flushBuffer(StreamEncoder.java:104)
 * 	- locked <0x00000000f5d69aa8> (a java.io.OutputStreamWriter)
 * 	at java.io.OutputStreamWriter.flushBuffer(OutputStreamWriter.java:185)
 * 	at java.io.PrintStream.newLine(PrintStream.java:546)
 * 	- eliminated <0x00000000f5d60cc0> (a java.io.PrintStream)
 * 	at java.io.PrintStream.println(PrintStream.java:807)
 * 	- locked <0x00000000f5d60cc0> (a java.io.PrintStream)
 * 	at com.example.demo.thread.FindJavaThreadInTaskManager$Worker.run(FindJavaThreadInTaskManager.java:20)
 * 	at java.lang.Thread.run(Thread.java:748)
 *
 * 	通过对比我们就知道了问题代码是在倒出第二行
 *
 *
 *
 *
 *
 */
public class FindJavaThreadInTaskManager {

    public static void main(String[] args) {
        Thread thread = new Thread(new Worker());
        thread.start();

    }

    private static class Worker implements Runnable {
        @Override
        public void run() {
            while (true) {
                System.out.println("Thread Named:" + Thread.currentThread().getName());
            }
        }
    }
}
