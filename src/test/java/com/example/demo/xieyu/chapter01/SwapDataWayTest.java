package com.example.demo.xieyu.chapter01;

import org.junit.Test;

/**
 * @Author: zhuwei
 * @Date:2019/10/22 16:23
 * @Description: 交换变量A、B的几种方式
 */
public class SwapDataWayTest {

    private int a = 34;
    private int b = 98;

    /**
     * 引用一个中间变量
     */
    @Test
    public void test01() {
        System.out.println("交换前：a="+a+",b="+b);
        int temp = a;
        a = b;
        b = temp;
        System.out.println("交换后：a="+a+",b="+b);
    }

    /**
     * 数据叠加后在进行减法
     */
    @Test
    public void test02() {
        System.out.println("交换前：a="+a+",b="+b);
        a = a + b;
        b = a - b;
        a = a - b;
        System.out.println("交换后：a="+a+",b="+b);
    }


    /**
     * test02方法有一个问题 就是 a + b有可能会越界
     * 所以我们可以用异或的方式进行运算
     * 这个运算是最低级的CPU位运算，所以它的效率是极高的，而且不会越界
     */
    @Test
    public void test03() {
        System.out.println("交换前：a="+a+",b="+b);
        a = a ^ b;
        b = a ^ b;
        a = a ^ b;
        System.out.println("交换后：a="+a+",b="+b);
    }
}



