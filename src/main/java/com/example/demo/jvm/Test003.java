package com.example.demo.jvm;

/**
 * @Author: zhuwei
 * @Date:2020/2/16 14:01
 * @Description: 设置新生代与老年代的比例参数 默认1:2
 * -Xms20m -Xmx20m -XX:SurvivorRatio=2 -XX:+PrintGCDetails -XX:+UseSerialGC -XX:NewRatio=2
 */
public class Test003 {

    public static void main(String[] args) {
        byte[] b = null;
        for (int i = 0; i < 10; i++) {
            b = new byte[1 * 1024 * 1024];
        }

    }
}
