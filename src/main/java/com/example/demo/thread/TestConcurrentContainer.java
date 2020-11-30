package com.example.demo.thread;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @Author: zhuwei
 * @Date:2020/2/4 19:21
 * @Description:
 */
public class TestConcurrentContainer {

    public static void main(String[] args) {
        ConcurrentLinkedQueue<String> concurrentLinkedQueue =
                new ConcurrentLinkedQueue<String>();
        concurrentLinkedQueue.offer("可乐");
        concurrentLinkedQueue.offer("Andy");
        concurrentLinkedQueue.offer("Lucy");
        //testPoll(concurrentLinkedQueue);
        testPeek(concurrentLinkedQueue);
    }

    private static void testPoll(ConcurrentLinkedQueue<String> concurrentLinkedQueue) {
        //取出元素并且删除,如果没有元素则返回null
        System.out.println(concurrentLinkedQueue.poll());
        System.out.println(concurrentLinkedQueue.poll());
        System.out.println(concurrentLinkedQueue.poll());
        System.out.println(concurrentLinkedQueue.poll());
        System.out.println(concurrentLinkedQueue.size());
    }

    private static void testPeek(ConcurrentLinkedQueue<String> concurrentLinkedQueue) {
        //取出元素并且删除,如果没有元素则返回null
        System.out.println(concurrentLinkedQueue.peek());
        System.out.println(concurrentLinkedQueue.peek());
        System.out.println(concurrentLinkedQueue.peek());
        System.out.println(concurrentLinkedQueue.peek());
        System.out.println(concurrentLinkedQueue.size());
    }
}
