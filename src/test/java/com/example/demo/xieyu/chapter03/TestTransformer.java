package com.example.demo.xieyu.chapter03;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * @Author: zhuwei
 * @Date:2019/11/5 23:15
 * @Description: 我们构造自己的一个Transformer
 */
public class TestTransformer implements ClassFileTransformer {

    @Override
    public byte[] transform(ClassLoader loader,String className,
                            Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) throws IllegalClassFormatException {
        //输出加载的类名，同样看到了类的加载
        System.out.println("load class:"+className);
        //只有指定的类才加载
        if("com/example/demo/xieyu/chapter03/ForASMTestClass".equals(className)) {
            try {
                CtClass ctClass
                        = ClassPool.getDefault().get(
                                className.replace('/','.'));
               CtMethod ctMethod = ctClass.getDeclaredMethod("display1");
               ctMethod.insertBefore(
                       "name=\"我是name!这次用javassist了哦!\";"+
                       "value=\"我是value!\";"+
                       "System.out.println(\"我是加进去的哦：\"+name);");
               ctMethod.insertAfter("System.out.println(value);");
               return ctClass.toBytecode();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return classfileBuffer;
    }





































}
