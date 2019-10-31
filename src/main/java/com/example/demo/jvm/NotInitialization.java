package com.example.demo.jvm;


/**
 * @Author: zhuwei
 * @Date:2019/10/28 22:11
 * @Description: 非主动使用类字段演示
 */
public class NotInitialization {

    //经过验证，以下代码只会输出SuperClass init!,而不会输出SubClass init!
    //所以通过子类来引用父类中定义的静态字段，只会触发父类的初始化而不会触发子类的初始化。
    //但是触发了子类的加载和验证。（对于Sun HotSpot虚拟机而言）

//    public static void main(String[] args) {
//        System.out.println(SubClass.value);
//    }

    //通过数组定义来引用类，不会触发此类的初始化
    //都是这段代码里面触发了另一个名为“[com.example.demo.jvm.SuperClass”的类的初始化阶段，
    //对于用户代码来说，这并不是一个合法的类名称，它是一个由虚拟机自动生成的、直接继承于java.lang.Object的子类，
    //创建动作由字节码指令newarray触发
    //这个类代表了一个元素类型为com.example.demo.jvm.SuperClass的一维数组，数组中应有的属性和方法（length
    // clone()方法）都实现在这个类中。

//    public static void main(String[] args) {
//        SuperClass[] sca = new SuperClass[10];
//    }


    //常量在编译阶段会存入调用类的常量池中，本质上并没有直接引用到定义常量的类，因此不会触发定义常量的类的初始化
    //虽然在源码中引用了ConstClassl类中的常量，但其实在编译阶段通过常量传播优化，已经将此常量的值“hello world”
    //存储到了NotInitialization类的常量池中
    //也就是说NotInitialization的Class文件中并没有ConstClss类的符号引用入口，这两个类在编译后就不存在联系了。
    public static void main(String[] args) {
        System.out.println(ConstClass.HELLOWORLD);
    }





}
