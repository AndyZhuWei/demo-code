package com.example.demo.aop.v2;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * @author HP
 * @Description 用注解的方式定义切面逻辑
 * @date 2020/9/13-14:27
 */
@Aspect
public class TimeProxy {

    @Before("execution(void com.example.demo.aop.v2.Tank.move())")
    public void before() {
        System.out.println("method start.."+ System.currentTimeMillis());
    }

    @After("execution(void com.example.demo.aop.v2.Tank.move())")
    public void after() {
        System.out.println("method stop.."+System.currentTimeMillis());
    }

}
