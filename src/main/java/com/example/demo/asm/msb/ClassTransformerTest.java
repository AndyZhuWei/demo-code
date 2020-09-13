package com.example.demo.asm.msb;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassVisitor;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.MethodVisitor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static jdk.internal.org.objectweb.asm.Opcodes.ASM4;
import static jdk.internal.org.objectweb.asm.Opcodes.INVOKESTATIC;

/**
 * @author HP
 * @Description TODO
 * @date 2020/9/13-22:19
 */
public class ClassTransformerTest {
    public static void main(String[] args) throws IOException {
        ClassReader cr = new ClassReader(
                ClassPrinter.class.getClassLoader().getResourceAsStream("com/example/demo/asm/msb/Tank.class"));
        ClassWriter cw = new ClassWriter(0);
        ClassVisitor cv = new ClassVisitor(ASM4, cw) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);

                return new MethodVisitor(ASM4, mv) {
                    @Override
                    public void visitCode() {
                        visitMethodInsn(INVOKESTATIC, "TimeProxy", "before", "()V", false);
                        super.visitCode();
                    }
                };
            }
        };
        cr.accept(cv, 0);
        byte[] b2 = cw.toByteArray();

        String path = (String)System.getProperties().get("user.dir");
        File f = new File(path+"/com/example/demo/asm/msb/");
        f.mkdirs();

        FileOutputStream fos = new FileOutputStream(new File(path+"/com/example/demo/asm/msb/Tank_0.class"));
        fos.write(b2);
        fos.flush();
        fos.close();
    }
}
