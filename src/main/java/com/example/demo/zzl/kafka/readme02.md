#理论课
kafka中的第四节课程
有以下几个角色
1.zk
2.broker
  这个broker是一个集群，再找个集群中会有一个controller角色的broker，怎么决定谁是controller
其实是通过再zk中创建一个临时节点controller,谁创建成功了谁就是controller
通过再zk客户端查看
```
get /kafka/controller
```
除此之外所有的broker再启动后还会再zk的/kafka/brokers/ids中创建自己的id
找个id节点也是一个临时节点
```
get /kafka/brokers/ids/1  此处的id就是1
```
创建主题的时候是有controller的broker来创建的，创建的topic信息是保存在
zk中的/kafka/brokers/topics
topics目录下就是该主题的分区信息
分区下面就是一个状态节点state，该节点就不是临时节点，需要持久化。
当controller节点的broker下线后，其他broker通过zk也是可以拿到这些元数据信息

分区还有一个副本的概念，主副本就是提供增删改查，辅副本主要提供可靠性的。
kafka没有读写分离的


##单机
MQ 消息队列
异步
削峰填谷
解耦

可靠性有不同的要求
acks：
0 ：producer消息发出即完成消息发出的语义
1 默认 服务器端一定收到了
-1 面向分布式


trade off(权衡)
写磁盘的粒度越小 速度越慢

应该把单机的持久化可靠性转向集群多机方式
磁盘随机读写远比顺序读写慢

** 底层文件
生产者把数据写入kafka后，会先写到内核的pageCache,
pageCache在写到磁盘（可以根据场景控制写入时机），
在磁盘中除了数据文件还有索引文件
找个索引文件有基于offset 还有基于时间戳的，时间戳先转化成offset，如何offset在
找到对应的数据文件记录给消费者
不然那么多不同消费组的消费者消费不同进度的消息时怎么快速找到起点位置呢
常识：
```text
数组大小固定 空间上时连续 计算方式找到地址
链表 不固定 空间上不连续 遍历复杂度 建立索引

```

** 消费端拉取数据
当有一个消费端消费数据时，磁盘中的数据可能还没有被内核刷到磁盘中。
消费端把需要拉取的数据告诉broker，broker经过计算知道要从哪个offset开始读取数据，
kafka对消息只是发送，不需要加工，此时在系统内核中通过sendFile零拷贝直接给消费端会比较快。
broker调用sendfile(in,offset,out)系统调用直接给消费端返回数据，不需要再经过broker进程
in对接的就是pageChage,out对接的就是消费端。
零拷贝减少了到程序的复制过程。

如果没有零拷贝就会从pageCache拷贝到进程空间，然后进程空间再拷贝到系统内核空间中

##分区可靠性
要解决一个问题，伴随着引入更多的问题


再分布式中，如果多机的情况下，数据到达一个节点，其他节点也要保证同步到的话有，也就是一致性
一致性有以下词汇
1.强一致性：所有节点必须全部存活,一致性会破坏可用性
2.1,最终一致性，过半通过，最常用的分布式一致性解决方案
2.2,
ISR(in-sync replicas)在同步中的节点数连通性&活跃性
OSR(outof-sync replicas),超过阈值时间（10秒）没有”心跳“
AR(assisgned replicas) 面向分区的副本集合,创建topic的时候你给出了分区的副本数，那么
 controller再创建的时候就已经分配了broker和分区的对应关系，并得到了该分区的broker集合
AR=ISR+OSR
你要学明白的是ack机制（尤其是-1机制）


###ack
1默认值
-1 最严苛 所有的副本都要同步，一致

ack为-1的时候 多个broker（ISR）的消息进度是一致的(这个是ack为-1的语义)
consumer消费的就是producer生产的

ack为1的时候，producer发送的数据leader的broker一定会有了，找个是ack为1的语义
多个broker的消息进度是不一致的，但是没有超过10秒，他们都是ISR集合中
所以站在consumer的角度看他们的消息进度不一致

logEndOffset(LEO)
High Watermak(HW):集群对外暴露的，消费端可以消费最高的offset

生产是生产 消费是消费，已经被MQ解耦了


KAFKA存储层（全量历史） 弹性
队列，反复的查阅使用，推送
趋向于稳定
要么用redis
要么用kafka
支持时间戳

裁剪历史数据
Low Watermark(LW)



总结：
0.另外一个trade off:
不要强调磁盘的可靠性，转向异地多机的同步
1.如果拿磁盘做持久化，在做trade off
优先pagecache或者绝对磁盘
2.在多机集群分布式的时候
强一致性，最终一致性（过半，ISR）

