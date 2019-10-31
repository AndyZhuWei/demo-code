package com.example.demo.xieyu.chapter03;

import com.google.common.base.Utf8;
import com.sun.org.apache.bcel.internal.classfile.LineNumberTable;
import org.junit.Test;

import java.io.PrintStream;

/**
 * @Author: zhuwei
 * @Date:2019/10/25 10:33
 * @Description: JVM相关这是
 */
public class JVMTests {

    //用javap来验证输出的结果是true
    public static void main(String[] args) {
        String a = "a" + "b" +1;
        String b = "ab1";
        System.out.println(a == b);//true
    }
    //用javap查看的编译后的指令如下：
//    Last modified 2019-10-25; size 693 bytes
//    MD5 checksum 0683c3ce16138b97db09fedf66f5cc33
//    public class com.example.demo.xieyu.chapter03.JVMTests
//    minor version: 0
//    major version: 52
//    flags: ACC_PUBLIC, ACC_SUPER
//    Constant pool:
//            #1 = Methodref          #6.#25         // java/lang/Object."<init>":()V
//            #2 = String             #26            // ab1
//            #3 = Fieldref           #27.#28        // java/lang/System.out:Ljava/io/PrintStream;
//            #4 = Methodref          #29.#30        // java/io/PrintStream.println:(Z)V
//            #5 = Class              #31            // com/example/demo/xieyu/chapter03/JVMTests
//            #6 = Class              #32            // java/lang/Object
//            #7 = Utf8               <init>
//   #8 = Utf8               ()V
//   #9 = Utf8 Code
//  #10 = Utf8               LineNumberTable
//  #11 = Utf8               LocalVariableTable
//  #12 = Utf8               this
//            #13 = Utf8               Lcom/example/demo/xieyu/chapter03/JVMTests;
//  #14 = Utf8               main
//  #15 = Utf8               ([Ljava/lang/String;)V
//  #16 = Utf8               args
//  #17 = Utf8               [Ljava/lang/String;
//  #18 = Utf8               a
//  #19 = Utf8               Ljava/lang/String;
//  #20 = Utf8               b
//  #21 = Utf8               StackMapTable
//  #22 = Class              #17            // "[Ljava/lang/String;"
//            #23 = Class              #33            // java/lang/String
//            #24 = Class              #34            // java/io/PrintStream
//            #25 = NameAndType        #7:#8          // "<init>":()V
//            #26 = Utf8               ab1
//  #27 = Class              #35            // java/lang/System
//            #28 = NameAndType        #36:#37        // out:Ljava/io/PrintStream;
//            #29 = Class              #34            // java/io/PrintStream
//            #30 = NameAndType        #38:#39        // println:(Z)V
//            #31 = Utf8               com/example/demo/xieyu/chapter03/JVMTests
//  #32 = Utf8               java/lang/Object
//  #33 = Utf8               java/lang/String
//  #34 = Utf8               java/io/PrintStream
//  #35 = Utf8               java/lang/System
//  #36 = Utf8               out
//  #37 = Utf8               Ljava/io/PrintStream;
//  #38 = Utf8               println
//  #39 = Utf8               (Z)V
//    {
//  public com.example.demo.xieyu.chapter03.JVMTests();
//        descriptor: ()V
//        flags: ACC_PUBLIC
//        Code:
//        stack=1, locals=1, args_size=1
//        0: aload_0
//        1: invokespecial #1                  // Method java/lang/Object."<init>":()V
//        4: return
//            LineNumberTable:
//        line 8: 0
//        LocalVariableTable:
//        Start  Length  Slot  Name   Signature
//        0       5     0  this   Lcom/example/demo/xieyu/chapter03/JVMTests;
//
//        public static void main(java.lang.String[]);
//        descriptor: ([Ljava/lang/String;)V
//        flags: ACC_PUBLIC, ACC_STATIC
//        Code:
//        stack=3, locals=3, args_size=1
//        0: ldc           #2                  // String ab1
//        2: astore_1
//        3: ldc           #2                  // String ab1
//        5: astore_2
//        6: getstatic     #3                  // Field java/lang/System.out:Ljava/io/PrintStream;
//        9: aload_1
//        10: aload_2
//        11: if_acmpne     18
//        14: iconst_1
//        15: goto          19
//        18: iconst_0
//        19: invokevirtual #4                  // Method java/io/PrintStream.println:(Z)V
//        22: return
//            LineNumberTable:
//        line 12: 0
//        line 13: 3
//        line 14: 6
//        line 15: 22
//        LocalVariableTable:
//        Start  Length  Slot  Name   Signature
//        0      23     0  args   [Ljava/lang/String;
//        3      20     1     a   Ljava/lang/String;
//        6      17     2     b   Ljava/lang/String;
//        StackMapTable: number_of_entries = 2
//        frame_type = 255 /* full_frame */
//        offset_delta = 18
//        locals = [ class "[Ljava/lang/String;", class java/lang/String, class java/lang/String ]
//        stack = [ class java/io/PrintStream ]
//        frame_type = 255 /* full_frame */
//        offset_delta = 0
//        locals = [ class "[Ljava/lang/String;", class java/lang/String, class java/lang/String ]
//        stack = [ class java/io/PrintStream, int ]
//    }


