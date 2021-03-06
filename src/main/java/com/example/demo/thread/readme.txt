第三章 Java内存模型


==并发编程模型的两个关键问题===
1.并发编程模型的两个关键问题：线程之间的通信和线程之间的同步。
2.在命令式编程中，线程之间的通信机制有两种：共享内存和消息传递。Java的并发采用的是共享内存模型。


===Java内存模型的抽象结构===
3.Java线程之间的通信由Java内存模型（JMM）控制，JMM决定一个线程对共享变量的写入何时对另一个线程可见。
4.从抽象的角度看，JMM定义了线程和主内存之间的抽象关系：线程之间的共享变量存储在主内存（Main Memory）中。每个线程都有一个
私有的本地内存（Local Memory）,本地内存中存储了该线程以读/写共享变量的副本。JMM通过控制主内存与每个线程的本地内存之间的交互，来为Java程序员提供
内存可见性保证。


===源代码到指令序列的重排序===
1.在执行程序时，为了提高性能，编译器和处理器常常会对指令做重排序，重排序分为3中
第一种：编译器重排序。编译器在不改变单线程程序语义的前提下，可以重新安排语义的执行顺序。
第二种：指令级并行的重排序。如果不存在数据依赖性、处理器可以改变语句对应机器指令的执行顺序
第三种：内存系统的重排序。由于处理器使用缓存和读/写缓冲区，这使得加载和存储操作看上去可能是乱序执行。

第一种属于编译器重排序，第二和第三中属于处理器重排序。
对于编译器，JMM的编译器重排序规则会禁止特定类型的编译器重排序（不是所有的编译器重排序都要禁止）
对于处理器重排序，JMM处理器重排序规则规则会要求Java编译器在生成指令序列时，插入特定类型的内存屏障（Memory Barriers）指令，通过内存屏障指令来禁止特定类型
的处理器重排序。


===并发编程模型的分类===
1.常见的处理器都允许Store-Load重排序，都不允许对存在数据依赖的操作做重排序。
2.为了保证内存可见性，Java编译器在生成指令序列的适当位置会插入内存屏障指令来禁止特定类型的处理器重排序。JMM把内存屏障指令分为4类
第一类：LoadLoad Barriers
第二类：StoreStore Barries
第三类：LoadStore Barries
第四类：StoreLoad Barries

StoreLoad Barries是一个“全能型”的屏障，它同时具有其他3个屏障的效果。执行该屏障开销会很昂贵，因为当前处理器通常要把写缓冲区中的数据全部刷新到内存中


===happens-before简介===
1.从JDK1.5开始，Java使用新的JSR-133内存模型。JSR-133使用happens-before的概念来阐述操作之间的内存可见性。
2.在JMM中，如果一个操作执行的结果需要对另一个操作可见，那么这两个操作之间必须要存在happens-before关系。这里提到的两个操作既可以在一个线程之内，也可以是在
不同线程之间
3.与程序员密切相关的happens-before规则如下：
第一：程序顺序规则：一个线程的每个操作，happens-before于该线程中的任意后续操作
第二：监视器锁规则：对一个锁的解锁，happens-before于随后对这个锁的加锁
第三：volatile变量规则：对于一个volatile域的写，happens-before于任意后续对这个volatile域的读
第四：传递性：如果A happens-before B，且B happens-before C，那么A happens-before C。

===重排序===
1.编译器和处理器可能会对操作做重排序。处理器和编译器在重排序时，会遵守数据依赖性，编译器和处理器不会改变存在数据依赖关系的两个操作的执行顺序
，这里所说的数据依赖性仅针对单个处理器中执行的指令序列和单个线程中执行的操作。不同处理器之间和不同线程之间的数据依赖性不被编译器和处理器考虑。
2.as-if-serial:意思是不管怎么重排序（编译器和处理器为了提高并行度），（单线程）程序的执行结果不能被改变。编译器、runtime和处理器都必须遵守as-if-serial语义
3.在单线程程序中，对存在控制依赖性的操作重排序，不会改变执行结果（这也是as-if-serial语义允许对存在控制依赖性的操作做重排序的原因）；但在多线程程序中，对存在控制依赖性
的操作做重排序，可能会对改变程序的执行结果。

===顺序一致性===
1.JMM对正确同步的多线程程序的内存一致性做了如下保证：
如果程序是正确同步的额，程序的执行将具有顺序一致性——即程序的执行结果与该程序在顺序一致性内存模型中的执行结果相同。这里的同步是指广义上的同步，包括对常用同步原语（
synchronized、volatile和final）的正确使用。

2.在JSR-133之前的旧内存模型中，一个64位long/double型变量的读/写操作可以被拆分为两个32位的读/写操作来执行。从JSR-133内存模型开始（即从JDK5开始），仅仅只允许把一个
64位long/double型变量的写操作拆分为两个32位的写操作来执行，任意的读操作在JSR-133中都必须具有原子性（即任意读操作必须要在单个事务中执行）。


===volatile的内存语义===
1.volatile变量自身具有下列特性
a.可见性：对一个volatile变量的读，总是能看到（任意线程）对这个volatile变量最后的写入
b.原子性：对任意单个volatile变量的读/写具有原子性，但类似于volatile++这种复合操作不具有原子性

2.从JSR-133开始（即从JDK5开始），volatile变量的写-读可以实现线程之间的通信。
3.从内存语义的角度来说，volatile写-读与锁的释放-获取有相同的内存效果：volatile写和锁的释放有相同的内存语义；voliatile读与锁的获取有相同的内存语义。

