JVM由3部分组成：类装载子系统、执行引擎，运行时数据区（内存模型）
线程栈--栈帧
程序计数器：记录线程正在执行的字节码指令地址
栈帧由局部变量表、操作数栈、动态链接、方法出口组成
javap -c xxx.class反汇编我们的字节码文件，显示对应的字节码指令
其中方法出口保存的是方法执行完返回的地址
动态链接：保存的是在程序运行过程中方法对应的类元信息在JVM中的地址，通过对象头中的类型指针得到的地址

调优：
打印GC日志
Java虚拟机调优的目的是减少Full GC的次数和减少每次Full GC的时间
