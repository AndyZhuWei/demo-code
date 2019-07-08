package com.example.demo.jdk8;

/**
 * @Author: zhuwei
 * @Date:2019/7/6 21:34
 * @Description: 普通方法应用
 */
@FunctionalInterface //此为函数式接口，只能够定义一个方法
public interface IMessage5<R> {
    public R upper();
}
