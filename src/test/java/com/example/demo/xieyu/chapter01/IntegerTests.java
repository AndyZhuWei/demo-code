package com.example.demo.xieyu.chapter01;

import org.junit.Test;

import java.lang.reflect.Field;

/**
 * @Author: zhuwei
 * @Date:2019/10/23 17:05
 * @Description: 原生态类型介绍，也就是java中的基本类型
 */
public class IntegerTests {


    //若将原始类型int赋值给Integer类型，就会将原始类型自动编译为Integer.valueOf(int)
    //如果将Integer类型赋值给int类型，则会自动转换调用intValue()方法
    //根据源码Integer.valueOf(i)发现，当传入i的值在[-128,IntegerCache.high]区间的时候，会直接读取IntegerCache.cache这个数组中的值 较低版本JDK没有cache
    //默认情况下IntegerCache.high是127.不在区间中的就会直接通过new Integer(int)来创建
    //可以通过设置JVM启动参数-Djava.lang.Integer.IntegerCache.high=200来间接设置IntegerCache.high的值
    //也可以通过-XX:AutoBoxCacheMax来达到目的

    //
    //Boolean的两个值true和false都是在cache在内存中的。
    //Byte的256个值全部cache在内存中
    //Short\Long两种类型的cache范围为-128~127，无法调整，代码中完全写死了
    //Float\Double没有cache

    //当Integer与int类型进行比较的时候，会将Integer转换成int类型来比较（也就是通过调用intValue()方法返回数字），直接比较数字
    //Integer做“>”、">="、“<”、“<=”比较的时候，Integer会自动拆箱，就是比较它们的数字值
    //switch case为选择语句，匹配的时候不会equals(),而是直接用“==”。而在switch case语句
    //中，语法层面case部分是不能写Integer对象的。只能是普通的数字。如果传入Integer,会自动拆箱

    //JDK1.7中switch case支持String,但其实是语法糖，编译后字节码依然是if else语句，且通过equals()实现的
    @Test
    public void test01() {
        Integer a = 1;
        Integer b = 1;
        Integer c = 200;
        Integer d = 200;
        System.out.println(a == b);//true
        System.out.println(c == d);//false
    }


    static class People {
        private String name;
        private Integer age;
        private int sex;

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }
    }


    //在反射当中，对于Integer属性不能使用field.setInt()和field.getInt()操作
    //像sex就可以操作
    @Test
    public void test02() throws NoSuchFieldException, IllegalAccessException, InstantiationException {
        //People p = new People();
        Class peopleClass = People.class;
        People p = (People)peopleClass.newInstance();
        Field f = peopleClass.getDeclaredField("age");
        f.setAccessible(true);
        f.setInt(p,12);
        System.out.println(p.getAge());

    }





}
