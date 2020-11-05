# 2020-10-10 笔记
##演示在linux中不用curl或者wget请求百度的网页信息
###应用层
在Linux中一切皆文件
1.cd /proc/$$/fd  这句话的意思时进入linux系统的进程文件中的文件描述符中，$$表示的就是当前解释程序的进程id,fd表示当前进程id的文件描述符。
                   执行后，我们执行pwd就会显示类似/proc/115601/fd。其中115601就表示的当前解释程序的进程id，在当前路径下执行ll可以显示出fd
                   中的所有文件描述符，其中0、1、2、255都是表示的该进程中各个文件描述符，分别表示输入、输出、错误和是一个小技巧,bash用于在重定向
                   时保留这些副本.这是特定于bash
2.exec 8<> /dev/tcp/www.baidu.com/80  其中/dev/tcp/www.baidu.com/80 这个路径表示的时建立tcp链接，
                                      www.baidu.com为对应的域名地址
                                      80为相应的端口号
                                      8<> 表示用文件描述符8表示对应链接到相应网址的输入输出，此时在当前目录中ll会发现
                                      有一个文件描述符8指向了百度的socket的链接
3.exec 8<& -    停止相应的文件描述符
4.echo -e 'GET / HTTP/1.1\n' >& 8  向文件描述符发送请求百度的http协议 其中-e用于解析换行符
5.cat <& 8      读取百度返回的内容信息，也就是相应的html代码


###传输控制层
备注：在tcp四层协议中，应用层时处于用户态，下面的各层都是操作系统内核态来实现的

tcp是面向连接可靠的传输协议
三次握手和四次分手
一个机器上有65535个端口号

三次握手完了之后才是数据传输，在没有数据传输时双方需要四次分手才可以销毁相应的资源。这个过程是一个最小粒度不可分割

netstat -natp   n表示用ip地址显示不要用逻辑名称显示
                a表示显示所有
                t表示显示tcp链接
                p表示进程号pid




[root@localhost fd]# netstat -natp
Active Internet connections (servers and established)
Proto Recv-Q Send-Q Local Address           Foreign Address         State       PID/Program name    
tcp        0      0 192.168.122.1:53        0.0.0.0:*               LISTEN      1627/dnsmasq        
tcp        0      0 0.0.0.0:22              0.0.0.0:*               LISTEN      1095/sshd           
tcp        0      0 127.0.0.1:631           0.0.0.0:*               LISTEN      1093/cupsd          
tcp        0      0 127.0.0.1:25            0.0.0.0:*               LISTEN      1507/master         
tcp        0     48 192.168.80.100:22       192.168.80.10:59123     ESTABLISHED 115596/sshd: root@p 
tcp        0      0 192.168.80.100:22       192.168.80.10:59053     ESTABLISHED 19204/sshd: root@no 
tcp        0      0 192.168.80.100:22       192.168.80.10:59125     ESTABLISHED 115641/sshd: root@n 
tcp6       0      0 :::29418                :::*                    LISTEN      29939/GerritCodeRev 
tcp6       0      0 192.168.80.100:8080     :::*                    LISTEN      29939/GerritCodeRev 
tcp6       0      0 :::22                   :::*                    LISTEN      1095/sshd           
tcp6       0      0 ::1:631                 :::*                    LISTEN      1093/cupsd          
tcp6       0      0 ::1:25                  :::*                    LISTEN      1507/master         


其中0.0.0.0:22              0.0.0.0:*               LISTEN      1095/sshd  这个是sshd服务的父进程，有一个客户端连接时就会启动另一个进程来服务
比如以下这三个进程
tcp        0     48 192.168.80.100:22       192.168.80.10:59123     ESTABLISHED 115596/sshd: root@p 
tcp        0      0 192.168.80.100:22       192.168.80.10:59053     ESTABLISHED 19204/sshd: root@no 
tcp        0      0 192.168.80.100:22       192.168.80.10:59125     ESTABLISHED 115641/sshd: root@n 


socket就是一个ip:sort ---- ip:sort
传输控制层只会创建用于连接的包，但是还是没有能力把这个包发出去，它还是需要依赖下层网络层提供的能力才行

