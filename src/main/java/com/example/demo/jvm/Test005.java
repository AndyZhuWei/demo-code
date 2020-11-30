package com.example.demo.jvm;

/**
 * @Author: zhuwei
 * @Date:2020/2/16 14:01
 * @Description: 栈溢出问题 栈默认最大深度12291（针对此时环境，不同环境不同）
 * -Xxs5m
 */
public class Test005 {

    private static int count;

    public static void count() {
        try {
            count++;
            count();
        } catch (Throwable e) {
            System.out.println("最大深度：" + count);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        count();
    }
}
