package com.example.demo.jvm;

/**
 * @Author: zhuwei
 * @Date:2020/2/16 14:01
 * @Description: 设置新生代比例参数 默认8:1:1
 * -Xms20m -Xmx20m -Xmn2m -XX:SurvivorRatio=2 -XX:+PrintGCDetails -XX:+UseSerialGC
 */
public class Test002 {

    public static void main(String[] args) {
        byte[] b = null;
        for (int i = 0; i < 10; i++) {
            b = new byte[1 * 1024 * 1024];
        }

    }
}
