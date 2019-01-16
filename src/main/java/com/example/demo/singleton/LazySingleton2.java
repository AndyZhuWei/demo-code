package com.example.demo.singleton;

/**
 * @Author: zhuwei
 * @Date:2018/12/10 16:59
 * @Description: 懒汉式单例类（线程不安全）
 */
public class LazySingleton2 {

    private static LazySingleton2 instance = null;

    /**
     * 私有构造器
     */
    private LazySingleton2(){}

    public static  LazySingleton2 getInstance() {
        if(instance==null) {
            instance = new LazySingleton2();
        }
        return instance;
    }
}
