package com.example.demo.xieyu.chapter02;


import org.junit.Test;
import org.springframework.test.context.TestPropertySource;

import java.nio.charset.Charset;

/**
 * @Author: zhuwei
 * @Date:2019/10/23 22:35
 * @Description: 计算机体系结构
 */
public class ComputerTests {

    //CPU的就近原则：一般来讲，一级缓存与CPU的延迟一般在2~3ns之间，二级缓存通常为10~15ns
    //三级缓存为20~30ns,而内存通常会在50ns以上甚至更高

    //在多核的Cache中，对于某些数据Cache后,数据在“写入”和“读取”的时候必须满足一些规范，通常
    //叫做“缓存一致性协议”。通过这种规范来实现架构，用以满足多个CPU对同一变量修改时，相互之间
    //都是知道的。不过，它并不保证所有的数据都是这样的，因为这样做的开销是巨大的


    //在编译阶段，Java就可以决定方法的“本地变量”（LocalVariable）的个数，因此在方法调用的时候，
    //就可以直接分配一个本地变量的区域。
    //这个空间是基于slot来分配的，每个slot占用32bit，就算是boolean也会占用这么宽，当前long、double
    //会占用2个slot，这些slot是可以被复用的，也就是说，在方法体内部，如果某个局部变量是在循环或判定语句内部声明的，
    //那么在退出这个区域后，对应的slot是可以被释放给在它之后声明的局部变量使用的

    //这里使用javap命令(反汇编器)看看运行后的指令是什么
    //通过它，我们可以对照源代码和字节码，从而了解很多编译器内部的工作。
    //运行“javap -verbose”(这是一种输出比较全的方式)
    //例如执行javap -verbose ComputerTests.class
    //结果如下：



    /*Classfile /F:/IT/demo-code/src/test/java/com/example/demo/xieyu/chapter02/ComputerTests.class
    Last modified 2019-10-23; size 324 bytes
    MD5 checksum 7bdf16f8c1be11dc136eec7ff875fc22
    Compiled from "ComputerTests.java"
    public class com.example.demo.xieyu.chapter02.ComputerTests
    minor version: 0
    major version: 52
    flags: ACC_PUBLIC, ACC_SUPER
    Constant pool:
    #1 = Methodref          #3.#12         // java/lang/Object."<init>":()V
    #2 = Class              #13            // com/example/demo/xieyu/chapter02/ComputerTests
    #3 = Class              #14            // java/lang/Object
    #4 = Utf8               <init>
   #5 = Utf8               ()V
   #6 = Utf8               Code
   #7 = Utf8               LineNumberTable
   #8 = Utf8               main
   #9 = Utf8               ([Ljava/lang/String;)V
  #10 = Utf8               SourceFile
  #11 = Utf8               ComputerTests.java
  #12 = NameAndType        #4:#5          // "<init>":()V
  #13 = Utf8               com/example/demo/xieyu/chapter02/ComputerTests
  #14 = Utf8               java/lang/Object
    {
  public com.example.demo.xieyu.chapter02.ComputerTests();
        descriptor: ()V
        flags: ACC_PUBLIC
        Code:
        stack=1, locals=1, args_size=1
        0: aload_0
        1: invokespecial #1                  // Method java/lang/Object."<init>":()V
        4: return
            LineNumberTable:
        line 8: 0

        public static void main(java.lang.String[]);
        descriptor: ([Ljava/lang/String;)V
        flags: ACC_PUBLIC, ACC_STATIC
        Code:
        stack=2, locals=4, args_size=1
        0: iconst_1
        1: istore_1
        2: iconst_2
        3: istore_2
        4: iload_1
        5: iload_2
        6: iadd
        7: istore_3
        8: return
            LineNumberTable:
        line 12: 0
        line 13: 2
        line 14: 4
        line 15: 8
    }
    SourceFile: "ComputerTests.java"*/

    //以上信息可以分两部分来看，一部分是常量池信息，另一部分是字节码body部分
    //常量池描述信息是在编译时就确定的。所谓常量池就是要记录一些常量，这些常量通常包含：类名、方法名、属性名、类型、修饰符、字符串常量，记录他们
    //的入口位置（符号#带上一个数字，可以理解为一个入口标志位）、一些对象的常量值

    //常量池在运行过程中需要组合成有效的运算指令，指令在body内部
    //iconst_1,将int类型的值1推送到栈顶
    //istore_1,将栈顶的元素弹出，赋值给第二个slot的本地变量
    //以上两个虚指令相当于int a = 1;

    //istore_0是赋值给你第一个slot起始位置，也就是main方法传入的String[]参数，如果是非静态方法看，this将作为任何方法的第1个本地变量

    //LocalVariableTable列表是本地变量的列表，某些开发工具默认就有这些信息，通过javac命令编译后的Class文件默认是没有这些输出信息，换句话
    //说在默认情况下本地变量没有名称的概念

