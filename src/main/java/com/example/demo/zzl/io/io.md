#虚拟文件系统
拷贝多个文件
cp /lib64/{xxxmxxm,mmmm,mmmmfefd} ./lib64
stat xxx.txt 查看文件详细信息
ln xxx ddd.txt 硬链接
ln -s xxx ddd.txt 软链接

硬链接： 与普通文件没什么不同，inode 都指向同一个文件在硬盘中的区块
软链接： 保存了其代表的文件的绝对路径，是另外一种文件，在硬盘上有独立的区块，访问时替换自身路径

演示一个例子
1 创建一个空目录 mkdir zhuwei
2 创建设备文件 dd if=/dev/zero of=mydisk.img bs=1048576 count=100
(/dev/null是黑洞 /dev/zero为无限大的空，但是不占用太多的磁盘空间)
执行完后就会得到一个磁盘文件，里面都是0这样的数据填充的一共100M大小
接下来要做的就是要把找个磁盘文件挂载到我们文件系统当中去
这个步骤其实就是在创建空的磁盘镜像
3 挂载到环回设备 losetup /dev/loop0 mydisk.img
4. 格式化 mke2fs /dev/loop0
5. 将虚拟的环回设备挂载到文件系统
mount -t ext2 /dev/loop0 /mnt/ooxx
操作成功后，以后我们再访问/mnt/ooxx就不是以前根目录上的那个ooxx了，而是环回设备中的内容
现在我们就基于找个目录再做一些操作
6.再找个目录中新建一个bin目录 然后拷贝bash命令过来
cp /bin/bash bin
ldd bash 可以分析程序的动态链接库有哪些从，然后复制相应的依赖库进来
cp /lib64/{libtinfo.so.5,libdl.so.2,libc.so.6,ld-linux-x86-64.so.2} lib64/
7. chroot ./ 把根目录切换到当前目录，并启动bash
echo $$ 得到当前程序的进程号
当前程序中是没有ll vi cat等 因为当前程序bash中是没有这些程序的
8.echo 'xxxx' > /bac.txt 写入一个文件重定向到更目录，然后退出当前程序exit
9.退出后再当前系统中根目录中式没有bac.txt文件，这个文件其实是再/mnt/ooxx的根目录下

以上的操作最终都是在我们最初创建的那个磁盘文件中，故有一切皆文件之说
如果我们卸载这个umount /mnt/ooxx后就看不到 ooxx中的目录文件呢，但其实我们的磁盘文件
mydisk.img还在,只要挂载上就可以看到

docker中首先就需要创建镜像文件image，image中就要放我们的程序。
docker是一个容器，是一个虚幻的东西。里面跑的是进程。
没有操作系统内核，所有docker复用的是物理机的内核。
有镜像文件后才有容器概念，镜像在磁盘中就是image，要先挂载，切换根目录
读取进程中的执行文件，在准备命名空间。最后跑在命名空间中。这源于整个虚拟文件系统的支持。

###显示进程打开了哪些文件
lsof -p $$ 打开当前进程打开了哪些文件
exec 8< ooxx.txt 用8读取ooxx.txt文件内容
这样在打开lsof -p $$就可以看到相应的文件描述符8了且FD那列是8r 这个r表示的就是读取 TYPE
那一列就是REG表示普通文件
lsof -op $$ 可以输出偏移量


#文件描述符
文件描述符就是描述你打开文件的一些信息，偏移量 类型 位置 node号 设备等

read a 0<& 8 用read程序读取文件描述符8的内容，0表示read程序的标准输入，read读取到换行符就不继续读取了
此时在观察losf -op $$时发现文件描述符8的偏移量就不是0了
如果用其他程序打开相同的文件，各个程序中的fd会维护自己的偏移量。文件的内容在内存中时一份的


dd if/dev/sda3 of=~/back.img 将物理硬盘sda3分区进行备份

cat /proc/vmstat | grep dirty 查询脏页

exec 3> andy.txt 创建一个写文件描述符
此时查询脏页 发现没有
如果我们写入一些东西到3号文件描述符
echo '234324' >& 3
此时在查看发现有了脏页数据
查询刷新脏页的阈值
sysctl -a | grep dirty

