package com.example.demo.xieyu.chapter03;

import org.springframework.asm.ClassReader;
import org.springframework.asm.ClassWriter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLClassLoader;

/**
 * @Author: zhuwei
 * @Date:2019/10/29 11:03
 * @Description:
 */
public class ASMTestMain {
    private final static DynamicClassLoader TEST_CLASS_LOADEr
            = new DynamicClassLoader(
            (URLClassLoader)ASMTestMain.class.getClassLoader());

    public static void main(String[] args) throws Exception {
        //修改器的Class
        Class<?> beforeASMClass =
                TEST_CLASS_LOADEr.loadClass("com.example.demo.xieyu.chapter03.ForASMTestClass");
        //修改Class
        TEST_CLASS_LOADEr.defineClassByByteArray("com.example.demo.xieyu.chapter03.ForASMTestClass",
                asmChangeClassCall());
        //修改后的Class
        Class<?> afterASMClass
                = TEST_CLASS_LOADEr.loadClass("com.example.demo.xieyu.chapter03.ForASMTestClass");

        Object beforeObject = beforeASMClass.newInstance();
        Object afterObject = afterASMClass.newInstance();

        beforeASMClass.getMethod("display1").invoke(beforeObject);
        afterASMClass.getMethod("display1").invoke(afterObject);
    }



    private static byte[] asmChangeClassCall() throws IOException {
        ClassReader classReader =
                new ClassReader("com.example.demo.xieyu.chapter03.ForASMTestClass");
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        ASMClassModifyAdpter modifyAdpter =
                new ASMClassModifyAdpter(classWriter);
        classReader.accept(modifyAdpter,ClassReader.SKIP_DEBUG);
        //这里输出的字节码，代码已被注释，若输出到文件，则可以用javap命令来查看
        //byte[] bytes = classWriter.toByteArray();
        //new FileOutputStream("d:/ForASMTestClass.class").writer(bytes);
        //return bytes;

        return classWriter.toByteArray();
    }





























}
