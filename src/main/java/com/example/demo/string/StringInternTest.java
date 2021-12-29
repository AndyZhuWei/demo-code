package com.example.demo.string;

/**
 * @Description String#intern方法测试
 * @Author zhuwei
 * @Date 2021/4/1 11:05
 * https://tech.meituan.com/2014/03/06/in-depth-understanding-string-intern.html
 */
public class StringInternTest {


    public static void main(String[] args) {
        String s = new String("1");
        String s2 = "1";
        s.intern();
        System.out.println(s == s2); //JDK6和JDK7都会输出false
        System.out.println("sdfas");




    }

}