/proc 映射的内核的一些变量属性，开机后里面才有内容
$$ 当前bash的pid
$BASHPID 也表示当前bash的pid
/proc/$$/fd  命令lsof -op fd

重定向不是命令 是一种机制
任何程序都有标准输入输出
ls ./ 1> ~/ls.out 这句话的意思就是把ls ./下的输出内容重定向到家目录的ls.out 1代表的就是标准输出 >表示输出重定向符号

ls ./ /xxx 1> ls01.out 2> ls03.out
ls ./ /xxx 2>& 1 1> ls04.out 这里需要注意的是如果1前边的&符号，这样做的目的就是说&后边接的1是文件描述符，否则就会被当作是1文件了
ls ./ /xxx  1> ls04.out 2>& 1

管道 | 

pstree

{ echo "ssss"; echo "123123"; }

管道类型的文件，以下程序会用到
{ a=9; echo "adfadf"; } | cat
bash在解析以上命令时会在管道命令符左右各启动一个子进程来执行，cat需要前面
进程的输出，则会通过管道类型的文件进行连接

echo $$ | cat   $$优先级高于| 所以先把$$替换成父进程的PID,然后在执行|，进而开启左右两个子进程。
echo $BASHPID | cat $BASHPID低于| 所以会开启左右两个子进程来执行

{ echo $BASHPID; read x; } | { cat ; echo $BASHPID ; read y;} 通过这个可以看到PIPE文件类型



#pagecache
当用户进程调用 read(fd8)会执行
system call 系统调用 int 0x80
其中int是cpu的指令 0x80是128。
这个值会存储到cpu中的寄存器中。用于和中断描述符表进行比对，
表中的128表示call back方法。cpu看到call back后就会保护现场、执行用户态和内核态的切换
在内核态是操作系统就会去读取相应硬盘中的信息，在内核中开辟一个pageCache，大小4k。就算读取的是1个字节，
也会开辟4k的pagecache空间存储内容。
在硬盘中其实也有缓冲区。
DMA协处理器，可以直接让数据不经过CPU直接到内存


sysctl -a | grep dirty
可以从/etc/sysctl.conf中修改
完事可以使用sysctl -p使其生效

pageCache内核维护 中间层 使用多大内存呢？
通过以下参数配置
vm.dirty_backgroud_ratio=90 内核业务线程计算内存，如果占有90%就开始同步到磁盘
vm.dirty_ration=90 前台分配页时达到90%则会阻塞程序
vm.dirty_writeback_centisecs=5000 后台业务线程写回的时间
vm.dirty_expire_centisecs=30000 脏页生命周期可以存多久

程序申请到页的时候就是脏页了，如果把脏页写到了磁盘就是脏页了，此时就会同各国LRU进行
淘汰，如果是脏页则必须先同步到磁盘。

pcstat out.txt 这个命令可以查询out.txt被pageCache缓存的状态


pagecache是优化IO性能，缺点就是会丢失数据

程序中的线性的逻辑地址需要通过CPU的MMU进行解析，解析成page物理内存映射表的的地址进行访问
#文件系统的IO

通过家目录中的mysh脚本进行演示
ll -h && pcstat out.txt 通过输出观察页缓存的情况

在mysh脚本中有一个追踪代码执行的命令strace 
```
rm -rf *out*
/opt/jdk1.8/bin/javac OSFileIO.java
strace -ff -o out /opt/jdk1.8/bin/java OSFileIO $1
```
##普通写
我们打开其中一个文件，有类似如下的应用程序对系统内核调用的方法
write(4,"123456789\n",10) 这个其实就是基本写入操作调用的方法，我们在看基于buffer的io调用
##带buffer写
write(4,"123456789\n123456789\n".......,8910) 基于buffer的调用一次会把buffer中的数据一次写入到内核

##基于byteBuffer
ByteBuffer buffer = ByteBuffer.allocate(1024); 在堆内分配
ByteBuffer buffer = ByteBuffer.allocateDirect(1024); 在堆外分配

byteBuffer的集中常用api
flip get put compact clear