    //分析
    //1.比较靠前的一个部分是“常量池”，每一项都以“#数字”开头，这个数字是递增的，通常叫常量池的入口位置

    //例子 #1 = Methodref          #6.#25         // java/lang/Object."<init>":()V
    //入口位置#1，代表一个方法入口，该方法入口由入口#6和入口#21组成，中间用了一个“.”
    //通过分析#6和#25可以知道 就是一个java.lang.Object类的构造方法，入口参数个数为0，返回值为void.其实在#1后边的备注中已经标识出来了

    //例子 #2 = String             #26            // ab1
    //代表将会有一个String类型的引用入口，而引用的是入口#26的内容
    //#26 = Utf8               ab1
    //代表常量池中会存放内容ab1
    //综合起来就是：一个String对象的常量，存放的值是ab1


    //例子 #3 = Fieldref           #27.#28        // java/lang/System.out:Ljava/io/PrintStream;
    //入口#3代表一个属性，这个属性引用入口#27的类、入口#28的具体属性

    //后边就是指令执行的部分，这些指令是按照方法分开的。
    //stack=1, locals=1, args_size=1 这一行是所有方法都会有的
    //stack代表栈顶的单位大小（每个大小为一个slot，每个slot是4个字节的宽度），当需要使用一个数据时，它首先会被放入栈顶，使用完后会
    //写回到本地变量或主存中。这里栈的宽度是1
    //locals是本地变量的slot个数，但是并不代表与stack宽度一致。
    //args_size代表入口参数的个数，不再是slot的个数，即使传入一个long，也只会记录1

    //0: aload_0
    //第一个0代表虚指令中的行号 alodad_0表示将第1个slot所在的本地变量推到栈顶

    //1: invokespecial #1                  // Method java/lang/Object."<init>":()V
    //执行invokespecial指令，当发生构造方法调用、父类的构造方法调用、非静态的private方法调用时会使用该指令，这里需要常量池中获取一个方法，
    //这个地方会占用2个字节的宽度。所以下一行行号是从4开始

    //4: return
    //最后一行是return



//        LineNumberTable:
//        line 8: 0
//        LocalVariableTable:
//        Start  Length  Slot  Name   Signature
//        0       5       0    this   Lcom/example/demo/xieyu/chapter03/JVMTests;

    //代表本地变量的列表，本地变量的作用域其实位置为0，作用域宽度为5，slot起始位置也是0，名称为this 类型为com/example/demo/xieyu/chapter03/JVMTests

