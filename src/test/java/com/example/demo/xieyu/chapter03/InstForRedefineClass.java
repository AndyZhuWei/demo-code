package com.example.demo.xieyu.chapter03;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

/**
 * @Author: zhuwei
 * @Date:2019/11/7 22:43
 * @Description: redefine Class的Agent代码部分
 */
public class InstForRedefineClass {

    private static Instrumentation inst;

    public static void premain(String agentArgs,
                               Instrumentation instP) {
        inst = instP;
    }

    public static void redefineClass(Class<?> theClass,
                                     byte[] theClassFile) throws UnmodifiableClassException, ClassNotFoundException {
        inst.redefineClasses(new ClassDefinition(theClass,theClassFile));
    }



















}
