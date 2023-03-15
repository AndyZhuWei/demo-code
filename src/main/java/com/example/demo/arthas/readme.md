#用arthas

##thread
thread命令主要查看当前线程信息，查看线程的堆栈。
###thread 查看所有线程的信息
thread 我们可以看到线程的id、名称、状态、占用cpu这些信息。以及一个汇总信息。
###thread --state 线程状态
在线程很多的情况下，我们可以通过thread --state 线程状态命令进行过滤，只展示某种状态的线程
###查看某个id线程堆栈
thread id
下面这个例子比较简单，查看主线程的堆栈信息。
###找出当前阻塞的线程
thread -b
此命令类似于我们使用jdk的jstack pid命令找死锁的线程

##trace
方法内部调用路径，并输出方法路径上的每个节点上耗时。可以帮助找到程序中执行慢的代码，优化性能

###基本用法
trace 类路径 方法名
###按照时间过滤
trace 类路径 方法名 '#cost>时间(ms)'
可以看到过滤以后，观察到的都是时间大于0.5ms的该方法的调用
###trace多个类或者多个函数
trace命令只会trace匹配到的函数里的子调用，并不会向下trace多层。因为trace是代价比较贵的，多层trace可能会导致最终要trace的类和函数非常多。

可以用正则表匹配路径上的多个类和函数，一定程度上达到多层trace的效果。(注意-E参数)
trace -E com.test.ClassA|org.test.ClassB method1|method2|method3

### jad/mc/redefine线上热更新一条龙
jad反编译代码
反编译UserController，保存到 /tmp/UserController.java文件里。

jad --source-only com.example.demo.arthas.user.UserController > /tmp/UserController.java

jad --source-only com.healthlink.bbs.business.pmforward.service.PmForwardNew
修改反编译出来的代码
sc查找加载UserController的ClassLoader
$ sc -d *UserController | grep classLoaderHash
 classLoaderHash   1be6f5c3
 可以发现是spring boot的 LaunchedURLClassLoader@1be6f5c3 加载的。
 
 mc内存编译代码
 保存好/tmp/UserController.java之后，使用mc(Memory Compiler)命令来编译，并且通过-c参数指定ClassLoader：
 
 $ mc -c 1be6f5c3 /tmp/UserController.java -d /tmp
 Memory compiler output:
 /tmp/com/example/demo/arthas/user/UserController.class
 Affect(row-cnt:1) cost in 346 ms
 redefine热更新代码
 再使用redefine命令重新加载新编译好的UserController.class：
 
 $ redefine /tmp/com/example/demo/arthas/user/UserController.class
 redefine success, size: 1
 
###观察方法入参
watch com.healthlink.hbs.business.productMember.controller.ProductMemberController getMemberByPmid '{params,returnObj}' -X 2



























