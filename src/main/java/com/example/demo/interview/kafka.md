
# Kafka的设计指标
1.吞吐量/延迟
kafka如何做到高吞吐和低延迟
a.kafka的写入操作很快，这得益于它对磁盘的使用方法
 每次写入都是写入到操作系统的页缓存（page cache）,然后由操作系统决定什么时候把页缓存中的数据写回磁盘上。kafka写入操作采用的都是追加写入的方式，避免
 磁盘随机写操作，顺序写入磁盘的速度可能都会比内存随机IO要快。
  所以发送的吞吐量很高，可以做到每秒写入几万甚至几十万
b.在看看消费端如何做到高吞吐和低延迟
  之前提到kafka是把消息写入操作系统的页缓存中，那么同样的，在kafka读取消息时会首先尝试从OS的页缓存中读取，如果命中便把消息经页缓存直接发送到网络的
  Socket上。这个过程就是利用Linux平台的sendFile系统调用做到的，而这种技术就是大名鼎鼎的零拷贝(Zero Copy)技术。而kafka的消息消费机制使用的就是sendfile,
  严格来说是通过Java的FileChannel.transferTO方法实现的。
```text
零拷贝：https://blog.csdn.net/weixin_42096901/article/details/103017044
```
   总结一下，Kafak就是依靠下列4点达到了高吞吐量、低延时的设计目标
* 大量使用操作系统的页缓存，内存操作速度快且命中率高
* Kafka不直接参与物理I/O操作，而是交由最擅长此事的操作系统来完成
* 采用追加写入方式，摈弃了缓慢的磁盘随机读/写操作
* 使用以sendfile为代表的零拷贝技术加强网络间的数据传输效率

2.消息持久化
持久化的好处：
* 解耦消息发送于消息消费
* 实现灵活的消息处理  （消息重演基于此）

有数据都会立即写入文件系统的持久化日志中，之后kafka服务器才会返回结果给客户端通知它们消息已被成功写入。实时保存了数据

3.负载均衡和故障转移
负载均衡：kafka是通过智能化的分区领导者选举来实现的
kafka服务器支持故障转移的方式就是通过会话机制。

4.伸缩性
有了消息的持久化，Kafka实现了高可靠性，
有了负载均衡和使用文件系统的独特设计，Kafka实现了高吞吐
有了故障转移，Kafka实现了高可用性

kafka的状态都交由zookeeper来保存，kafka扩容的时候只需要增加相应的机器即可