##堆外映射写
只有文件上的Channel有map也就是FileChannel的map方法。即把文件和pageCache映射起来。
//调用map就是调用系统的mmap 是堆外的和文件直接映射
MappedByteBuffer map = fileChannel.map(FileChannel.READ_WRITE,0,4096);
map.put("xxxx".getBytes());//不是系统调用，但是数据会到达内核的pagecache,
//曾经我们是需要out.write()这样的系统调用，才能让程序的data进入内核的pagecache
//曾经必须有用户态和内核态的切换
//mmap的内存映射，依然是内核的pagecache体系约束的
//换言之，丢数据
//你可以去github上找一些 其他C程序员写的jni扩展库，使用linux内核的Direct IO
//直接IO是忽略linux的pagecache
//是把pagecache  交给了程序自己开辟一个字节数组当作pagecache，动用代码逻辑来维护一致性/dirty。。。一系列复杂问题


#内存和IO关系


各IO性能
on head < off head < mapped(file)

#网络IO

tcpdump -nn -i eth0 port 9090 监控网络状态

###演示网络建立的过程
1.首先再一个标签页中启动服务器端，
javac SocketIOPropertites.java && java SocketIOPropertites
启动后通过netstat -natp可以发现有一行数据：
tcp6       0      0 :::9090                 :::*                    LISTEN      17276/java
表明服务端会开启LISTEN状态，并申请了端口号9090
后边客户端连接分为两个步骤，首先是和9090建立三次握手，握手后服务端就会开启一个线程来处理这个请求，其中17276就是监听的程序进程号
我们可以通过lsof -op 17276来查看这个进程打开的一些文件描述符，可以发现有一个6的文件描述
java    17276 root    6u  IPv6             191299       0t0      TCP *:websm (LISTEN)
再服务端server.accept()之前，如果有请求到达服务器，就已经通过三次握手建立了相应的资源
tcp6       0      0 192.168.80.100:9090     192.168.80.100:60925    ESTABLISHED - 
再客户端处发送数据，此时内核也会进行接收
如果accept就会分配相应的文件描述符给程序进行操作
此时netstat -natp显示如下：
tcp6       0      0 192.168.80.100:9090     192.168.80.100:60925    ESTABLISHED 17276/java 
lsof -p 17276如下
java    17624 root    7u  IPv6             194869       0t0      TCP localhost:websm->localhost:60925 (ESTABLISHED)
分配了相应的文件描述符

附加：
另一个启动客户端
javac SocketClient.java && java SocketClient
还有一个监控网络连接
netstat -natp
再打开一个监控tcp包
tcpdump -nn -i eth0 port 9090

###演示backlog(backlog的大小设置为2，默认好像是50 可以调整)
再没有accept的时候
多启动几个客户端
通过netstat -natp可以发现
tcp6       0      0 192.168.80.100:9090     192.168.80.100:60928    ESTABLISHED -                   
tcp6       0      0 192.168.80.100:9090     192.168.80.100:60929    ESTABLISHED -    
tcp6       0      0 192.168.80.100:9090     192.168.80.100:60930    ESTABLISHED -
都建立了链接，但是没有分配相应的程序来处理
当再启动一个客户端的时候显示如下
tcp        0      0 192.168.80.100:9090     192.168.80.100:60931    SYN_RECV    -
SYN_RECV表示客户端没有收到确认包，过一会这个显示就没有了

###服务端timeout
server.setSoTimeout(SO_TIMEOUT);
表示再accept阻塞时等待的时长，如果没有请求会抛异常
###客户端timeout
client.setSoTimeout(CLI_TIMEOUT);
表示读取客户端消息时的超时时长

###测试抓包工具
tcpdump -nn -i etho port 9090
然后启动测试的服务程序，
用 nc ip port来进行测试
再同一台服务器上测试无效，必须时两台机器进行测试
数据包的大小
MTU 整个数据包的大小
MMS 数据包中的自己的数据内容大小
tcp三次握手的时候会协商两边数据包的大小
窗口机制解决了拥塞，如果服务器没有余量接收数据包，再ack给客户端的时候会显示窗口大小
没有余量，此时客户端就阻塞不会再发送了。等后续服务端处理一下，有余量了会补送一个数据包，
让其客户端继续发送

