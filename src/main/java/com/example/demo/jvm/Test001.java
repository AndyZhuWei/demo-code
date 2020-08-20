package com.example.demo.jvm;

/**
 * @Author: zhuwei
 * @Date:2020/2/16 14:01
 * @Description: 配置堆内存大小 在8G或者16Gn内存的机器上默认为4G,在小于4G的机器上默认为当前机器内存大小
 * -Xmx20m -Xms5m
 */
public class Test001 {

    public static void main(String[] args) {
        byte[] b = new byte[4*1024*1024];
        System.out.println("分配了4M空间给数组");


        System.out.println("堆最大内存：" + Runtime.getRuntime().maxMemory() / 1024 / 1024 + "M");
        System.out.println("堆可用内存：" + Runtime.getRuntime().freeMemory() / 1024 / 1024 + "M");
        System.out.println("堆已经使用内存：" + Runtime.getRuntime().totalMemory() / 1024 / 1024 + "M");
    }
}
