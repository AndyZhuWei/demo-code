package com.example.demo.asm;

import org.springframework.asm.ClassReader;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.ClassWriter;

import java.io.FileInputStream;
import java.io.InputStream;

public class FieldPractice {

    public static void main(String[] args) {
        addAgeField();
        //输出以下内容
        //null
        //0
    }

    private static void addAgeField() {
        try {
            String className = "com.example.demo.asm.Person";
            InputStream inputStream = new FileInputStream("F:\\IT\\demo-code\\target\\classes\\com\\example\\demo\\asm\\Person.class");
            ClassReader reader = new ClassReader(inputStream);
          
            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
          
            ClassVisitor visitor = new Transform(writer);
            reader.accept(visitor, ClassReader.SKIP_DEBUG);

            byte[] classFile = writer.toByteArray();
            MyClassLoader classLoader = new MyClassLoader();
            Class clazz = classLoader.defineClass(className, classFile);
            Object obj = clazz.newInstance();

            System.out.println(clazz.getDeclaredField("name").get(obj)); //----(1)
            System.out.println(clazz.getDeclaredField("age").get(obj));  //----(2)
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}