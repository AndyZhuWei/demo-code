package com.example.demo.asm;

/**
 * @Author: zhuwei
 * @Date:2019/10/30 9:13
 * @Description:
 */
public class MyClassLoader extends ClassLoader {

    public Class defineClass(String className, byte[] toByteArray) {
       return defineClass(className, toByteArray, 0, toByteArray.length);
    }
}