    //我们在分析main方法
 //   stack=3, locals=3, args_size=1
    //stack=3代表本地栈的slot个数为3，两个String需要load,System的out也会占用一个，当发生对比生成boolean的时候，会将两个
    //String的引用从栈顶pop出来，所以栈最多是3个slot
    //locals为3，因为有一个参数和两个String，如果是非静态方法，则本地变量会自动增加this
    //args_size为1代表这个方法的入参args
//        0: ldc           #2                  // String ab1
    //指令的body部分第0个字节为idc指令，从常量池入口#2中取出内容推到栈顶，这个String是引用，但是它是常量，所以使用Idc指令，而不是aload指令
//        2: astore_1
    //将栈顶的引用值写入第1个slot所在的本地变量
    //它与aload指令正好相反，对应astore_[0-3](范围是0x4b~0x4e)
    //更多的本地引用变量写入，则使用astore+ 引用遍历的slot位置
//        3: ldc           #2                  // String ab1
    //与第0行的操作一致，引用常量池入口#2来获得
//        5: astore_2
    //类似第二行，将栈顶的值赋值给第2个slot位置的本地引用变量
//        6: getstatic     #3                  // Field java/lang/System.out:Ljava/io/PrintStream;
    //获取静态域，放入栈顶，引用常量池入口#3来获得
    //此时的静态区域是System类的out对象
//        9: aload_1
    //将第1个slot所在位置的本地引用变量加载到栈顶
//        10: aload_2
    //将第2个slot所在位置的本地引用变量加载到栈顶
//        11: if_acmpne     18
//        14: iconst_1
//        15: goto          19
//        18: iconst_0
    //判定两个栈顶的引用是否一致（引用值也就是地址），对比处理的结束位置是18行
    //在if_acmpne操作之前会先将两个操作数从栈顶pop出来，因此栈顶最多是3位
    //如果一致，则将常量值1写入栈顶，对应到boolean值true,并跳转到19行
    //如果不一致，则将常量值0写入栈顶，对应到boolean值false
//        19: invokevirtual #4                  // Method java/io/PrintStream.println:(Z)V
    //执行out对象的println()方法，方法的入口参数是boolean类型，返回值是void
    //从常量池入口#4获得方法的内容实体
    //此时会将栈顶的元素当成入库参数，栈顶0或1则会转换成boolean值true\false
//        22: return
//            LineNumberTable:
//        line 12: 0
//        line 13: 3
//        line 14: 6
//        line 15: 22
    //对应源文件行号，左边是字节码的位置（也可以叫作行号），右边是源文件中的实际文本行号
    //javac编译默认有这个内容，但是如果-g:none则不会有，调式就会有问题
//        LocalVariableTable:
//        Start  Length  Slot  Name   Signature
//        0      23     0  args   [Ljava/lang/String;
//        3      20     1     a   Ljava/lang/String;
//        6      17     2     b   Ljava/lang/String;
       //本地变量列表，javac中需要使用-g:vars才会生成，使用一些工具会自动生成，若没有，则调式时在断点中看到的变量是没有名称的
    //第一个本地变量的作用区域从第0个字节开始，作用区域的范围是23字节，所在slot的位置是第0个位置，名称为args，类型为java/lang/String数组
    //第二 第三个类似
//        StackMapTable: number_of_entries = 2
//        frame_type = 255 /* full_frame */
//        offset_delta = 18
//        locals = [ class "[Ljava/lang/String;", class java/lang/String, class java/lang/String ]
//        stack = [ class java/io/PrintStream ]
//        frame_type = 255 /* full_frame */
//        offset_delta = 0
//        locals = [ class "[Ljava/lang/String;", class java/lang/String, class java/lang/String ]
//        stack = [ class java/io/PrintStream, int ]



