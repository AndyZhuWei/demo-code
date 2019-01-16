package com.example.demo.singleton;

/**
 * @Author: zhuwei
 * @Date:2018/12/10 16:57
 * @Description: 饿汉式单例（静态常量）
 */
public class EagerSingleton2 {

    private static final EagerSingleton2 instance = new EagerSingleton2();

    /**
     * 私有默认构造器
     */
    private EagerSingleton2(){}

    /**
     * 静态工厂方法
     */
    public static EagerSingleton2 getInstance() {
        return instance;
    }
}
