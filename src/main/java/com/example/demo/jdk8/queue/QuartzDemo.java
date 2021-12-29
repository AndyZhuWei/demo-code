package com.example.demo.jdk8.queue;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Description TODO
 * @Author zhuwei
 * @Date 2021/3/2 15:41
 * Quartz一款非常经典任务调度框架，在Redis、RabbitMQ还未广泛应用时，超时未支付取消订单功能都是由定时任务实现的。定时任务它有一定的周期性，
 * 可能很多单子已经超时，但还没到达触发执行的时间点，那么就会造成订单处理的不够及时。
 *
 * 在启动类中使用@EnableScheduling注解开启定时任务功能。
 */
@Component
public class QuartzDemo {

    //每隔五秒
    @Scheduled(cron = "0/5 * * * * ? ")
    public void process(){
        System.out.println("我是定时任务！");
    }
}
