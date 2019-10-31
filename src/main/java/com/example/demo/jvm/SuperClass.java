package com.example.demo.jvm;

/**
 * @Author: zhuwei
 * @Date:2019/10/28 22:10
 * @Description:
 */
public class SuperClass {

    static {
        System.out.println("SuperClass init!");
    }

    public static int value = 123;
}
