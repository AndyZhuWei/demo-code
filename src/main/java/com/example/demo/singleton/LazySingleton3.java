package com.example.demo.singleton;

/**
 * @Author: zhuwei
 * @Date:2018/12/10 16:59
 * @Description: 懒汉式单例类（线程假装安全，同步代码块）
 */
public class LazySingleton3 {

    private static LazySingleton3 instance = null;

    /**
     * 私有构造器
     */
    private LazySingleton3(){}

    public static LazySingleton3 getInstance() {
        if(instance==null) {
            synchronized (LazySingleton3.class) {
                instance = new LazySingleton3();
            }
        }
        return instance;
    }
}
