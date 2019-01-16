package com.example.demo.singleton;

/**
 * @Author: zhuwei
 * @Date:2018/12/10 16:51
 * @Description: DCL「双重检测锁:Double Checked Lock」 单例（真，推荐使用）
 */
public class Singleton2 {

    private static volatile Singleton2 instance = null;

    private Singleton2(){
    }

    public static Singleton2 getInstance() {
        //先检查实例是否存在，不过不存在，则进入下面的代码块
        if(instance==null) {
            //同步块，线程安全的创建实例
            synchronized (Singleton2.class) {
                //再次检查实例是否存在，如果不存在则创建实例
                if(instance==null) {
                    instance = new Singleton2();
                }
            }
        }
        return instance;
    }
}
