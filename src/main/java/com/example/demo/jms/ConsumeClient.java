package com.example.demo.jms;

/**
 * @Author: zhuwei
 * @Date:2019/7/9 14:37
 * @Description:
 */
public class ConsumeClient {
    public static void main(String[] args) throws Exception {
        String message = MqClient.consume();
        System.out.println(message);

    }
}