*: redis,宁可用HA,不用刻意追求AOF准确性 向kafka，我们追求ack -1
不要太追求磁盘的可靠性
还有trade off，就是在HA场景下，如果有实例异常退出，是否需要立刻尝试重启

## 实践
主要讲解的知识点
1.索引
时间戳索引和偏移量索引
2.消费者，数据可靠性
ASR ISR AR
3.时间戳，自定义消费数据
本质是：seek




环境
在node01~node03上跑kafka
在node02~node04上跑zk
kafka持久化的数据目录是在配置文件中的log.dirs配置的
创建一个主题msb-items

### 索引

```text
kafka-topics.sh --zookeeper node02:2181,node03:2181/kafka --create --topic msb-items --partitions 2 --replication-factor 3
kafka-topics.sh --zookeeper node02:2181,node03:2181/kafka --describe --topic msb-items #复制出来观察```
```
观察分区在哪个节点，观察持久话目录里的数据，可以看到有数据和索引
数据大小为0，但是索引文件都是10M，这些都是mmap映射出来的，
通过lsof查看时对应的type为mem，而log文件则是普通的文件reg

mmap(内核空间和用户空间的映射)

file文件的channel.mmap
文件的块和内核的块映射和应用程序空间打通

lsof -Pnp pid   #-P:端口号 -n显示端口号不要显示端口号别名 -p：进程

为什么log不用mmap?
通用知识点：
mmap 或者普通io
log使用普通io的形式目的时通用性，数据存入磁盘的可靠性级别、
app层级调用了io的write，但是这个时候指到达了内核，性能快，但丢数据
只有NIO的filechannel,你调用了write()+force()才真的写道磁盘，性能极低的
1，每条都force
2.只是write基于内核刷写机制，靠脏页


java中传统的io oio
io.flush 是个空实现，没有物理刷盘，还是依赖内核的dirty刷盘，所以会丢东西


kafka-dump-log.sh --files xxx.log
kafka-dump-log.sh --files xxx.index
现在查看这些日志文件还索引文件都还是空的内容，我们现在写一些内容，
根据写入的分区到对应的节点再次查看数据会发现log文件的大小有变化了，
当超过10m的索引文件时，索引文件也会变大
（mmap也是利用脏页来刷盘）

索引文件中不会每条记录都会索引记录，它是松散的，比如
offset:54 positions:4158
表示的就是offset是54的，在字节数组（log文件）的4158处可以读到

os提供了seek方法
os提供了sendFile方法零拷贝直接把数据给到消费端

除此之外还有一个时间戳索引，里面记录了时间戳了offset的关系



### 生产者，数据可靠性

在生产者这边还有一个配置项
ProducerConfig.ACKS_CONFIG
演示0，1，-1的效果

ISR是个弹性机制，而不是强硬的过半



现在要做的就是让leader节点于其中的一个follwer节点通信受阻
（可以发出去，但是回不来，阻断一个数据包的双向完整通信回路），
观察效果，我们不能kill，不然操作系统会回收tcp资源
阻塞通信回路可以修改路由表，比如让3到2的路由表正常，让2到3的路由表地址指向其他地方。
这样可以把2节点踢到OSR集合中，ISR由3个节点变为2个节点，程序可以正常运行


实验一：演示acks为0
在node02上操作
```text
route -n
ping node03
```
首先生产者发送消息
在node02上执行route add -host node03的ip gw 127.0.0.1
此时可以发现生产者还是可以正常发送消息，没有任何影响
可以观察一下分区情况
kafka-topics.sh --zookeeper node02:2181,node02:2181/kafka --describe --topic msb-items

结论：生产者发送消息和后端的节点状态没有关系

实验一：演示acks为1
在node02上执行route del -host node03的ip
然后生产者开始跑
添加在node02上执行route add -host node03的ip gw 127.0.0.1
观察生产者不受影响
观察分区情况。ISR少了一个节点2

结论：生产者发送消息没有影响

实验三.演示acks为-1的场景
为-1表示的是leader必须等到所有的follwer节点同步完成在返回确认给生产者，
先恢复环境
在node02上执行route del -host node03的ip
生产者开始跑数据，观察一直在跑
然后添加在node02上执行route add -host node03的ip gw 127.0.0.1
生产者此时会卡住大约10秒后就会又开始正常跑。

结论：生产者发送消息时会卡，卡的过程其实就是剔除node02到OSR集合的过程，剔除后，
ISR集合中1和3接收到数据反馈后就会开始正常的流程了


### 时间戳，自定义消费数据








































