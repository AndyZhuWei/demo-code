#lvs三种模式
##NET模式 
非对称，来的数据包可能很大，会称为瓶颈。改变了三层的ip地址
##DR模式（直接路由模型）。
MAC欺骗+要隐藏VIP，改变了二层的mac地址，所以就约束了负载均衡器和服务在同一局域网（MAC地址是节点间的），不能是跨网络
如果是跨网络，则在网络结点中换MAC地址时看到ip地址时vip则会跳回来
基于2层的成本低，速度快


LVS负载均衡器会偷窥握手包，本地会记录客户端和服务的链接信息
搭建LVS不需要装相应的模块，这个模块一般操作系统都自带，但是我们不能直接操作这个模块，需要一个用户空间的程序来操作
这个程序就是ipvsadm

配置：
入包规则：
出包规则：

实验手册
node01作为LVS（DR模式）
node02和node03分别开启一个http的服务

1.首先配置node01上的vip
ifconfig eth0:1 192.168.80.150/24
2.然后配置node02和node03上的vip
有一个顺序需要注意：需要先修改其上的arp协议参数，然后在配置，不然有可能把配置的vip传播出去了，达不到隐藏的效果
修改参数arp_ignore：echo 1 > /proc/sys/net/ipv4/conf/eth0/arp_ignore
修改参数arp_announce：echo 2 > /proc/sys/net/ipv4/conf/eth0/arp_announce
以上只改了eth0,为了确保其他接口也修改其参数，我们在取修改all下面的相应参数值
echo 1 > /proc/sys/net/ipv4/conf/all/arp_ignore
echo 2 > /proc/sys/net/ipv4/conf/all/arp_announce
两个接口eth0和all的协议改完了，在添加vip
但是在这要特别特别注意，添加此vip时子网掩码要设置成255.255.255.255.
因为这样可以规避死循环，可以让与其做与运算的ip地址找到eth0出去，不然会一直发送给lo接口
ifconfig lo:1 192.168.80.150 netmask 255.255.255.255
3.搭建node02和node03上的http服务
yum install httpd -y
httpd是apache的一个静态的web服务器
安装完成后启动并制作主页
service httpd start
在目录/var/www/html下创建一个主页index.html

4.在node01上安装操作ipvsadm
yum install ipvsadm -y
5.添加进来包的规则
ipvsadm -A -t 192.168.80.150:80 -s rr 添加
ipvsadm -ln 查看
6.添加出包规则：
添加两个服务器处理入包的ip地址请求
ipvsadm -a -t 192.168.80.150:80 -r 192.168.80.202 -g -w 1
ipvsadm -a -t 192.168.80.150:80 -r 192.168.80.204 -g -w 1
7.验证
然后再浏览器中访问192.168.80.150就会出现负载的效果
我们再node01上看看是否有握手的包的建立
netstat -natp
发现再node01上是没有握手包的建立，但是node02和node03上就会观察到有很多握手包的建立（需要快点看，一会就没有了）

综上所述：
1.就可以知道LVS是4层的，没有建立3次握手，只是快速的数据包的转发
2.LVS本地记录了转发的记录 ipvsadm -lnc
IPVS connection entries
pro expire state       source             virtual            destination
TCP 14:58  ESTABLISHED 192.168.80.10:62994 192.168.80.150:80  192.168.80.202:80
TCP 01:55  FIN_WAIT    192.168.80.10:62986 192.168.80.150:80  192.168.80.202:80
TCP 01:56  FIN_WAIT    192.168.80.10:62990 192.168.80.150:80  192.168.80.202:80
TCP 01:56  FIN_WAIT    192.168.80.10:62992 192.168.80.150:80  192.168.80.202:80
TCP 01:57  FIN_WAIT    192.168.80.10:62993 192.168.80.150:80  192.168.80.204:80
TCP 01:55  FIN_WAIT    192.168.80.10:62982 192.168.80.150:80  192.168.80.202:80
TCP 01:56  FIN_WAIT    192.168.80.10:62991 192.168.80.150:80  192.168.80.204:80
TCP 01:56  FIN_WAIT    192.168.80.10:62988 192.168.80.150:80  192.168.80.202:80
TCP 01:55  FIN_WAIT    192.168.80.10:62985 192.168.80.150:80  192.168.80.204:80
TCP 01:55  FIN_WAIT    192.168.80.10:62984 192.168.80.150:80  192.168.80.202:80
TCP 01:55  FIN_WAIT    192.168.80.10:62987 192.168.80.150:80  192.168.80.204:80
TCP 01:54  FIN_WAIT    192.168.80.10:62980 192.168.80.150:80  192.168.80.202:80
TCP 01:55  FIN_WAIT    192.168.80.10:62983 192.168.80.150:80  192.168.80.204:80
TCP 01:54  FIN_WAIT    192.168.80.10:62981 192.168.80.150:80  192.168.80.204:80
TCP 01:56  FIN_WAIT    192.168.80.10:62989 192.168.80.150:80  192.168.80.204:80
这些表就是它偷窥记录的数据
FIN_WAIT：表示已经连接过，偷窥
SYN_RECV: 如果有这样基本上就可以证明LVS没事，都记录了，一定是后边网络层出问题了

