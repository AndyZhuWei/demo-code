package com.example.demo.xieyu.chapter02;

import net.bytebuddy.matcher.IsNamedMatcher;

/**
 * @Author: zhuwei
 * @Date:2019/10/28 9:47
 * @Description: Class字节码的加载
 */
public class ClassLoaderTests {

    //ClassLoader的继承关系是从BootStrapClassLoader开始的，也有由它最先加载类，然后是ExtClassLoader，接下来
    //是AppClassLoader(应用车关系默认的)，最后是用户自己的ClassLoader

    //BootStrapClassLoader主要用于加载一些Java自带的核心类，在JVM中由C++实现
    //ExtClassCloader加载jre/lib/ext目录下的jar包
    //AppClassLoader，加载classpath下面的内容
    //用户自定义的ClassLoader，可以完全由用户自己来定义

    //用户自定义的ClassLoader一般继承于URLClassLoader，也可以继承ClassLoader或SecureClassLoader
    //它们之间本身也是继承关系，根据实际情况重写不同的方法即可

    //加载过程
    //1.读取文件。读取文件首先得找到文件，也即是找到.class文件，它将以“类全名+ClassLoader名称”作为唯一标识，
        //加载于方法内部。在加载过程中，首先申请父ClassLoader来加载，也就是尽量用父类的ClassLoader来加载；
    //如果父ClassLoader无法加载这个类，那么就由子类来加载，如果最终都没有找到对应的类，则会抛出异常ClassNotFoundException

    //2.链接。这个动作内部就是要对加载的字节码进行解析、校验，看是否符合字节码的规范结构。如果不符合就会抛错ClassNotFoundEror
    //这一步还会为Class对象分配内容。这一步可选的是将常量池中的符号引用解析为直接引用

    //3.初始化。会调用这个Class对象自身的构造函数，对静态变量、static块进行赋值（通过javap指令可以发现，其实许多静态变量的赋值
    // 会在编译时放在static块中完成）

    //所有的类在使用前都必须被加载和初始化，初始化过程由<clinit>方法来确保它是线程安全的（包括static块也必须要执行完，才能被使用），
    //如果有多个线程同时尝试访问该类，则必须等待static块执行完成，否则都将被阻塞



    //java.lang.NoClassDefFoundError: Could not initialize class com.example.demo.xieyu.chapter02.B
    //当在static块中引用自身类的一个实例时，JVM在加载这个类时会调用这个static块，因此就会去引用相应的实例。而JVM又发现这个类
    //还未加载，但是目前正在加载这个类的过程中，因此就会出现类无法加载的问题。
    public static void main(String[] args) {
        new Thread() {
            @Override
            public void run() {
               try {
                   Thread.sleep(300);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }

               B.getInstance().test2();
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                B.getInstance().test2();
            }
        }.start();
    }


    //Class的加载会阻塞多个线程，因此会使用到的Class最好被预先加载到内存中（可以叫它预热），否则用的时候再去加载就可能会发生阻塞
    //static块是一个好东西，它可以让我们做一些线程安全的扩展，但是它并不是适合于编写大量复杂业务的地方，尤其是大量的I/O操作逻辑

    //如果想看看一段程序到底加载了什么类，则可以通过在Java程序运行时增加启动参数：-XX:+TraceClassLoading来实现。


    //在JVM加载的过程中还会产生很多的中间代码，会做一些优化后转换为目标代码开始运行，运行时会有JIT的介入，它会做动态内联及代码的进一步优化并将它尽量Cache
    //住。作为Java程序猿，应该知道它的运行时优化不止一层，也不是立即被优化到最佳的，而是随着运行次数的增加，会被JIT不断去优化。这样，自己在写代码的时候，
    //就要考虑代码是否可能被JIT优化

    //JIT优化会将优化后的本地代码存放在CodeCache中，这个空间默认的大小为48MB,可以使用参数-XX:ReservedCodeCacheSize=64m来修改这个空间大小
    //参数-XX:+Use CodeCacheFlushing可以对CodeCache进行清理。
    //-XX:CICompilerCount的设置相对较大可以在一定程度上提升JIT编译的速度（默认为2），在高并发系统中适当设置可以有效提升性能


    //类中的static块、static变量、final static变量、普通块、对象的构造方法执行顺序？
    //用javap命令就可以发现static变量的赋值操作会被放到static块中来执行，如果被赋值的是final static普通变量，则会直接赋值，如果是对象，
    //则依然编译到static块中执行，由<clinit>保证线程安全。普通块将被编译到相应类的构造方法自定义语句的前面，不过是在调用super()操作的后面。








































}
