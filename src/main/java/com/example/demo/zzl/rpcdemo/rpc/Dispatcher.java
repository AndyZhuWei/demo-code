package com.example.demo.zzl.rpcdemo.rpc;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author HP
 * @Description TODO
 * @date 2020/11/9-10:43
 */
public class Dispatcher {

    private static Dispatcher dis = null;
    static {
        dis = new Dispatcher();
    }

    public static Dispatcher getDis() {
        return dis;
    }

    private Dispatcher(){

    }


    public static ConcurrentHashMap<String,Object> invokeMap = new ConcurrentHashMap<>();

    public void register(String k,Object object) {
        invokeMap.put(k,object);
    }

    public Object get(String k) {
        return invokeMap.get(k);
    }
}