以上LVS还有问题就是
a.单点问题，LVS是单点的，所有流量都是从这里过来的，如果LVS挂掉则会影响业务。
b.而且以上操作都不是持久化的，重启后就没有了。
c.LVS后端的业务服务器如果挂掉一台，对于LVS是不敢知的，LVS还会把请求转发到这个断掉的服务器上

基于上边的问题 我们引出了keepalived软件
问题：
1.lvs会挂，业务下线，单点故障
2.RS会挂，一部分业务会请求异常，lvs还存有这个RS的负载记录
解决问题：
单点故障的解决方式：它是一个，一个有问题，那么我们就用一堆：一变多
2个思路：多点：形式：a 主备 b主主（可能会引入动态DNS再LVS前边再加一层）
结论：先讨论主备
方向性：主发送广播包给备，证明还活着
效率性：推让制
皇帝--->皇子们
集群模式
主备
主（单点->主备）从
如果确认RS挂了
回答：ping不对
访问一下--》底层：验证的是应用层的http协议-》发请求，判断返回200 ok
---------------------
lvs：内核中有模块：ipvs  增加代码？ 第三方实现？
第三方：人  响应慢 最不靠谱
企业追求自动化 把人解耦出去 用程序替代  keepalived!
代替人自动运维，解决单点故障 实现HA
1.监控自己服务
2.Master通告自己还活着，Backup监听Master状态，
Master挂了，一堆Backup推举出一个新的Master
3.配置:vip,添加ipvs,keepalived是有配置文件
4.对后端server做监控检查
keepalved是一个通用的工具，主要作为HA实现
nginx可以作为公司的负载均衡来使用 nginx成为单点故障
也可以用keepalived来解决

实验手册
再之前的实验手册中增加一个节点node04,用其来作为LVS的备机
步骤：
1.清理调上次手动配置的node01的东西，如果node02和node03有重启过，则需要重新配置一下
2.node01清理
  ifconfig eth0:1  down 
3.node04就是一台裸机
4.在node01和node04上安装我们应用层程序，keepalived
yum install keepalived ipvsadm -y
安装keepalived后它是有能力直接修改ipvs内核中的参数，不需要ipvsadm这个命令
5.修改配置文件
  /etc/keepalived/keepalived.conf
  vi keepalived.conf
vrrp_xxx 其中vrrp表示虚拟的路由冗余协议
virtual_server xxx 虚拟服务.此处的配置就相当于配置LVS的入包规则
persistence_timeout 实验环境改为0
其下的real_server 配置相当于出包规则

帮助手册
yum install man
man 5 keepalived.conf
virtual_ipaddress 192.168.80.150/24 dev eth0 label eth0:3
6.同样的操作在node04上在进行配置，变化的就是state的值，一个是MASTER 另一个是BACKUP,备机的权重改为50
scp ./keepalived.conf root@node04:`pwd`
7.启动
service keepalived start
8.验证
ifconfig eth0 down
ifconfig eth0 up
不是所有的主备，在主切回来后都会抢回来主

vi操作
o O r
:.,$-1y

存疑：
自己在上述配置中忘记改了node01上lvs的模式也就是没有把NET改为DR也可以跑，但是在验证时当把node01上的网卡down后，node04虽然切换成功，
但是此时网络并没有通

其实keepalived引入进来后，keepalived也有可能异常退出，也不能保证高可用，后边会引入zookeeper集群后，就会改善这个问题

