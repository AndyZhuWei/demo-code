package com.example.demo.annoation;

import sun.security.krb5.internal.crypto.Des;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @Author: zhuwei
 * @Date:2019/6/20 13:49
 * @Description:
 */
public class ParseAnnotation {

    public static void main(String[] args) {
        try {
            //1.使用类加载器加载类对象
            Class clazz = Class.forName("com.example.demo.annoation.Child");
            //2.判断是否应用了这个注解
            boolean hasAnno = clazz.isAnnotationPresent(DesSimple.class);
            //3.获取Annotation对象
            if(hasAnno) {
                DesSimple ds = (DesSimple)clazz.getAnnotation(DesSimple.class);
                System.out.println(ds.value());
            }
            //4.找到方法上的注解
            Method[] ms = clazz.getMethods();
            /***********第一种解析方式************/
            for(Method m : ms) {
                boolean isMExist = m.isAnnotationPresent(DesSimple.class);
                if(isMExist) {
                    DesSimple dsm = (DesSimple)m.getAnnotation(DesSimple.class);
                    System.out.println(dsm.value());
                }
            }
            /***********另一种解析方式************/
            for(Method m : ms) {
                Annotation[] ans = m.getAnnotations();//获取所有注解在该方法上的注解
                for(Annotation an:ans) {
                    if(an instanceof DesSimple) {
                        DesSimple desSimple = (DesSimple)an;
                        System.out.println(desSimple.value());
                    }
                }

            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
