package com.example.demo.aop.v1;

/**
 * @author HP
 * @Description TODO
 * @date 2020/9/13-14:27
 */
public class TimeProxy {

    public void before() {
        System.out.println("method start.."+ System.currentTimeMillis());
    }

    public void after() {
        System.out.println("method stop.."+System.currentTimeMillis());
    }

}