###网络层
cat /etc/sysconfig/network-scripts/ifcfg-eno16777736  表示显示网卡信息 其中ifcfg-eth0中的if表示interface，cfg为config，eth表示以太网 0表示第一块网卡
一般说到网络配置有4个维度
IPADDR=192.168.80.100  IP地址 点分字节
NETMASK=255.255.255.0   网络掩码 ip地址与掩码按位做与运算，会得到网络号为192.168.80.0，那个ip地址就表示的是这个网络号中的第100号，也就是主机位，所以ip地址是由网络号和主机号组成
GATEWAY=192.168.80.1    网关
DNS1=223.5.5.5   域名解析地址

路由表（世界上任何互联网设备都有路由表，这里演示的是操作系统的路由表，它会做路由判定，判定的过程就是做按位与操作）
route -n
[root@localhost fd]# route -n
Kernel IP routing table
Destination     Gateway         Genmask         Flags Metric Ref    Use Iface
0.0.0.0         192.168.80.1    0.0.0.0         UG    100    0        0 eno16777736
192.168.80.0    0.0.0.0         255.255.255.0   U     100    0        0 eno16777736
192.168.122.0   0.0.0.0         255.255.255.0   U     0      0        0 virbr0
[root@localhost fd]# ping www.baidu.com
PING www.a.shifen.com (61.135.169.121) 56(84) bytes of data.
64 bytes from 61.135.169.121: icmp_seq=1 ttl=128 time=36.8 ms
64 bytes from 61.135.169.121: icmp_seq=2 ttl=128 time=37.3 ms
64 bytes from 61.135.169.121: icmp_seq=3 ttl=128 time=35.6 ms
^C64 bytes from 61.135.169.121: icmp_seq=4 ttl=128 time=36.6 ms

--- www.a.shifen.com ping statistics ---
4 packets transmitted, 4 received, 0% packet loss, time 11170ms
rtt min/avg/max/mdev = 35.696/36.649/37.335/0.629 ms

解释：
如何通过ping www.baidu.com发送数据包到百度的呢？步骤如下
1.通过解析得到www.baidu.com的ip地址为61.135.169.121
2.那上边得到的ip地址与路由表中的每条记录中的Genmask列中的值进行按位与计算得到的ip地址与Destination进行比较，如果不相等则跳过，如果相等就找到了下一跳路由的地址
  就拿上边的例子可以看出路由表中的第一条是满足条件的，所以就得到了192.168.80.1的网关地址，数据包就会发送到这个网关地址中。
3.同一局域网内的机器通信时不需要下一跳的网关的，所以网关Gateway那一列就是0.0.0.0
4.当找到了下一跳后，我们怎么把目标地址61.135.169.121扔给下一跳的地址192.168.80.1呢，这时候就是由下一层链路层来处理了


###链路层

在链路层给ip地址外又套了一层地址，这个地址是网卡地址，即MAC地址。这个是链路层层的通信地址。每层都有表，链路层也有表arp -a
arp是一个协议，dns会解析ip地址和域名的映射（dns是全网的逻辑域名和ip地址）
arp会解释ip地址和网卡硬件地址的映射（arp是同一局域网内的，受限于统一局域网，这个局域网有哪些ip，这些ip地址在网卡中的mac地址是什么）

注意：
通过路由表可以找到下一跳的ip地址，但是数据包中只能有一个目标ip地址（这个ip地址就是端点最终给谁的），所以要在外边套一层MAC地址，而且
MAC地址在每一次跳时都会换，目标ip地址是一直不变的

结论：
TCP/IP协议是基于下一跳的机制
IP是端点间
MAC地址是节点间的

备注：网络工程师会规划下一跳的地址，还有路由协议等机制

设置虚拟地址
ifconfig eno16777736:3 192.168.88.88/24
取消是
ifconfig eno16777736:3 down
设置路由表信息
route add -host 192.168.88.88 gw 192.168.80.100

路由器上有两个地址 是内网地址和外网地址
内网的包在发出去时是需要转换成公网的地址（也包括端口号），内部网络的地址时不能出现的公网的

