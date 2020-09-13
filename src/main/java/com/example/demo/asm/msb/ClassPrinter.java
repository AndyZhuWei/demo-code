package com.example.demo.asm.msb;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassVisitor;
import jdk.internal.org.objectweb.asm.FieldVisitor;
import jdk.internal.org.objectweb.asm.MethodVisitor;

import java.io.IOException;

import static jdk.internal.org.objectweb.asm.Opcodes.ASM4;

/**
 * @author HP
 * @Description TODO
 * @date 2020/9/13-21:53
 */
public class ClassPrinter extends ClassVisitor {

    public ClassPrinter(){super(ASM4);}

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] strings) {
        System.out.println(name+" extends " + superName + "{");
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String s2, Object o) {
        System.out.println("    "+ name);
        return null;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String s1, String s2, String[] strings) {
        System.out.println("    "+name+"()");
        return null;
    }

    @Override
    public void visitEnd() {
        System.out.println("}");
    }

    public static void main(String[] args) throws IOException {
        ClassPrinter cp = new ClassPrinter();
        ClassReader cr = new ClassReader("java.lang.Runnable");
        cr.accept(cp,0);
    }
}
