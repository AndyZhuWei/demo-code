package com.example.demo.jdk8;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @Author: zhuwei
 * @Date:2020/3/2 14:16
 * @Description: Java8获取参数名及Idea/Eclipse/Maven配置
 *
 * 在Java8之前，代码编译为class文件后，方法参数的类型固定，但是方法名称会丢失，方法名称会变成arg0、arg1....。
 * 而现在，在Java8开始可以在class文件中保留参数名，
 * 这就给反射带来了极大的遍历。像mybatis等需要使用反射机制获取方法参数的时候就可以不用像以前一样需要使用类似于@Para之类的注解。
 */
public class GetRuntimeParameterName {

    public void createUser(String name,int version) {

    }

    public static void main(String[] args) {
        for(Method m:GetRuntimeParameterName.class.getMethods()) {
            System.out.println("--------------------");
            System.out.println("method:"+m.getName());
            System.out.println("return:"+m.getReturnType().getName());
            for(Parameter parameter : m.getParameters()) {
                System.out.println("parameter:"+ parameter.getType().getName()+","+ parameter.getName());
            }
        }
    }
}
