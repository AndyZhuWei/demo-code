package com.example.demo.designPatterns.singleton;

/**
 * @Author: zhuwei
 * @Date:2018/12/10 16:59
 * @Description: 懒汉式单例类（线程安全，存在同步开销）
 */
public class LazySingleton {

    private static LazySingleton instance = null;

    /**
     * 私有构造器
     */
    private LazySingleton(){}

    public static synchronized LazySingleton getInstance() {
        if(instance==null) {
            instance = new LazySingleton();
        }
        return instance;
    }
}
