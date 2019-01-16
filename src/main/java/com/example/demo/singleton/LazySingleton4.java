package com.example.demo.singleton;

/**
 * @Author: zhuwei
 * @Date:2018/12/10 16:59
 * @Description: 懒汉式单例类（DCL「双重检测锁:Double Checked Lock」 单例（假））
 */
public class LazySingleton4 {

    private static LazySingleton4 instance = null;

    /**
     * 私有构造器
     */
    private LazySingleton4(){}

    public static LazySingleton4 getInstance() {
        if(instance==null) {
            synchronized (LazySingleton4.class) {
                if(instance==null) {
                    instance = new LazySingleton4();
                }
            }
        }
        return instance;
    }
}
