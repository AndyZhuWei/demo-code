package com.example.demo.jdk8;

/**
 * @Author: zhuwei
 * @Date:2019/7/6 21:36
 * @Description:
 */
public class MessageImpl implements IMessage {
    @Override
    public void print() {
        System.out.println("Hello World!");
    }
}