4.当写一个volatile变量时，JMM会把该线程对应的本地内存中的共享变量值刷新到主内存。
5.当读一个volatile变量时，JMM会把该线程对应的本地内存置为无效。线程接下来从主内存中读取共享变量。
6.总结：
a.线程A写一个volatile变量，实质上是线程A向接下来将要读这个volatile变量的某个线程发出了（其对共享变量所做修改的）消息
b.线程B读一个volatile变量，实质上是线程B接收了之前某个线程发出的（在写这个volatile变量之前对共享变量所做修改的）消息
c.线程A写一个volatile变量，随后线程B读这个volatile变量，这个过程实质上是线程A通过主内存向线程B发送消息

7.volatilen内存语义的实现
为了实现volatile内存语义，JMM会分别限制编译器和处理器重排序。规则如下：
当第二个操作是volatile写时，不管第一个操作是什么，都不能重排序
当第一个操作是volatile读时，不管第二个操作是什么，都不能重排序
当第一个操作是volatile写时，第二个操作是volatile读时，不能重排序

为了实现volatile的内存语义。编译器在生成字节码时，会在指令序列中插入内存屏障来禁止特定类型的处理器重排序。

在X86处理器仅会对写-读操作做重排序，所以JMM仅需要在volatile写后面插入一个StoreLoad屏障即可正确实现volatile写-读的内存语义。这意味着在X86
处理器中，volatile写的开销比volatile读的开销会大很多（因为执行StoreLoad屏障开销会比较大）

8.JSR-133为什么要增强volatile的内存语义
在JSR-133之前的旧Java内存模型中，虽然不允许volatile变量之间重排序，但旧的Java内存模型允许volatile变量与普通变量重排序。
因此在旧的内存模型中，volaitle写-读没有锁的释放-获取所具有的内存语义。为了提供一种比锁更轻量级的线程之间通信的机制，JSR-133专家组
决定增强volatile的内存语义。

由于volatile仅仅保证对单个volatile变量的读/写具有原子性，而锁的互斥性执行的特性可以确保对整个临界区代码的执行具有原子性。
在功能上，锁比volatile更强大；在可伸缩性和执行性能上，volatile更有优势。


===锁的内存语义===
1.锁的释放-获取建立的happens-before关系
锁是Java并发编程中最重要的同步机制。锁除了让临界区互斥执行外，还可以让释放锁的线程向获取同一个锁的线程发送消息。
2.锁的内存语义：当线程释放锁时，JMM会把该线程对应的本地内存中的共享变量刷新到主内存中。当线程获取锁时，JMM会把该线程对应的本地内存置为
无效。从而使得被监视器保护的临界区代码必须从主内存中读取共享变量。
3.总结：
a.线程A释放一个锁，实质上是线程A向接下来将要获取这个锁的某个线程发出了（线程A对共享变量所做修改的）消息
b.线程B获取一个锁，实质上是线程B接收了之前某个线程发出的（在释放这个锁之前对共享变量所做的修改）消息
c.线程A释放锁，随后线程B获取这个锁，这个过程实质上线程A通过主内存向线程B发送消息

4.锁内存语义的实现
以ReentrantLock的源码分析锁的内存语义的实现

ReentrantLock的实现依赖于Java同步器框架AbstractQueuedSynchronizer(AQS)。AQS使用一个整型的volatile变量（命名为state）来维护同步状态。

对公平锁和非公平锁的内存语义做个总结：
a.公平锁和非公平锁释放锁时，最后都要写一个volatile变量state
b.公平锁获取时，首先会去读volatile变量
c.非公平锁获取时，首先会利用CAS更新volatile变量，这个操作同时具有volatile读和volatile写的内存语义

从对ReentrantLock的分析可以看出，锁释放-获取的内存语义的实现至少有下面两种方式
第一种：利用volatile变量的写-读所具有的内存语义
第二种：利用CAS所附带的volatile读和volatile写的内存语义

5.concurrent包的实现

1.由于Java的CAS同时具有volatile读和volatile写的内存语义，因此Java线程之间的通信现在有了下面4中方式
a.A线程写volatile变量，随后B线程读这个volatile变量
b.A线程写volatile变量，随后B线程用CAS更新这个volatile变量
c.A线程用CAS更新一个volatile变量，随后B线程用CAS更新这个volatile变量。
d.A线程用CAS更新一个volatile变量，随后B线程读这个volatile变量

2.volatile变量的读/写和CAS可以实现线程之间的通信。把这些特性整合在一起，就形成了整个concurrent包得以实现的基石。
AQS、非阻塞数据结构和原子变量类，这些concurrent包中的基础类都是使用这种模式来实现的，而concurrent包中的高层类又是
依赖于这些基础类来实现的。

===final域的内存语义===
1.final域的重排序规则
对于final域，编译器和处理器要遵守两个重排序队则。
a.在构造函数内对一个final域的写入，与随后把这个被构造对象的引用赋值给一个引用变量，这两个操作之间不能重排序。
b.初次读一个包含final域的对象的引用，与随后初次读这个final域，这两个操作之间不能重排序。

2.final语义在处理器中的实现
由于X86处理器不会对写-写操作做重排序，所以在X86处理器中，写final域需要的StoreStore屏障会被省略掉。同样，由于X86处理器不会对存在间接
依赖关系的操作做重排序，所以在X86处理器中，读final域需要的LoadLoad屏障也会被省略掉。也就是说，在X86处理器中，final域的读/写不会插入任何内存屏障！

3.JSR-133为什么要增强final的语义
在旧的Java内存模型中，一个最严重的缺陷就是线程可能看到final域的值会改变，为了修补这个漏铜，增强了final的语义。


===双重检查锁与延迟初始化===
1.双重检查锁的问题在于第一次读取到的instance不为空时，instance还没有完全初始化，解决方案有以下两种
a.基于volatile的解决方案
b.基于类初始化的解决方案




限流一般会用到Guava包中的RateLimit服务类






