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


































