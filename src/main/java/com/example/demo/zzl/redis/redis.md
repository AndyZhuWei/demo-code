##常识
1.好久以前数据是存在文件中的
data.txt

随着文件变大，查询速度会变慢，是因为I/O瓶颈。

2.数据库：
数据库出现了，它有一个dataPage是4k
在数据库中的表里面存储数据时反应到物理上就是用一个个4k的小格子来存储的
以前在数据文件中保存的数就散落在这些下格子中。如果只是这样和以前数据文件那种全量查询一样慢，
数据中还有索引，这些索引也是4k，这些存储的是数据的信息。
关系型数据库建表：必须给出schema
类型：字节宽度
存：倾向于行级存储

数据和索引都是存储在硬盘中的，真正用的是时候是在内存中准备一个B+树，
B+树树干在内存中的。
这么做的目的是减少I/O避免磁盘的缺点

如果数据库中的表很大性能就会下降？
如果表有索引 增删改变慢，查询速度会变慢？
1.1个或少量查询依然很多
2.当并发很多到达（数据很多，能被命中的几率就大）会受硬盘带宽影响速度




3.数据存储在磁盘，这个磁盘有两个维度：
磁盘
a.寻址 ms级别
b.带宽 单位时间有多少数据流过去，一般是1/2G或几百M级别

内存：
a.寻找：ns级别 s(秒)>ms(毫秒)>μs(微妙)>ns(纳秒)
磁盘比内存在寻址上慢了10w倍
b.带宽很大

I/O buffer：成本问题
磁盘与磁道，扇区 512Byte再来一个成本变大：索引 
4K 操作系统无论你读多少都是最少4K

4. SAP HANA内存级别关系型数据库（极端）
方案：内存2T 硬件定制 2个亿

数据在磁盘和内存体积不一样

5.折中方案缓存
内存级别的很贵，磁盘级别的性能又是瓶颈，所以就有了折中的方案缓存
用少一些的内存缓存数据：memcached和redis
计算机发展到现在的2个基础设施
a.冯诺依曼体系的硬件
b.以太网，tcp/ip的网络


6.架构师的能力
技术选型和技术对比


##Redis
在redis之前又一个memcached,redis出来后要取代它，为什么？
a.memcached中的value没有类型的概念
 如果我们要保存复杂的数据结构，那么我们就可以用json来表示，那为什么还要有类型的redis呢？
 但是我们客户端想通过缓存系统取回value中的某一个元素。那么memcached返回value所有的数据到client。网卡I/O就会成为瓶颈，客户端还要有相应的代码去解析json。有这么两个复杂度
 如果是redis，它有类型，其实类型不是很重要，重要的是redisService对每种类型都有其相应的方法，如lpop。这样就避免了memacached的缺点。本质解耦，计算向数据移动。


安装
wget https://download.redis.io/releases/redis-6.0.8.tar.gz
tar xf redis-5.0.5.tar.gz

源码安装都是一个套路，先看安装文件中的readme.md
编译命令make是操作系统自带的，是一个编译工具，它需要也给文件就是Makefile（如果没有这个文件，一般还需要执行config命令，才会生成）
redis安装目录中的Makefile并不是真正的编译配置文件，只是一个跳转文件，背后执行的是src目录下的Makefile文件
这个文件中有一个参数PREFIX?=/usr/local 表示如果执行make install时并不指定PREFIX则默认安装目录就是/usr/local

make disclean清理命令 如果第一次没有安装成功，再接着安装时可能需要清理
make执行过后再src目录下就有一些启动服务的执行命令了
但是这样启动比较low
我们可以把这个软件安装成操作系统的服务，所以可以执行make install 或者make install  PREFIX=path 
make install PREFIX=/opt/zhuwei/redis5 这样就会再这个目录下有一个bin,bin就是前边编译后再src目录下生成的那些命令。
这样就和src原本的源码区分开了
如果我们想要以操作系统的服务那样启动redis，则这样做
   在/etc/profile中定义REDIS_HOME环境变量
   执行util目录下有一个install-server.sh的脚本
   install-server.sh这个脚本会以交互式的方式让用户选择程序运行的端口、配置文件、日志文件、数据文件、可执行命令路径等信息
   最终会显示类似如下的信息：
   ```
   Copied /tmp/6379.conf => /etc/init.d/redis_6379
   Installing service...
   Successfully added to chkconfig!
   Successfully added to runlevels 345!
   Starting Redis server...
   Installation successful!
  ```
   然后启动服务的时候就是这样了：servcie redis_6379 start
   
安装上边的过程可以启动多个redis

yum install man man-pages
man 帮助程序 可以看8类程序的文档 man 2 read   read就是2类系统调用的命令


mmap 共享空间（内核态和用户态共享）

man 7 epoll
epoll里面有三个系统调用 epoll_create(2)、epoll_ctl(2)、epoll_wait(2)

零拷贝：系统调用sendfile(out,in)

sendfile+mmap可以组成一个高效的组件就是kafka



db-engines
1.5M ops/sec <1ms redis秒级10w级别速度


###Redis基本操作
redis是单进程单线程处理用户数据请求，还有其他线程再别的事情。
redis的顺序性指的的是每连接内的命令顺序

redis-cli 启动客户端,默认连接的端口是6379，退出执行exit
如果需要查看redis-cli的帮助则执行redis-cli -h
redis默认有16个库 0到15.可以配置 连接时可以加入-n来连接指定的库，例如
redis-cli -n 8
再登陆状态下可以通过select number来切换库,例如select 8

帮助命令
再登陆状态下输入help命令可以显示帮助信息，如下：
To get help about Redis commands type:
      "help @<group>" to get a list of commands in <group>
      "help <command>" for help on <command>
      "help <tab>" to get a list of possible help topics
      "quit" to exit



@<group> @后边写组命令，一般有generic、string、set等，我们可以再输入完@后按键tab可以自动显示
help @generic可以查看一些通用的命令

keys * 显示创建过哪些key
flushdb 清空所有key，生产时不要使用，一般运维也会进行rename



redis支持多种类型指的是key-value中的value


###redis 5大数据类型：string、hashs、lists、sets、sorted sets









