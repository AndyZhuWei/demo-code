# kafka 
what why how
思路
框架角度
横向 对比
纵向的知识点

## 什么是kafka
是一个消息中间件

亲缘性
牵扯到并发一定要注意一致性的问题 

粗略图


## kafka与磁盘和网卡的技术点

搭建、演示
面向kafka这种MQ，消息的处理方式
http://kafka.apache.org/

环境信息：
机器：node01~04
kafka:node01~03
zookeeper:node02~node04

1.首先安装zk,启动，记录一下zk的根目录信息
2.下载kafka安装包到本地
版本为kafka_2.12-2.1.0.taz
3.解压到目录/opt/kafka20
tar -xzf kafka_2.12-2.1.0.tgz
mv kafka_2.12-2.1.0 kafka20
4.配置环境变量
```
export KAFKA_HOME=/opt/kafka20
export PATH=$JDK_HOME/bin:$PATH:$ZOOKEEPER_HOME/bin:$KAFKA_HOME/bin
```
source /etc/profile
5.对kafka配置文件进行修改 
vi /opt/kafka20/config/server.properties
```
broker.id=0  每个节点不一样
listeners=PLAINTEXT://node01:9092 每个节点不一样
log.dirs=/var/kafka-logs   修改到/var目录下，/tmp目录满了会自动删除
zookeeper.connect=node02:2181,node03:2181,node04:2181/kafka  添加zk集群的节点信息
```
6.节点同步
scp 个性化

7.启动
kafka-server-start.sh /opt/kafka20/config/server.properties
有时候启动会启动 在启动一次就好了？

8.创建topic
kafka-topics.sh --zookeeper node02:2181,node03:2181/kafka --create --topic
ooxx --partitions 2 --replication-factor 2
其实就是通过zookeeper找到controller，然后由controller来创建topic
9.查看topic
kafka-topics.sh --zookeeper node02:2181,node03:2181/kafka --list
10.查看topic的描述信息
kafka-topics.sh --zookeeper node02:2181,node03:2181/kafka --describe --topic ooxx
11.演示发布者和消费者
启动消费者：
kafka-console-consumer.sh --bootstrap-server node01:9092,node02:9092 --topic ooxx --group msb
启动生产者：
kafka-console-producer.sh --broker-list node03:9092 --topic ooxx 
发送消息进行演示
然后再启动一个msb组中的消费者，然后由一个生产者发送消息1，2，3，4，5，6，7
可以看到同一个组的两个消费者分别拿到奇数和偶数的消息。
如果再向msb的组中加入一个消费者，这个消费者是消费不到消息的，因为只有2个分区
12.查询有哪些组
kafka-consumer-groups.sh --bootstrap-server node02:9092 --list
13.查询组的描述信息
kafka-consumer-groups.sh --bootstrap-server node02:9092 --describe --group msb
14.再zk上查看topics
ls /kafka/brokers/topics
[ooxx, __consumer_offsets]
其中__consumer_offsets就是持久化offset


## 消费端如何处理批次
消费端一般都是拉取操作，拉取的颗粒度则是一批，那么消费端要怎么处理（单线程or多线程）这批数据，offset如何维护与持久化？
kafka consumer以什么粒度更新和持久化offset？

1.单线程，一条一条处理的时候，按顺序处理的时候，来更新offset
速度比较慢，硬件资源浪费
1-1. 多线程，offset维护？按条，还是按批？

2.多线程，流式计算，充分利用线程
该并行的地方并行，该统一的地方统一起来
     解决方案是啥？使用多线程
     1.间接的聊大数据，比如spark
     2.流失计算编程上去
批批次的头或尾的绝对更新，依赖了事务的反馈
不会有重复消费，丢失数据的问题

trade off
架构师该做的事嘛
思路决定出路

什么情况下多线程的优势发挥到极致
具备隔离性



总结：
1.单线程，按顺序，单条处理，offset就是递增的，无论对db，offset频率，成本有点高，CPU、网卡，资源浪费
  进度控制，精度比较好
2.流式的多线程，能多线程的多线程，但是，将整个批次的事务环节交给一个线程，做到这个批次，要么成功，要么失败，
  减少对DB的压力，和offset频率压力，更多的去利用CPU,网卡硬件资源
  粒度比较粗

代码示例参考kafka-demo中的zzl包下


默认的分区器是hash分区器

再只有一个消费端，多个分区时，消费端是如何拉取分区的数据？是每次拿不同的分区数据还是怎么样？
其实每次是拿取多个分区的数据

kafka中的offset是按什么取更新维护的？
是按照分区来进行维护的



自己补充：对应示例参考kafka-demo
1.对于消息在分区中的位置，我们将offset称为“偏移量”
2.对于在消费者消费到的位置，将offset称为“位移”或“消费位移”
3.整个生产者客户端由两个线程协调运行，这两个线程分别为主线程和Sender线程（发送线程）。在主线程中由KafkaProducer创建消息，如何通过可能的拦截器、
序列化器和分区器的作用之后缓存到消息累加器(RecordAccumulator,也称为消息收集器)中，Sender线程负责从RecordAccumulator中获取消息并将其发送
到kafka中
4.RecordAccumulator缓存消息，以便sender可以批量发送消息，减少网络传输的资源损耗 缓存大小可以通过buffer.memory参数配置 默认33554432B 即32MB
5.生产速度大于发送速度导致生产空间不足时，send()方法会被阻塞或抛异常，取决参数max.block.ms大小，默认60000 即60秒
6.RecordAccumulator内部为每个分区都维护了一个双端队列，队列中的内容则是ProducerBatch，消息写入是追加到尾部，sender线程发送消息时从头部获取数据，
获取的producerBatch是一批消息，如果要向多个分区发送数据，则可以适当调整buffer.memory大小，以增加吞吐量
7.RecordAccumulator内部还有一个BufferPool，实现ByteBuffer的复用，只针对特定大小，默认为16384B,即16KB 这个是由batch.size参数决定

8.Sender线程从RecordAccumulator拿到数据后会经历一系列转化
<分区,Deque<ProducerBatch>> ---->  <Node,List<ProducerBatch>> ---->  <Node,Request>
之后到达inFlightRequests中，这个里面保存对象形式为Map<NodeId,Deque<Request>>主要作用就是缓存了已经发出去但还没有收到响应的请求
max.in.flight.requests.per.connection 默认为5 配置每个连接最多可以缓存的请求数
9.通过InFlightRequest还可以获得leastLoadedNode，即所有Node中负载最小的那一个
10元数据更新请求
metadata.max.age.ms 默认值300000 即5分钟
元数据虽然由Sender线程负责更新，但是主线程也需要读取这些信息，这里的数据同步通过synchronized和final关键字来保障。

11.重要的生产者参数
acks：这个参数用来指定分区中必须要有多少个副本收到这条消息，之后生产者才会认为这条消息是成功写入的。它涉及消息的可靠性和吞吐量之间的权衡
min.insync.replicas配合acks
max.request.size ：限制生产者客户端能发送的消息的最大值，默认为1048567B即1MB
broker端message.max.bytes参数
retries和retry.backoff.ms  :
compression.type:默认为none
connection.max.idle.ms:

12.重要的消费者参数
fetch.min.bytes: 拉取请求最小数据量，默认1B
fetch.max.bytes: 默认52428800B 即50MB
fetch.max.wait.ms
max.partition.fetch.bytes
max.poll.records
connections.max.idle.ms
exclude.internal.topics
receive.buffer.bytes
send.buffer.bytes
































