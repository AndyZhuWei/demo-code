package com.example.demo.jms;

/**
 * @Author: zhuwei
 * @Date:2019/7/9 14:23
 * @Description:
 */
public class ProduceClient {

    public static void main(String[] args) throws Exception {
        MqClient.produce("Hello World!");
    }
}
