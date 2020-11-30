package com.example.demo.dataStruct;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author HP
 * @Description TODO
 * @date 2020/10/2-11:43
 */
public class ConCurrentQueueTest {

    private static Queue<String> tickets = new ConcurrentLinkedQueue<>();


    static {
        for (int i = 0; i < 10000; i++) {
            tickets.add("票号：" + i);
        }
    }

    public static void main(String[] args) {
        for(int i=0;i<10;i++) {
            new Thread(()->{
                while(true) {
                    String poll = tickets.poll();
                    if(poll == null) break;
                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("销售："+poll);
                }
            }).start();
        }
    }
}
