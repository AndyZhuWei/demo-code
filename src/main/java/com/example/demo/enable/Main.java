package com.example.demo.enable;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Description TODO
 * @Author zhuwei
 * @Date 2021/4/19 19:58
 */
//@EnableHelloWorld
//@EnableHelloWorld2
@EnableHelloWorld3
public class Main {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(Main.class);
        context.refresh();
        String helloWorld = context.getBean("helloWorld",String.class);
        //hello world
        System.out.println(helloWorld);
        context.close();
    }
}
