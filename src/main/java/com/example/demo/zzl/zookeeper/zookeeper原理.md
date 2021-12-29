zookeeper分布式协调框架  具备扩展 可靠性 时序性、快速！！！！

###扩展性
####框架 架构
再zookeeper中除了leader、follower还有一个角色就是observer
####读写分离
只有follower才能选举，
Observer其实是一个比follower还要low的一个级别，如果说一个集群有30台机器，当
leader挂了，只有follower角色的机器才能参与投票，observer等着，不参与投票。一旦选出了
leader之后，observer只是追随leader同步数据，接收用户的查询，如果客户给它发送写请求时，它只是转给leader
所以一个集群中参与投票选举的速度是这个集群中follower的数量来决定。
当有30台机器的时候留25台做集群，其余的都是做observer，放大查询能力

如何配置observer
加入把node04当作observer，则可以这样
在之前的zoo.cfg配置文件中最后面把
server.4=node04:2888:3888改为
server.4=node04:2888:3888:observer只需要添加一个observer即可

###可靠性
攘其外必先安其内
zookeeper的可靠性来它内部可以快速恢复
如何实现快速恢复？


数据可靠可用一致性（最终一致性，过程中节点是否对外提供服务），如何实现？



### paxos协议
百度定向搜索
paxos site:douban.com
https://www.douban.com/note/208430424/
先说Paxos，它是一个基于消息传递的一致性算法.
过半通过，两阶段提交


### zab协议
zab是paxos的精简版
zab:zookeeper atom broadcast zookeeper原子广播协议
原子：成功、失败、没有中间结果（基于队列+2pc）
     在zookeeper集群中leader与follower之间的操作是原子的，也就是要么成功，要么失败
广播：分布式多节点，不代表全部知道（过半通过）

队列：先进先出、顺序性

zk的数据状态在内存
用磁盘保存日志

zab作用在可用状态（有leader）


过程：
(有3各节点组成的集群，1个leader、2个follower)
1.有一个客户端对follower发起一个写的请求create /ooxx
2.follower讲这个写请求转发给leader
3.leader是主的单点的所以很容易实现递增，假如创建了一个zxid位8（事务id为8）
4.leader会对两个follower发起写日志（有操作要写数据这件事）
  * （两阶段提交的第一阶段）
      写日志是向磁盘写。写的业务数数据是在内存
      其实在这一步中leader中有维护一个发送队列，对每一个follower都有一个发送队列
       在队列里面压进去的就是写日志请求，写完后会回送一个结果。
      此时如果两个follower中的一个返回了成功，加上leader的成功就是过半的请求，代表
      这个客户端的写请求通过了半数。
   * （两阶段提交的第二阶段）
   写日志通过半数后，leader就会发送写数据的请求（当然也是写入到队列中的），
   给follower进入第二阶段，将数据写入内存。
   最终把队列中的消息消费完就是最终一致性的数据（最终一致性体现在这）
5.写成功后就给客户端返回ok

###leader挂掉怎么恢复？
什么节点可以当leader？
1.数据最全的也就是zxid
2.论资排辈（myid）
注意：过半通过的数据才是真数据，你见到的可用的zxid

有4台机器，myid和zxid分别都是
M1 Z0
M2 Z0
M3 Z0
M4 Z0

* 场景一 第一次启动集群
 先启动M1 M2 M4
 如果是按这个顺序启动，他们的数据一样，zxid一样，
 在启动M4时节点数量过半，那么开始选举leader。
 论资排辈myid最大的就是M4,所以leader就是M4

* 场景二 重启集群
背景：4台机器的情况如下
M1 Z8
M2 Z8
M3 Z7
M4 Z8
只有M3机器的zxid没有同步，数据已经过半，此时leader挂了（M4）。
假如此时M3先发现M4挂了,则向M1和M2发其投票（首先M3给自己投了一票），M1和M2接收zxid是7小于
当前他们记录的zxid为8，则会否决这个票(与此同时它们给自己投票并发起投票)，
并发起一个正确的票（zxid是8）给M3，还有另一个节点。
最终所有人都会投node02为3张票，所以node02开启2888端口，其他人连接2888端口进行通信

zk选举过程：
1.3888造成两两通信
2.只要任何人投票，都会触发那个准leader发起自己的投票
3.推选制：先比较zxid，如果zxid相同，再比较myid

演示：
启动4台将node04停调
zkServer.sh stop
之后会选择node03为leader


### watch
监控、观察
统一视图
目录树结构

应用场景：
两个客户端通过zookeeper，其中一个监控另一个。
在此之前我们可能需要再另一个客户端启动一个线程专门和对方进行心跳检测。
这两种方式的区别在于方向性和失效性a

回调时基于注册的

### api开发
不怕写zk client
集群式哪个版本客户端就必须用哪个版本
具体代码示例见zookeeper-demo项目


### callback
reactive响应式编程
更充分压窄OS HW资源、性能



###自己补充
####ZAB协议
