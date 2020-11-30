package com.example.demo.jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zhuwei
 * @Date:2020/2/16 14:01
 * @Description: Java堆内存溢出问题
 * 垃圾回收机制基本原则：内存不足的时候会去回收，内存如果足够，暂时不会回收。减少回收次数和回收时间
 * -Xms5m -Xmx10m -XX:+PrintGCDetails -XX:+HeapDumpOnOutOfMemoryError
 */
public class Test004 {

    public static void main(String[] args) {
        List<Object> listObject = new ArrayList<>();
        for(int i=0;i<10;i++) {
            System.out.println("i:"+i);
            Byte[] bytes = new Byte[1*1024*1024];
            listObject.add(bytes);
        }
        System.out.println("添加成功");




    }
}
