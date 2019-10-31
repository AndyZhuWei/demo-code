package com.example.demo.xieyu.chapter03;

import jdk.internal.org.objectweb.asm.Opcodes;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.MethodVisitor;

/**
 * @Author: zhuwei
 * @Date:2019/10/29 9:38
 * @Description:
 */
public class ASMClassModifyAdpter extends ClassVisitor {

    public ASMClassModifyAdpter(ClassVisitor classVisitor) {
        super(Opcodes.ASM5, classVisitor);
    }

    public MethodVisitor visitMethod(final int access,
                                     final String methodName,
                                     final String desc,
                                     final String signature,
                                     final String[] exceptions) {
        if("display2".equals(methodName)) {
            return null;//屏蔽了这个display2方法
        }
        if("display1".equals(methodName)) {
            MethodVisitor methodVisitor =
                    cv.visitMethod(access,methodName,desc,signature,exceptions);
            //下面几条代码是对name属性赋值，相当于增加代码：name=“我是name”
            //加载this到栈顶（此时本地变量其实只有一个this）
            methodVisitor.visitIincInsn(Opcodes.ALOAD,0);
            //idc指令，从常量池中取出值加载到栈顶
            //这个代码会隐藏修改常量池
            methodVisitor.visitLdcInsn("我是name");

            //putfield指令，修改ForASMTestClass的name属性
            methodVisitor.visitFieldInsn(Opcodes.PUTFIELD,"com/example/demo/xieyu/chapter03/ForASMTestClass",
                    "name","Ljava/lang/String;");

            //同样是对属性赋值，相当于增加代码：value="我是value"
            //加载this到栈顶
            methodVisitor.visitVarInsn(Opcodes.ALOAD,0);
            //idc指令，从常量池中将值加载到栈顶
            methodVisitor.visitLdcInsn("我是value");
            //putfield指令，将栈顶的值赋值给FORASMTestClass类的value属性
            methodVisitor.visitFieldInsn(Opcodes.PUTFIELD,"com/example/demo/xieyu/chapter03/ForASMTestClass",
                    "value","Ljava/lang/String;");
            //再添加一个属性获取出来并打印(也就是打印会多一次)
            //getstatic指令，获取System类的out属性
            methodVisitor.visitFieldInsn(Opcodes.GETSTATIC,"java/lang/System",
                    "out","Ljava/io/PrintStream;");

            //加载this到栈顶
            methodVisitor.visitVarInsn(Opcodes.ALOAD,0);
            //通过getfield指令将ForASMTestClass类的name属性加载到栈顶
            methodVisitor.visitFieldInsn(Opcodes.GETFIELD,"com/example/demo/xieyu/chapter03/ForASMTestClass",
                    "name","Ljava/lang/String;");

            //调用out的println方法
            methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                                    "java/io/PrintStream",
                    "println",
                    "(Ljava/lang/String;)V");
            methodVisitor.visitEnd();
            return methodVisitor;//返回visitor
        } else {//其余方法不做任何处理，直接返回
            return  cv.visitMethod(access,methodName,desc,signature,exceptions);

        }
    }
}