###演示客户端发送数据包，服务端再内核缓存区接收
前提：服务器端还没有应用程序接收客户端的请求。
再次前提下看看服务端内核可以缓存多少数据内容，如果超过了时什么情况？
实验证明，再缓冲区增长到1920时就不会再保存，新来的内容会被丢弃
这个内核缓冲区的大小也是可以配置的

###设置客户端发送的缓冲区大小
按以下参数进行演示
client.setSendBufferSize(20);
client.setTcpNoDeplay(false);//false时默认值，默认时开启了优化
client.setOOBInline(false);//是不是着急把数据的第一个字节发送出去了
开启优化后，数据包会攒到一块 一起发送出去
不开启优化后，数据包会根据内核的调度能发送就立刻发送出去了

###演示keepalive
将服务器端的keepalive设置为true
在tcp协议中，如果双方都将建立了连接，如果长时间没有发送数据包，双方怎么确认对方还活着？
这时就出现了一个keepalive,会自动发送心跳包，来确认对方还活着

在http协议中也有一个keepalive

在负载均衡中也有一个keepalived


###网络IO的演进和变化 模型
同步：是需要我自己的app进行读取
异步：不需要我自己读取，内核直接帮我们读取放到缓存，然后我们直接拿

阻塞：当调用读取时，如果还不能读，则不能干其他事情，阻塞住
非阻塞：当调用读取时，如果还不能读，则能干其他事情，不阻塞

strace -ff -o out cmd 追踪程序线程系统调用的情况，例如
strace -ff -o out java SocketIO

在linxu中还没有实现异步
没有异步阻塞模型

