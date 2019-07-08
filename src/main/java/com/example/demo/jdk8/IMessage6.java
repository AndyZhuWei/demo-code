package com.example.demo.jdk8;

/**
 * @Author: zhuwei
 * @Date:2019/7/6 21:34
 * @Description: 特定类引用
 */
@FunctionalInterface //此为函数式接口，只能够定义一个方法
public interface IMessage6<P> {
    public int compare(P p1,P p2);
}
