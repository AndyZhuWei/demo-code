package com.example.demo.aop.v2;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author HP
 * @Description TODO
 * @date 2020/9/13-14:20
 */
public class Main {
    public static void main(String[] args) {
        ApplicationContext content = new ClassPathXmlApplicationContext("app_auto.xml.bak");
        Tank t = (Tank)content.getBean("tank");
        System.out.println(t.getClass().getSimpleName());
        t.move();
    }
}