    //javac -g:vars可以输出相应的信息
    //javac -g:vars,lines可输出LineNumberTable，这个参数将会用于debug中Class与源文件的行号对应关系

    //JVM能发出指令请求，由OS去完成具体工作，JVM自身无法做计算工作。
    public static void main(String[] args) {
        int a = 1;
        int b = 2;
        int c = a + b;
    }


    //多进程模式 惊群 多个进程监听同一个端口，当这个端口得到信号时，可能多个进程同时被唤醒的现象还是存在的，我们称之为“惊群”
    //Cache line将“连续的一段内存区域”进行Cache，不是每次就Cache一个内存单元，而是一系列内存单元。在计算机中，通常以连续64字节
    //为单位进行Cache操作，未来可能会更大
    //例如：
    //循环遍历二维数组int[][] a = new int[5][10]
    //两层for循环来遍历，外层遍历第一位，内层遍历第二维。还有一种遍历就是外层遍历第二维，内层遍历第一维度
    //这两种方式前者效率高，java数组分配是会先分配第一维，然后是第二维。因为第一种方式时a[0][0]、a[0][1]、...是在连续空间上分配的，CPU访问
    // a[0][0]时会被连续空间中的内容一起被放到Cache line里面。下次访问a[0][1]是就会直接从缓存中提取
    //而第二种方式a[0][0]、a[1][0]是分配在不同内存空间上的。
    // 这些细小的差距不仅体现在CPU上，在内存开销上还有巨大区别




    //缓存一致性协议：
    //当有来自于内存中的同一份数据Cache在多个CPU中，且要求这些数据的读写一致时，多个CPU之间就需要遵循缓存共享的一致性原则
    //内存单元有修改（Modified）、独占（Exclusive）、共享(Shared)、失效（Invalid）多种状态，多个CPU通过总线相互连接，
    //每个CPU的Cache处理器除了要去响应本身所对应的CPU的读写操作外，还需要监听总线上其他CPU的读写请求，通过监听对自己的Cache
    //做相应的处理，形成了一种虚共享，这个协议叫作MESI协议

    //上下文切换
    //进程想OS申请资源，线程是CPU调度的最基本单位。线程共享进程申请的资源

    //一般系统分为计算密集型和I/O密集型两种。
    //CPU密集型就是系统大部分时间是在做程序正常的计算任务，例如数字运算、赋值、分配内存、内存拷贝、循环、查找、排序等，
    //这些处理都需要CPU来完成，所以也叫CPU密集型,
    //I/O密集型是值系统大部分时间是在做I/O交互，而这个时间线程不会占用CPU来处理(但是通常会在系统中记录下这个线程正在等待I/O,
    // 以便I/O数据返回时，系统可以将其激活，被CPU再次调度)。换句话说，在这个时间范围内，可以由其他的线程来使用CPU,因此就可以多配置
    //一些线程。
    //CPU密集型 配置的线程数为核心数+1或-1
    //I/O(包括网络I/O、磁盘I/O、键盘I/O、屏幕输出I/O)密集型  配置的线程数为2*cpu个数，当然这只是经验，还需要根据关键程序进行测试而定



    //内存
    //内存与CPU通信的速度是磁盘的千倍以上，几乎无IOPS概念。容量都是以GB为单位，带宽也非常高，几十G每秒
    //CPU缓存的大小是一MB为单位，目前最大的三级缓存大小也不过几十MB,如果缓存变得过大，又会像内存那样设计各种赋值的算法管理
    //磁盘需要经过主板的很多步骤才能到达CPU(通常会经过南桥、北桥)。另外，机械硬盘的IOPS本身就很低，即使SSD硬盘，安装在原先
    //的SATA接口上，由于带宽的限制也很难达到理想中的高效
    //几个基础概念，以32位模式下的OS为例
    //物理地址：系统会为每个内存单元编写一些物理地址，物理地址可以认为是唯一的，它由内存本身的电路来控制
    //虚拟地址：所有的“程序中”使用的地址都是虚拟地址（在段式管理中也叫逻辑地址），这些地址在不同的进程之间是可以重复的，所以才叫虚拟地址
    //通过虚拟地址如何找到物理地址？
    //C代码编译后的指令中，许多调用的地址在编译阶段就得确定下来，许多方法入口和变量位置在编译时确定了虚拟地址，而真正运行时是要由
    //OS来分配实际的地址给程序的。另外，使用虚拟地址后，地址是可以被复用的，程序并不关注其他的进程是否会用同一地址（这一点由OS分配
    // 时来确保），而只关注自己的进程可以用这个地址

    //如果没有使用分页机制，那么每个程序会有一个“段”的概念，也就是为进程分配的一段内存区，它的起始位置+逻辑地址（此时逻辑地址就像偏移量
    // 一样）得到线性地址，而此时由于每个程序段是单独的一块连续区域，因此线性地址就是物理地址
    //若一个进程访问的不是“本进程的内存”，那么就会出现问题，在Windows上写C程序遇到这种情况会提示“内存不能read”等错误，进程可能会被crash掉

