package com.example.demo.tools;

import com.example.demo.thread.Interrupted;
import org.springframework.aop.ThrowsAdvice;

import java.io.*;
import java.util.Random;
import java.util.Vector;

/**
 * @Author zhuwei
 * @Date 2020/8/5 9:36
 * @Description: 为测试了解各个监控工具编写的测试代码
 */
public class DemoCode {

    public static class HoldCPUTask implements Runnable {
        public static Object[] lock = new Object[100];
        public static Random r = new Random();
        static {
            for(int i=0;i<lock.length;i++) {
                lock[i] = new Object();
            }
        }

        @Override
        public void run() {
            int loop = 0;

            while(true) {
                //随机占用CPU资源
                int loopNum = (int)(Math.random()*100);//产生一个1-100的随机数，改数值被用来确定冒泡算法基础数据个数
                int a[] = new int[loopNum];
                for(int i=0;i<loopNum;i++) {
                    a[i]=(int)(Math.random()*100);//随机赋值
                }
                //开始冒泡方式排序
                for(int i=0;i<loopNum-1;i++){
                    for(int j=0;j<loopNum-i-1;j++) {
                        int temp;
                        if(a[j]>a[j+1]) {
                            temp = a[j];
                            a[j] = a[j+1];
                            a[j] = temp;
                        }
                    }
                }

                //随机占用磁盘I/O
                int fileloop = (int)(Math.random()*10000);//产生一个1-10000的随机数，该数值被用来确定写入文件的次数
                try {
                    FileOutputStream fos = new FileOutputStream(new File("temp"));
                    for(int i=0;i<fileloop;i++) {
                        fos.write(i);
                    }
                    fos.close();
                    FileInputStream fis = new FileInputStream(new File("temp"));
                    while(fis.read()!=-1);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //随机开始持有锁
                int x = (int)(Math.random()*100);//产生一个1-100的随机数，该数值被用来确定锁数量
                synchronized (lock[x]) {
                    if(x%2==0) {
                        try {
                            lock[x].wait(r.nextInt(10));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }//等待
                    } else {
                        lock[x].notifyAll();//通知
                    }
                }

                //随机开始占用内存
                int memSize = (int)(Math.random()*100);//产生一个1-100的随机数，该数值被用来确定写入申请内存大小
                Vector v = new Vector();
                for(int i=0;i<=10;i++) {
                    byte[] b = new byte[memSize*memSize];
                    v.add(b);
                }

                try {
                    Thread.sleep(1000);//该线程休息一会
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public static void main(String[] args) {
        int threadNum = (int)(Math.random()*100);//产生一个1-100的随机数，该数值被用来确定工作线程数量
        System.out.println(threadNum);
        for(int i=0;i<threadNum;i++) {
            new Thread(new HoldCPUTask()).start();
        }
    }
}
