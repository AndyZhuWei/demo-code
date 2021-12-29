package com.example.demo.jdk8.queue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

/**
 * @Description DelayQueue 延时队列
 * @Author zhuwei
 * @Date 2021/3/2 15:00
 * DelayQueue的put方法是线程安全的，因为put方法内部使用了ReentrantLock锁进行线程同步。DelayQueue还提供了两种出队的方法 poll() 和 take()
 * poll() 为非阻塞获取，没有到期的元素直接返回null；take() 阻塞方式获取，没有到期的元素线程将会等待。
 * 下边只是简单的实现入队与出队的操作，实际开发中会有专门的线程，负责消息的入队与消费。
 */
public class DelayQueueDemo {
    public static void main(String[] args) throws InterruptedException {
        Order order1 = new Order("order1",5, TimeUnit.SECONDS);
        Order order2 = new Order("order2",10, TimeUnit.SECONDS);
        Order order3 = new Order("order3",15, TimeUnit.SECONDS);
        DelayQueue<Order> delayQueue = new DelayQueue<>();
        delayQueue.put(order1);
        delayQueue.put(order2);
        delayQueue.put(order3);

        System.out.println("订单延迟队列开始时间："+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        while(delayQueue.size() != 0) {
            //取队列头部元素是否过期
//            Order task = delayQueue.poll();
            Order task = delayQueue.take();
            if(task != null) {
                System.out.format("订单：{%s}被取消，取消时间：{%s}\n",task.name,LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
            Thread.sleep(1000);
        }
    }
}
