package com.example.demo.common;

/**
 * @Author: zhuwei
 * @Date:2019/10/21 16:16
 * @Description:
 */
public class Test {

    public static void main(String[] args) {
        String a1 = "AA";//在常量池上创建常量AA
        String a2 = "AA";//直接返回已经存在的常量AA
        System.out.println(a1 == a2); //true


        String a3 = new String("AA");    //在堆上创建对象AA
        a3.intern(); //在常量池上创建对象AA的引用
        String a4 = "AA"; //常量池上存在引用AA，直接返回该引用指向的堆空间对象，即a3
        System.out.println(a3 == a4); //false,如果这个例子不理解，请看完整篇文章再回来看这里
    }
}
