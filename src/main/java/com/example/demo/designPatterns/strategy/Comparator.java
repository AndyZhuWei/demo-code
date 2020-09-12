package com.example.demo.designPatterns.strategy;

/**
 * @author HP
 * @Description 比较器
 * @date 2020/9/9-9:39
 */
//当确认只有一个方法时，这个注解也可以不写
@FunctionalInterface
public interface Comparator<T> {
    int compare(T o1,T o2);
}
