package com.example.demo.asm.msb;

import com.example.demo.asm.MyClassLoader;
import jdk.internal.org.objectweb.asm.ClassWriter;

import static jdk.internal.org.objectweb.asm.Opcodes.*;

/**
 * @author HP
 * @Description 利用asm动态生成字节码
 * @date 2020/9/13-22:01
 */
public class ClassWriteTest {
    public static void main(String[] args) {
        ClassWriter cw = new ClassWriter(0);
        cw.visit(V1_5,ACC_PUBLIC+ACC_ABSTRACT+ACC_INTERFACE,
                "pkg/Comparable",null,"java/lang/Object",
                null);
        cw.visitField(ACC_PUBLIC+ACC_FINAL+ACC_STATIC,"LESS","I",
                null,-1).visitEnd();
        cw.visitField(ACC_PUBLIC+ACC_FINAL+ACC_STATIC,"EQUAL","I",
                null,0).visitEnd();
        cw.visitField(ACC_PUBLIC+ACC_FINAL+ACC_STATIC,"GREATER","I",
                null,1).visitEnd();
        cw.visitMethod(ACC_PUBLIC+ACC_ABSTRACT,"compareTo",
                "(Ljava/lang/Object;)I",null,null).visitEnd();
        cw.visitEnd();
        byte[] b = cw.toByteArray();

        MyClassLoader myClassLoader = new MyClassLoader();
        Class c = myClassLoader.defineClass("pkg.Comparable",b);
        System.out.println(c.getMethods()[0].getName());

    }
}
