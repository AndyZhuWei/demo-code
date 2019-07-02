package com.example.demo.jdk8;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @Author: zhuwei
 * @Date:2019/7/2 17:13
 * @Description: 以ThreadLocal方式使用SimpleDateFormat
 */
public class ThreadLocalSimpleDateFormat {
    public static ThreadLocal<DateFormat> threadLocal = new ThreadLocal<DateFormat>(){
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };
}
