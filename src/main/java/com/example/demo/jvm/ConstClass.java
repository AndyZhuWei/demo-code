package com.example.demo.jvm;

import javax.sound.midi.Soundbank;

/**
 * @Author: zhuwei
 * @Date:2019/10/28 22:28
 * @Description:
 */
public class ConstClass {
    static {
        System.out.println("ConstClass init!");
    }

    public static final String HELLOWORLD="hello world";
}
