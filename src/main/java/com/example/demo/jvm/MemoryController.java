package com.example.demo.jvm;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Author: zhuwei
 * @Date:2020/4/19 17:57
 * @Description: 演示内存溢出
 */
@RestController
public class MemoryController {

    //对象的成员变量会随着对象本身而存储在堆上
    private List<User> userList = new ArrayList<>();
    //class会被放在Metaspace区
    private List<Class<?>> classList = new ArrayList<>();

    /**
     * 演示堆区内存溢出接口,并自动导出内存映像文件
     * 设定jvm参数：-Xms32M -Xmx32M -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./
     */
    @GetMapping("/heap")
    public String heap() {
        int i = 0;
        while(true) {
            //所以不断的往成员变量里添加数据就会导致内存溢出
            userList.add(new User(i++, UUID.randomUUID().toString()));
        }
    }

    /**
     * 演示非堆区内存溢出接口
     * 设定jvm参数：-XX:MetaspaceSize=32M -XX:MaxMetaspaceSize=32M
     */
    @GetMapping("/nonheap")
    public String nonHeap() {
        int i = 0;
        while(true) {
            //不断的存储class文件，就会导致Metaspace区内存溢出
            classList.addAll(Metaspace.createClasses());
        }
    }


















}
