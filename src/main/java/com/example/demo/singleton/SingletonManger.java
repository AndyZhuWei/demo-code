package com.example.demo.singleton;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zhuwei
 * @Date:2019/1/16 18:01
 * @Description: 登记式单例--使用Map容器来管理单例模式
 */
public class SingletonManger {
    private static Map<String,Object> objectMap = new HashMap<String,Object>();
    private SingletonManger(){};
    public static void registerService(String key,Object instance) {
        if(!objectMap.containsKey(key)) {
            objectMap.put(key,instance);
        }
    }
    public static Object getService(String key) {
        return objectMap.get(key);
    }

}
