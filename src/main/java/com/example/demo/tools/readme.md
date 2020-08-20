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
-XX:+PrintGCDetails,打印GC信息，这是-verbosegc默认开启的选项
-XX:+PrintGCTimeStamps，打印每次GC的时间戳
-XX:+PrintHeapAtGC,每次GC时，打印堆信息
-XX:+PrintGCDateStamps:打印GC日期，适合于长期运行的服务器
-Xloggc:/home/admin/logs/gc.log: 指定打印信息的记录的日志位置
