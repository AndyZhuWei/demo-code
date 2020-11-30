package com.example.demo.designPatterns.singleton;

/**
 * @Author: zhuwei
 * @Date:2018/12/10 16:57
 * @Description: 饿汉式单例（静态代码块）
 */
public class EagerSingleton3 {

    private static EagerSingleton3 instance;

    static {
        instance = new EagerSingleton3();
    }

    /**
     * 私有默认构造器
     */
    private EagerSingleton3(){
    }

    /**
     * 静态工厂方法
     */
    public static EagerSingleton3 getInstance() {
        return instance;
    }
}
