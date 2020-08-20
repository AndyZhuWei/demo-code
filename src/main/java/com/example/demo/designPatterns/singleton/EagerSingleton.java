package com.example.demo.designPatterns.singleton;

/**
 * @Author: zhuwei
 * @Date:2018/12/10 16:57
 * @Description: 饿汉式单例（静态变量）
 */
public class EagerSingleton {

    private static EagerSingleton instance = new EagerSingleton();

    /**
     * 私有默认构造器
     */
    private EagerSingleton(){}

    /**
     * 静态工厂方法
     */
    public static EagerSingleton getInstance() {
        return instance;
    }
}