    @Test
    public void test01() {
        int a = 1, b = 1, c = 1, d = 1;

        a++;
        ++b;

        c = c++;
        d = ++d;

        System.out.println(a+"\t"+b+"\t"+c+"\t"+d);//2  2  1  2
    }
    //以上代码中为什么只有一个1，我们用javap工具分析一下指令代码，列出部分指令代码如下：
    //0: iconst_1 //将int类型的常量值1推送到栈顶
    //1: istore_0 //将栈顶抛出的数据赋值给第1个slot所在的int类型的本地变量中
    //2: iconst_1 //与第0行一致
    //3: istore_1 //将栈顶抛出的数据赋值给第2个slot所在的int类型的本地变量中
    //4: iconst_1 //与第0行一致
    //5: istore_2 //将栈顶抛出的数据赋值给第3个slot所在的int类型的本地变量中
    //6: iconst_1 //与第0行一致
    //7: istore_3 //将栈顶抛出的数据赋值给第4个slot所在的int类型的本地变量中
    //8: iinc  0,1 //将第一个slot所在的int类型的本地变量自加1
    //11: iinc 1,1 //将第二个slot所在的int类型的本地变量自加1
    //14: iload_2 //将第3个slot所在的int类型的本地变量放入栈顶
    //15: iinc 2,1//将第3个slot所在的int类型的本地变量加1
    //18：istore_2 //将栈顶抛出的数据写入到第3个slot所在的int类型的本地变量
    //19：iinc 3,1 //将第4个slot位置所在的int类型的本地变量自增1
    //22：iload_3 //将第4个slot位置所在的int类型的本地变量加载到栈顶
    //23：istore_3 //将栈顶数据抛出，写入到第4个slot所在的int类型的本地变量中


    //当一个本地变量发生i++或++i操作的时候，如果这句代码发生在单行上面，即不会用于其他计算操作，那么他们最终的指令都是iinc,
    //也就是i++也会被改为++i操作

    //一个java方法分派时不仅仅分配操作数栈和本地变量区，至少还需要一个frame的数据区。职责包含：负责指向Class的常量池，以便于得到指令：会记录
    //一些内容帮助方法返回到正确的来源位置；设置PC寄存器得到要执行指令；记录异常表，控制异常的处理权

    //javap命令本身也将字节码翻译成文字，与反编译工具相比，它更加接近与字节码的结构


    //字节码
    //javac命令本身是一个“引导器”，它引导编译器程序的运行。编译器本身也是一个java程序，当运行javac命令时所引导的java类是
    //“com.sun.tools.javac.main.JavaCompiler”,这个类会完成Java源文件的解析（Parser）、注解处理（Annotation process）、
    //属性标注、检查、泛型处理、一些语法糖转换，最终生成Class文件

    //如果大家想要动态编译一些Java源码，则可以使用ToolProvider.getSystemJavaCompiler()来得到一个编译器，得到的是
    //javax.tools.Compiler类的一个对象，通过它可以创建CompilationTask任务来进行编译，编译任务运行后就可以得到编译后的
    //java字节码（换句话说，是一个byte[]数组），接下来就交给ClassCloader了。

    //主体结构由以下几部分
    //1.Class文件头部
    //首先，文件头部包含4个字节的头部验证码，这4个字节的十六进制表示分别是：0xCA、0xFE、0xBA、0xBE。这是固定的，如果文件
    //头部不是这4个字节，通常JVM不认
    //接下来是Class的minor version和major version,分别占用2个字节大小的空间，这两项信息代表Class是用什么版本的编译器编译的，当JRE
    //的版本低于这个版本是，它就会报错
    //Unsupported major.minor version 50

    //2.常量池区域
    //这部分的开始有2个字节来表示常量池入口位置的最大下标（其实不是长度，虽然官方给出的是长度），它的小标是从1开始的（从javap命令输出的结果
    // 中可以看到，都是从入口#1开始的）
    //常量池的每一项会有1个字节来标志它的类型，然后是常量池的内容。
    //例如Class紧接着的是2个字节的内容这2个字节代表Class的名称在常量池中的位置（通过前面的介绍，这个Class会记录另一个常量池的位置，另一个
    // 常量池的位置才会真正记录Class的名称，而这里该项仅仅代表返回的这个名称应当转换为一个Class）
    //Fieldref、Methodref、InterfaceMethodref会用2个字节代表Class在常量池中的位置，再用2个字节代表NameAndType信息，
    //对于属性自然是属性的名称和类型，对于方法自然是方法的名称，以及方法的入口参数和返回值类型

