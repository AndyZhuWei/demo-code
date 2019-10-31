package com.example.demo.xieyu.chapter03;

import java.net.URLClassLoader;

/**
 * @Author: zhuwei
 * @Date:2019/10/29 10:56
 * @Description:
 */
public class DynamicClassLoader extends URLClassLoader {

    public DynamicClassLoader(URLClassLoader parentClassLoader) {
        super(parentClassLoader.getURLs(),parentClassLoader);
    }

    public Class<?> defineClassByByteArray(String className,byte[] bytes) {
        //这个defineClass对于同名的类如果调用两次以上，就会报错
        return this.defineClass(className,bytes,0,bytes.length);
    }
}
