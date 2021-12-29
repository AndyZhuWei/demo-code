package com.example.demo.hj.identical.uuid;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description TODO
 * @Author zhuwei
 * @Date 2021/3/31 9:38
 */
public class Test {


    public String generateUid() {
        return UUID.randomUUID().toString().replace("-","");
    }

    private static Integer THREADS = 100;

    private ExecutorService executorService = Executors.newCachedThreadPool();



    public static void main(String[] args) {
        ConcurrentHashMap<String,String> idMap = new ConcurrentHashMap<>();
    }
}