    //3.当前类的一些描述信息
    //首先，会用2个字节表示类的Class修饰符，这个修饰符带博爱的是这个类的访问方式（access_flags）
    //紧接着会有2个字节来标识类名在常量池中的位置。然后在用2个字节来标识父类名称在常量池中的位置，（如果该类没有写父类，那么就是Object类，
    // 所以这2个字节始终存在）
    //Java字节码中也是用2个字节来表达接口数量的，然后循环获取接口，在每一个接口的描述中，标识了它们在常量池中的入口位置，通过这个入口便
    //可以找到松油的接口列表了。

    //4.属性列表
    //用2个字节来代表长度是多少，也就是代表有多少个field
    //根据这个长度，就可以开始循环了
    //它与Class一样，用2个字节来代表field的“access_flags”,与之不同的是field还会增加一些transient\volatile\static的判定
    //紧接着的2个字节用来获取常量池的入口，对应到“属性名称”（name_index）,然后就是属性类型在常量池中的入口位置
    //另外，还有一些附加属性（attribute列表），它通常标识Signature、Annotaiton、Deprecated等信息

    //5.方法列表
    //同样的，需要2个字节来表示method的数量，依照这个数量循环取method的列表信息。对于每个method都有access_flags、方法名，
    //不过方法名具有一些特殊性，就是它们有可能是“构造方法”或“static匿名块”（读被归结到method列表中）。
    //方法肯定有入口参数和返回值，这里方法使用2个字节来保存常量池区域的入口位置，为了节约空间，常量池本身会将平时Java代码中的类型
    //进行转换来存放。例如“(II)V”的一个常量池表，就代表入口参数为2个int,而返回值为void
    //方法的body部分是用attribute来标识的，首先用2个字节代表attribute的个数，然后遍历每一个attribute的内容。

    //方法部分的解析是复杂的，毕竟它要解析这么多的代码逻辑，但是大家应该清晰地认识到编译的结果是一种逻辑接口，而且是方便计算机认识的逻辑
    //结构，这种逻辑结构对于Java语言来讲是通用的，由于它不是真正的运行指令，所以叫它虚指令。
    //Java虚拟机也会为每个OS平台编写对应的JRE运行时环境，与OS动态链接，将这些虚指令编码翻译为对应操作系统的汇编指令信息，即可在对应的
    //OS上调用执行了。

    //6.attribute列表



    //字节码增强
    //动态代理是Java默认提供的AOP技术，也是早期的Spring AOP框架所选用的技术（早期的Spring代理类必须用
    // 接口获取返回值）。既然是代理自然就需要一个代理原始类的实体存在，Java提供了“java.lang.reflect.InvocationHandler”
    //接口，我们需要实现这个接口来实现它的invoke方法。这种技术可以实现AOP,但是有几个缺陷：其一：它必须基于接口来实现，也就是代理的类
    //一定要有接口，而且只能调用皆苦对于的方法，实例的其他方法无法访问。其二，它实现的方法就好比一扇大门，进去了就可以随便调用，方法之间的调用
    //不会再进行AOP,它只在外部调用这些方法时，才会调用代理的invoke()方法

    //除此之外，字节码增强技术有两种实现机制：一种是通过创建原始类的一个子类，也就是动态创建的这个类继承原来的类，
    //现在的SpringAOP也正是通过这样的方式来实现的，它们创建的子类通常以“原始类的名称”为前缀，再加上一个奇怪的后缀，
    //以避免类名重复

    //另一种是暴力的，即直接修改原先的Class字节码，在许多类的跟踪过程中会使用到此技术。

    //实现字节码增强要通过如下两个步骤：
    //1.在内存中获取到原始的字节码，然后通过一些“开源届”写好的API来修改它的byte[]数组，得到一个新的byte[]数组
    //2.将这个新的byte[]数组写道PermGen区域，也就是加载它或替换原来的Class字节码（字节码增强还可以在进程外调用完成）

    //现在使用比较多的字节码修改的API大概有ASM、javassist、BCEL、SERP、CGLib(基于ASM)等技术。

    //获取到新的字节数组后，就要想办法来加载它。其中一种方式是基于Java的Instrument API来实现；另一种方式是通过指定的
    //ClassLoader来完成














}