    //分页模型
    //分页模型会将内存划分成较小的页，物理上大多将其划分为4KB/页
    //系统会为每个进程分配页目录（也是一个页，大小也是4KB）,这些页由Kernel来管理。
    // 当通过一个虚拟地址访问内存时，会经过以下几步来找到物理地址
    // 1.首先会寻找对应进程页目录地址，加载到CR3寄存器中
    // 2.在32为系统中，每4个字节可以存放一个地址，因此页目录可以存放1024个地址，
    // 因此传入的逻辑地址只需要10位就可以定位到一个页目录中一个保存地址的单元（32位中的高10位），
    // 这个单元存储的地址就是“页表”地址
    // 3.页表本身也是一个页，大小也是4KB,每个也存储1024个地址，因此逻辑地址中的中间10个二进制位就可以标志出页表中的对应单元
    //页表也许不一定是物理页，可能还需要经过一层转换后才能得到物理页。
    //4 .也就是说，使用20个bit就可以定位到具体的页地址，在32位系统中，最后12位就可以在页内通过偏移地址找到对应的字节
    //通过以上可以看出，这样的寻址方式对于每个进程来讲都有机会得到1024*1024*4KB=4GB的地址空间。

    //前20位的地址都是连续的，而后面12位是内部偏移量，也是连续的，这样的地址叫“线性地址”（通常程序要分配一个连续地址，也即是一个线性地址）
    //线性地址最终要映射的物理位置，不仅仅是日常所说的物理内存本身，还可以是网卡设备、磁盘配置的Swap空间等，也就是当物理内存不够用时，
    //只要地址空间是够用的，那么就可以用其他的设备来替代，只是效率方面需要进一步评估。


    //OS通常会对“内核区”和“用户区”进行划分，“内核区”通常为操作系统本身所使用，它管管理各种进程、线程、各类设备的状态，管理它们之间的
    //关系对应、程序调度、分配资源、标记资源使用以及资源释放工作。

    //当一个进程需要申请“一次”分配内存时，操作系统会分配一块连续的虚拟地址内存给它。当然，进程可以多次向OS申请内存空间，
    //这些空间最终和进程所对应的页表映射起来，所以当进程退出后，这些资源都会被释放

    //在32为系统中，1.5G的Heap区域是比较合适的，因为JVM除了这部分空间外还需要使用很多空间，必须为这些开销预留空间
    //在64为系统中，这个空间几乎不会受到限制，不过这时JVM也必须换成64位模式的

    //STAT机械硬盘十分便宜 IOPS大概60
    //SAS机械硬盘贵一些 ISPS大概120~180
    //SSD硬盘采用了很多内存设计原理，而非机械硬盘的转盘思想，可以是心啊多通道读写操作。性能是机械硬盘几十倍甚至上百倍，
    //未来硬盘可能会和内存一样接近CPU

    //IOPS：磁盘每秒最多可以完成的I/O次数（一般是指小I/O）
    //随机读写：这是一个相对概念，磁盘每次发生读写时，需要寻找位置再进行。由于频繁发生磁盘小I/O请求（例如4KB）,所以就频繁地寻找位置，就像是随机读写
    //顺序读写：是相对随机读写来说的，每次读写的数据会比较大（例如1MB,对于过大的数据块读写，也会拆分为多个小I/O请求）。简单来说，如果找到了位置，那么处理1MB和4KB的数据时间是差不多的

    //机械硬盘的原理
    //一般许多硬件架构中的信息还会进程“南桥再到北桥”，传送过程中的通信带宽被大大降低，不过对于机械硬盘来讲，效率低的根本原因不是带宽，
    //而是IOPS,也就是机械硬盘“本身的读写效率”
    //首先，需要进行“寻道”操作（一块盘上有多个磁道），找到数据所在的磁道后，通过硬盘本身的不断“转动”转到数据实际的起始位置，
    //才开始真正读取数据，而读取数据之前的时间，都可以被认为是延迟时间。
    //摆头能否快速找到磁道，一般情况下和尺寸有关系，而磁道内寻址和转速有关系。
    //一般来讲，厂商会按照“半转”的旋转时间作为“平均寻址延迟时间”。
    //硬盘的延迟时间都是ms级别的，内存是ns级别的。

    //RAID5/RAID10



    //缓存
    //就近原则

    //一个汉字通过UTF-8编码，它一般被编码为3个字节，一个英文字母被编码为1个字节。
    //发送方用什么编码，接收方就要用什么编码来解码，如果发送方使用UTF-8编码，而接收方使用GBK来解码，那么肯定无法得到正确结果，而且一旦
    //转错，就无法转回来，但有的字符集是可以转回来的，如ISO8859-1
    //一般在传输数据是选择一个字符集，如果没有选择则使用“环境变量”相同的字符集，这个字符串可以通过Charset.defaultCharset()来获取
    @Test
    public void test01() {
        //环境变量中的字符集
        System.out.println(Charset.defaultCharset());
    }
}