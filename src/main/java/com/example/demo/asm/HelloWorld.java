package com.example.demo.asm;

import com.sun.org.apache.bcel.internal.classfile.Code;
import com.sun.org.apache.bcel.internal.classfile.LineNumberTable;

/**
 * @Author: zhuwei
 * @Date:2019/10/29 22:23
 * @Description: 通过javac HelloWorld.java和javap -verbose HelloWorld.class
 * 可以查看到sayName()方法的字节码如下
 */
public class HelloWorld {

    public void sayHello() {
        try {
            Thread.sleep(2*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


//    public void sayHello();
//    descriptor: ()V
//    flags: ACC_PUBLIC
//    Code:
//    stack=2, locals=2, args_size=1
//            0: ldc2_w        #2                  // long 2000l
//            3: invokestatic  #4                  // Method java/lang/Thread.sleep:(J)V
//            6: goto          14
//            9: astore_1
//        10: aload_1
//        11: invokevirtual #6                  // Method java/lang/InterruptedException.printStackTrace:()V
//            14: return
//    Exception table:
//    from    to  target type
//             0     6     9   Class java/lang/InterruptedException
//    LineNumberTable:
//    line 5: 0
//    line 8: 6
//    line 6: 9
//    line 7: 10
//    line 9: 14
//    StackMapTable: number_of_entries = 2
//    frame_type = 73 /* same_locals_1_stack_item */
//    stack = [ class java/lang/InterruptedException ]
//    frame_type = 4 /* same */
}
