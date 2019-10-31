package com.example.demo.asm;


import org.springframework.asm.ClassVisitor;
import org.springframework.asm.Opcodes;
import org.springframework.asm.Type;

public class Transform extends ClassVisitor {
    public Transform(ClassVisitor cv) {
        super(Opcodes.ASM4,cv);
    }
    
    @Override
    public void visitEnd() {
        cv.visitField(Opcodes.ACC_PUBLIC,"age", Type.getDescriptor(int.class),null,null);
    }


    //如果不注释掉以下代码，就会报错
    //在 Person 类中有重复的属性，为什么会报这个错误呢？
    //
    //分析 ClassVisitor#visitField() 方法可得知，只要访问类中的一个属性，visitField() 方法就会被调用一次，
    // 在 Person 类中有两个属性，所以 visitField() 方法就会被调用两次，也就添加了两次 ‘public int age’ 属性，
    // 就报了上述的错误，而 visitEnd() 方法只有在最后才会被调用且只调用一次，所以在 visitEnd() 方法中是添加属性的最佳时机
    //
   /* @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        cv.visitField(Opcodes.ACC_PUBLIC, "age", Type.getDescriptor(int.class), null, null);
        return super.visitField(access, name, desc, signature, value);
    }*/
}