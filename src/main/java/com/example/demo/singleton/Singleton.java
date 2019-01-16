package com.example.demo.singleton;

/**
 * @Author: zhuwei
 * @Date:2018/12/10 16:51
 * @Description: 通过类级内部类实现延迟加载和线程安全的单例对象(静态内部类（推荐使用）)
 */
public class Singleton {

    private Singleton(){
    }

    /**
     * 类级内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例没有绑定关系，而且只有被调用的时候
     * 才会被装载，从而是实现延迟加载
     */
    private static class SingletonHolder {
        /**
         * 静态初始化器，由JVMM来保障线程安全
         */
        private static Singleton instance = new Singleton();
    }

    public static Singleton getInstance() {
        return SingletonHolder.instance;
    }
}
