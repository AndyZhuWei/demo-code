# 1监控CPU
大多数操作系统的CPU分为用户态CPU使用率和系统态CPU使用率两类。用户态CPU使用率是指执行应用程序代码的时间占总CPU时间的百分比。系统态CPU使用率高意味着
共享资源存在竞争或者I/O设置之间存在大量的交互。
提高应用程序性能和扩展性的目标时我们可以指定为尽可能降低系统态CPU的使用率
对于计算密集型应用程序来说，不仅要监控用户态和系统态CPU使用率，还需要进一步监控每时钟指令数(IPC)或每指令时钟周期(CPI)等指标。
##1.1监控工具
#1.1.1windows篇
*任务管理器*
#1.1.2 Linux篇
GNOME System Monitor
Top命令
HTop命令
vmstat
Sar工具
mpstat命令
pidstat
jstack命令

#1.2 监控内存
#1.2.1windows篇
*Perfmon*
#1.2.2 Linux篇
free命令
/proc/meminfo
JMap方式 JHat
VisualVM
atop
vmstat
sar
#1.3 监控磁盘
#1.3.1windows篇
*Perfmon*
#1.3.2 Linux篇
iostat
df命令
vmstat命令
sar工具
#1.4 监控网络
#1.4.1windows篇
*Perfmon*
#1.4.2 Linux篇
netstat命令
ifstat工具
iftop工具
nload工具

#2 监控JVM活动
##2.1 监控目的
一般来说，垃圾收集分两种，即次要垃圾收集(也称为新生代垃圾收集，称为Minor GC)和主要垃圾收集(Full GC).
Minor GC收集新生代，Full GC通常会收集整个堆，包括新生代、老年代和永久代，除了将新生代中的活跃对象提升到老年代之外，还会压缩整理老年代和永久代。
因而Full GC之后，新生代为空，老年代和永久代也压缩整理并只有活跃对象
如果各项参数设置合理，系统没有超时日志，GC频率不高，GC耗时不高，那么没有必要进行GC优化。如果GC时间超过1到3秒，或者频繁GC,则必须优化。如果满足以下指标，一般不需要进行GC
Minor GC执行时间不到50ms
Minor GC执行不频繁，约10秒一次
Full GC执行时间不到1s
FUll GC执行频率不算频繁，不低于10分钟1次

重要的垃圾收集数据包括：
当前使用的垃圾收集器
Java堆的大小
新生代和老年代的大小
永久代的大小
Minor GC的持续时间
Minor GC的频率
Minor GC的空间回收量
Full GC的持续时间
Full GC的频率
每个并发垃圾收集周期内的空间回收量
垃圾收集前后Java堆的占有量
垃圾收集前后新生代和老年代的占有量
垃圾收集前后永久代的占有量
是否老年代或永久代的占有触发了Full GC
应用是否显示调用了System.gc()

##2.2 GC垃圾回收报告分析
我们可以通过-verbosegc来打印出GC日志。这是一个比较重要的启动参数，记录每次gc的日志，于其配合的参数如下：
-XX:+PrintGCDetails,打印GC信息，这是-verbosegc默认开启的选项,经测试需要添加到参数中
-XX:+PrintGCTimeStamps，打印每次GC的时间戳
-XX:+PrintHeapAtGC,每次GC时，打印堆信息
-XX:+PrintGCDateStamps:打印GC日期，适合于长期运行的服务器
-Xloggc:/home/admin/logs/gc.log: 指定打印信息的记录的日志位置

#2.3.1 GCHisto

GCHisto是一个离线分析工具
#2.3.2 JConsole
JConsole是一个JMX兼容的GUI工具,可以连接运行中的Java5或者更高版本的JVM。
用Java5 JVM启动Java应用时，命令行只有添加-Dcom.sum.management.jmxremote,JConsole才能连接，而Java6或更高版本的JVM不需要添加此属性
#2.3.3 VisualVM

jstat -gcutil命令可查看GC情况


通过-XX:UseTLAB设置是否开启TLAB空间，TLAB包含在Eden空间，默认情况下仅占用Eden空间的1%。可以通过-XX:TLABWasteTargetPereent这是TLAB空间所占用Eden空间的百分比大小
，如果在TLAB上分配失败，JVM就会尝试通过使用加锁机制确保数据操作的原子性，从而直接在Eden空间中分配内存，如果Eden空间无法分配内存，JVM就会执行MinorGC,直至最终可以在Eden空间
分配内存为止（如果时大对象直接在老年代分配）

逃逸分析于栈上分配
java堆区已经不再是对象内存分配的唯一选择，如果希望降低GC的回收频率和提升GC的回收效率，那么则可以使用堆外存储技术。目前最常见的堆外存储技术就是利用i逃逸分析技术筛选出未发生逃逸的
对象，然后避开堆区而直接选择在栈帧中分配内存空间。
逃逸分析（Escape Analysis）时JVM在执行性能优化之前的一种分析技术，它的具体目标就是分析出对象的作用域。简单来说当一个对象被定义在方法体内部之后，它的受访权限仅限于方法体内，一旦
其引用被外部v成员引用后，这个对象就因此发生了逃逸，反之如果定义在方法体内的对象并没有被任何的外部成员引用，JVM就会为其在栈帧中分配内存空间。
由于对象直接在栈上分配内存，因此GC就无须执行垃圾回收。栈帧会伴随着方法的调用而创建，伴随着方法的执行结束而销毁，由此可见，栈上分配的对象所占用的空间将随着栈帧的出栈而释放。
在JDK 6u23版本之后，HotSpot中默认就已经开启了逃逸分析，如果使用的是较早的版本，开发人员可以通过-XX:+DoEscapeAnalysis显示开启逃逸分析，以及通过选项-XX:+PrintEscapeAnalysis查看
逃逸分析的筛选结果