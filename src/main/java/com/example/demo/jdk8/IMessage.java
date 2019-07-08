package com.example.demo.jdk8;

/**
 * @Author: zhuwei
 * @Date:2019/7/6 21:34
 * @Description: 接口里定义default方法
 * 除了使用default定义方法之外，
 * 还可以使用static定义方法，一旦使用了static定义方法意味着这个方法可以直接由类名称调用
 */
public interface IMessage {
    void print();//这是一个接口里面原本定义的方法

    default void fun() {//在接口里面定义了一个普通的方法
        System.out.println("毁三观的方法出现了！");
    }

    static void get() {
        System.out.println("直接由接口调用！");
    }


}