演示使用的程序时SocketIO
执行完strace -ff -o out java SocketIO
在当前目录下就会出现一些out前缀的文件，表示的就是当前程序各个线程系统调用的一些记录
我们可以找到我们输出语句的系统调用如下：
write(1, "step1: new ServerSocket(9090) ", 30) = 30
其中1表示的就是标准输出屏幕
通过jps -l得到当前程序的pid
然后lsof -p pid 可以发现当前程序打开了文件描述符如下：
java    21160 root    6u  IPv6             225532       0t0      TCP *:websm (LISTEN)
6为当前服务端监听程序打开的文件描述符，在追踪文件中我们还可以发现以下信息
socket(PF_INET6, SOCK_STREAM, IPPROTO_IP) = 6
setsockopt(6, SOL_IPV6, IPV6_V6ONLY, [0], 4) = 0
bind(6, {sa_family=AF_INET6, sin6_port=htons(9090), inet_pton(AF_INET6, "::", &sin6_addr), sin6_flowinfo=0, sin6_scope_id=0}, 28) = 0
listen(6, 20)                           = 
其中的6就是我们监听程序打开的文件描述符6
任何服务端程序都有这些系统调用
当客户端调用来到的时候，系统调用就会出现以下记录
accept(6, {sa_family=AF_INET6, sin6_port=htons(59524), inet_pton(AF_INET6, "::ffff:192.168.80.10", &sin6_addr), sin6_flowinfo=0, sin6_scope_id=0}, [28]) = 7
其中的7表示创建连接后的文件描述符
通过lsof 查看如下
java    21160 root    7u  IPv6             228560       0t0      TCP 192.168.80.100:websm->localhost:59524 (ESTABLISHED)
java代码中的new一个线程其实就是内核中克隆一个进程
clone(child_stack=0x7f0b547fefb0, flags=CLONE_VM|CLONE_FS|CLONE_FILES|CLONE_SIGHAND|CLONE_THREAD|CLONE_SYSVSEM|CLONE_SETTLS|CLONE_PARENT_SETTID|CLONE_CHILD_CLEARTID, parent_tidptr=0x7f0b547ff9d0, tls=0x7f0b547ff700, child_tidptr=0x7f0b547ff9d0) = 21162
其中的21162就是克隆出来的进程
在java中的read操作对应系统调用就是
recv(7,

###linux中的帮助程序
man 2 后边可以跟1到8
或者
man ip 
或man 7 ip

###总结
不管什么io模型，都要执行以下三步
1.socket=fd;  调用socket得到文件描述符
2.bind(fd3,8090);绑定文件描述符到指定端口
3.listen(fd3);在文件描述符fd3上监听
只有执行完以上三步，在用netstat -natp时才会看到四元组这个条目
0.0.0.0:8090  0.0.0.0:*  LISTEN
以上3步都是内核实现的

监听有了只有就开始了accept阻塞等待客户端的连接
accept(fd3,--->fd5  等阻塞过了就有一个客户端连接返回
读取的时候recv(fd5--->
###BIO
在BIO时代就是主线程中阻塞在accept,建立连接后开启新线程阻塞在rece上进行数据读取
  把这两个阻塞的方法分开
  
###C10k问题
C10K问题由来
随着互联网的普及，应用的用户群体几何倍增长，此时服务器性能问题就出现。
最初的服务器是基于进程/线程模型。新到来一个TCP连接，就需要分配一个进程。
假如有C10K，就需要创建1W个进程，可想而知单机是无法承受的。
那么如何突破单机性能是高性能网络编程必须要面对的问题，
进而这些局限和问题就统称为C10K问题，最早是由Dan Kegel进行归纳和总结的，
并且他也系统的分析和提出解决方案。

C10K问题的本质
C10K问题的本质上是操作系统的问题。对于Web 1.0/2.0时代的操作系统，
传统的同步阻塞I/O模型处理方式都是requests per second。
当创建的进程或线程多了，
数据拷贝频繁（缓存I/O、内核将数据拷贝到用户进程空间、阻塞，
进程/线程上下文切换消耗大， 导致操作系统崩溃，这就是C10K问题的本质。

可见, 解决C10K问题的关键就是尽可能减少这些CPU资源消耗。

C10K问题的解决方案
从网络编程技术的角度来说，主要思路：

每个连接分配一个独立的线程/进程
同一个线程/进程同时处理多个连接

select方式：使用fd_set结构体告诉内核同时监控那些文件句柄，使用逐个排查方式去检查是否有文件句柄就绪或者超时。该方式有以下缺点：文件句柄数量是有上线的，逐个检查吞吐量低，每次调用都要重复初始化fd_set。
poll方式：该方式主要解决了select方式的2个缺点，文件句柄上限问题(链表方式存储)以及重复初始化问题(不同字段标注关注事件和发生事件)，但是逐个去检查文件句柄是否就绪的问题仍然没有解决。
epoll方式：该方式可以说是C10K问题的killer，他不去轮询监听所有文件句柄是否已经就绪。epoll只对发生变化的文件句柄感兴趣。其工作机制是，使用"事件"的就绪通知方式，通过epoll_ctl注册文件描述符fd，一旦该fd就绪，内核就会采用类似callback的回调机制来激活该fd, epoll_wait便可以收到通知, 并通知应用程序。而且epoll使用一个文件描述符管理多个描述符,将用户进程的文件描述符的事件存放到内核的一个事件表中, 这样数据只需要从内核缓存空间拷贝一次到用户进程地址空间。而且epoll是通过内核与用户空间共享内存方式来实现事件就绪消息传递的，其效率非常高。但是epoll是依赖系统的(Linux)。
异步I/O以及Windows，该方式在windows上支持很好，这里就不具体介绍啦。

###演示
通过再一个客户端中启动1w多个客户端(C10Client)的连接测试BIO的服务器端（选用SocketIOProperties），可以发现连接建立很慢。主要原因就是BIO的阻塞和创建新线程很慢。
阻塞的原因就是因为操作系统内核提供给我们监听连接和操作客户连接的方法是阻塞的，所以就需要操作系统进行改善。

###NIO
用服务器版本SocketNIO来进行演示
strace -ff -o out java SocketNio
可以发现再执行accept的时候就不会再阻塞了，而是一个线程一致再循环中执行
通过查看程序调用的系统方法发现再执行accept的时候会直接返回-1，如下
accept(5, 0x7f4d340f15c0, [28])         = -1 EAGAIN (Resource temporarily unavailable)
并不会像执行那样一直阻塞住

这样做的意义就是用一个线程就可以处理连接和客户端的读写.不会因为一个客户的读阻塞而影响其他客户的连接或读写
我们可以用nc localhost 9090 来测试

现在也可以用C10Client来进行压测，从测试结果我们发现了以下现象
a.随着客户端连接越来越多，程序会变慢，主要原因是我们服务器端保存的客户端的连接多了以后，每次循环遍历都要调用内核方法read。引起用户态到内核态的切换，所以会变慢
b.执行一段时间后，系统会报以下错误
Exception in thread "main" java.io.IOException : Too many open files

我们可以通过ulimit -a来查看系统目前设置的值
open files                      (-n) 1024
可以看到系统默认设置的是1024 表示一个进程可以打开多少个文件描述符(普通用户启动的进程严格首先于此，root用户会超过这个限制)
通过以下命令来进行设置（用户进程级别）
ulimit -SHn 500000 //S软设置 H硬设置 -n表示open files

cat /proc/sys/fs/file-max   385915 
这个值表示的就是当前操作系统内核一共可以开辟的文件描述符的数量。大概就是1G内存能开辟10w个描述符

NIO比BIO快，但是NIO其实也不是很快，问题出现在哪里了？
NIO优点：通过1个或几个线程，来解决N个IO连接的处理

问题：
在NIO的时候虽然我一个线程可以解决所有事情，但是每一个操作都需要触发一个系统调用，是由软件程序主动的，无论是接收我们的客户端accept还是我们尝试读取客户端内容read，
有读到还是没有读到这样的一个操作都是向内核调用，内核给程序反馈。如果是一个C10K的情况，则每循环一次都是一个O(n)的复杂的
在这么多循环次数中可能有数据的仅仅是其中少数，大部分的系统调用都是浪费。
要解决这个问题就出现了多路复用器


###多路复用器
多路复用器其实是对非阻塞IO的一种延续，底层用的是epoll

多路复用器就是多条路（IO）通过一个系统调用，获得其中IO状态，然后，由程序对由状态的IO进行读写
注意：只要程序自己读写，你的IO模型就是同步的

常用的IO模型组成就是：
同步阻塞：程序自己读取，调用方法一直等待有效返回结果
同步非阻塞：程序自己读取，调用方法一瞬间，给出是否读到（自己要解决下一次什么时候在去读）

异步：尽量不要去讨论，因为我们只讨论IO模型下，linux，目前没有通用内核的异步处理方案。

异步非阻塞
异步阻塞是没有意义的

多路复用器可以是以下几种
select POSIX
POLL
EPOLL

###select POSIX
synchronous I/O multiplexing 同步的IO多路复用器

select有一个FD_SETSIZE的限制 这个限制好像是1024 表示在使用select的时候只能传递1024个描述符，现在这个基本已经很少使用了
int select(int nfds, fd_set *readfds, fd_set *writefds,
                  fd_set *exceptfds, struct timeval *timeout);
其中fd_set就是不能超过FD——SETSIZE设置的大小

运用select后我们的用户程序应该就是这个样子
while {
 select(fds) O(1)的复杂度
 rece(fd)  O(m) m的复杂度，返回m个准备好的IO
}

###POLL
POLL和select一样但是没有1024的限制

###小总结
其实，无论NIO、SELECT、POLL都是要遍历所有的IO询问状态
只不过，在NIO中这个遍历的过程成本在用户态和内核态的切换
在select、poll（多路复用器-阶段1）下这个遍历的过程触发了一次系统调用，用户态内核态的切换，过程
中，把fds传递给内核，内核重新根据用户这次调用传过来的fds，遍历，修改状态


select、poll的弊端
1.select、poll每次都要重新传递fds
2.每次，内核被调用之后，针对这次调用，触发一个遍历fds全量的复杂度

kernel其实就是一个程序，在内存中我们由kernel和我们自己的用户程序。
中断有硬中断和软中断，软中断其实就是我们程序中调用系统内核的方法。也就是cpu读取到了我程序中的一个指令
这个指令就是一个int 80（中断前3个字母），中断会关联一个表，这个表叫中断向量表，在中断向量表中一个1字节表示
255个正数，其中有80。后边还有一个callback。cpu找到后会进行寄存器切换，保存现场等操作

时钟中断（晶振）是硬中断，这个中断可以让我们内存中的程序来回切换

IO中断（包括网卡、键盘、鼠标）是硬中断

我们说网卡产生的中断：
比如有一个客户端的数据包到达了网卡，这个时候网卡会发生IO中断，这个中断就会打断CPU，
CPU就不会处理内存中的程序，先把程序放一边，没有进行挂起，未来还会接着跑。CPU把从网卡
读到的数据复制到内存中。

（在内存空间中其实有和网卡对应的内存空间DMA,在内核的网卡驱动启动是就是在内存中开辟这个空间，网卡
产生中断后，cpu会直接把数据放到这个区域）
其实网卡中是有buffer。
网卡触发中断有3个级别
1.来了数据包就产生中断（package）
2.网卡来了一批数据（buffer） 一般都是这种级别
3.轮询（数据包来的太频繁） cpu专门拿出轮询时间来拷贝数据，不需要频繁被打断

这些级别内核可以根据网卡的工作频率来动态调整



最终：有中断就有回调
event事件 --> 回调处理事件

大小、复杂度
在epoll之前的callback只是完成了将网卡发来的数据走内核网络协议栈（2 3 4）最终关联到fd的buffer
所以，你某一时间如果从APP询问内核某一个或某些fd是否r/w,会有状态返回

如果内核在callback处理中再加入到一个list中，未来程序向内核索取时只是把结果集拿走了，
而不用再遍历。这个其实就是epoll的模型


yum install man man-pages man只是帮助命令，会有一些简单的命令用法，man-pages里面才会有一些系统调用的命令用法


###EPOLL
epoll其实有以下3项的系统调用
epoll_create(2)：
epoll_ctl(2)
epoll_wait(2)

再epoll下服务器变化过程
app Server
1.socket -->fd4 创建socket返回文件描述符
2.bind 绑定
3.listen fd4 监听fd4文件描述符
以上和之前的模型都是一样的，接着调用epoll_create，返回文件描述符fd6
4.epoll_create会让内核开辟空间，fd6就是描述的这个空间的文件描述符。
这个空间就会放红黑树，这个调用在其生命周期中只会调用一次。

接着会调用
5.int epoll_ctl(int epfd,  int  op,  int  fd,  struct
       epoll_event *event);
其中epfd说的就是之前创建的fd6
op表示的时操作，包括添加，修改和删除
fd：表示的就是你需要添加，修改和删除的文件描述符
*event：表示你所关注的事件

最后调用epoll_wait
6.epoll_wait
这个调用就是在等待一个链表，这个链表中的数据就是到达的数据，那是怎么到达的呢？
其实就是在网卡中断callback时候由内核拷贝过去的。
之前的中断只是把网卡的数据拷贝到文件描述的buffer中，现在是多了一个延伸处理，这个延伸
处理就是在epoll_create所创建的空间中查找所对应的文件描述符，然后迁移到相应的链表中。
所以程序在未来某个时刻在调用时直接就返回了相应的文件描述符，而不需要在遍历了。

epoll规避了遍历


###上层java的抽象
java中把多路复用器抽象了Selector  在linux中多路复用器可以是（select poll epoll kqueue）
这个可以通过java启动参数指定
-Djava.nio.channels.spi.SelectorProvider=sun.nio.ch.EPollSelectorProvider

示例代码，参见SocketMultiplexingSingleThreadv1
```
public void initServer() {
        try {
            server = ServerSocketChannel.open();
            server.configureBlocking(false);
            server.bind(new InetSocketAddress(port));


            //如果在epoll模型下，open--》  epoll_create -> fd3
            selector = Selector.open();  //  select  poll  *epoll  优先选择：epoll  但是可以 -D修正

            //server 约等于 listen状态的 fd4
            /*
            register
            如果：
            select，poll：jvm里开辟一个数组 fd4 放进去
            epoll：  epoll_ctl(fd3,ADD,fd4,EPOLLIN
             */
            server.register(selector, SelectionKey.OP_ACCEPT);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
```

多种模型用了一套Selector

epoll有一个参数值max_user_watches,允许红黑树的大小














































