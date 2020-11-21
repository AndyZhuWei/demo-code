#集群
之前讲解的都是单机、单实例的
如果拿redis当缓存，则使用rdb
如果拿redis当数据，则使用aof

单机、单实例的问题：
1.单点故障
2.容量限制
3.压力（链接压力）
根据这3个问题，他们都有独立的解决方案，如果都想解决，还有整合解决方案

AKF（微服务拆分的4个原则之一）:它描述的是xyz轴对你的技术的拆解划分
基于X轴式全量镜像
基于Y轴业务功能
基于Z轴按照优先级逻辑再拆分


如果单机的redis会挂（单点故障），我们可以沿着X轴做redis的副本，它只解决了单点的故障
如果式容量限制，我们可以沿着Y轴对数据按功能业务划分不通的进程实例存储，它解决了容量的问题

通过AKF会一变多，一变多会引入其他问题：
1.数据一致性问题
解决：
    a.所有节点阻塞直到所有数据一致(强一致性 同步方式)，强一致性极易破坏可用性，比如我再给3个节点中同步数据时，其中一个特别慢，最后通信超时还失败，最终返回给客户端写失败，这个就是可用性不高。
这时候，我们就想为什么要把单个redis变为多个，不就是为了可用性，解决单点故障，如果追求强一致性，则会破坏可用性。【强一致性】
   b.所以我们需要把强一致性降级，允许丢失一部分数据，用异步方式同步数据。【弱一致性 丢失数据】
   c.用类似kafka的中间件，它时足够可靠的。只要我们把数据写到（一定是同步写）其中，就返回给客户端成功，后续的节点可以最终同步到数据即可，不会丢失数据，即达到【最终一致性】
 
   
如果一个客户端要访问一个黑盒化的集群（采用最终一致性的集群）中取数据时有几种可能：
在最终一致性之前有可能取到不一致的数据，所以一般redis，zookeeper也好，它们都是最终一致性的。但是可以**强调**成强一致性，让其更新成一致性的

名词解释：
主从：3台redis，客户端除了可以访问主，也可以访问从。
主备：3台redis，客户端只能访问主，不会访问剩余的2台备用机器，备用机器就是在主挂掉后接替主的。备机不参与业务

主从或主备都有一个主，所以一般还需要对主做高可用。当主挂掉后，从或备可以自动接替主
达到故障转移，自动故障转移就是代替人来做这件事，怎么自动转移呢？需要一个监控程序，来监控集群，那么监控集群也一定要是一个集群，否则也是单点故障
是不是看似死循环了，其实它们的特征不一样。
有3个监控程序，监控1个主的redis，如果按照3个监控程序的结果为准，则就是强一致性了，导致可用性降低，那么我们就从1个监控程序推导，如果是以1个监控程序的结果
为准，则容易出现网络分区，即脑裂，不同的监控程序容易出现不同的结果。并不是说出现网络分区就不好，看客户端的容忍性了，看怎么要求
为了防止脑裂，则我们要求过半，即达到势力范围的数量要过一半，则对外的状态就是这过一半达成的状态。剩下的对不算数了，

网络分区（脑裂）：对外服务状态不一致性。即一个网络中的3个节点，不通客户端连接到不通的节点时拿到不通的结果
不是说出去网络分区不好，就看你的分区容忍性，所以一般都要求过半。
即n/2 + 1 可以解决在一个n个节点中集群中的脑裂问题。集群一般使用奇数台，原因是：4台与3台可以承受风险的台数都是1，基于成本3台就比较合适。而且4台发生风险的概率还要大于3台


以上讲解的就是CAP原则

Hbase强调的是强一致性，它的数据不会出现在多个节点的



#Redis主从复制
Redis使用默认的异步复制，其特点是低延迟和高性能。（属于弱一致性，可能会丢失数据）
演示步骤：（192.168.80.100）
1.首先在一台服务器上搭建一个有3个节点的伪分布式集群
2.通过redis源码包中util的install_server.sh脚本进行安装
```
./install_server.sh 
Welcome to the redis service installer
This script will help you easily set up a running redis server

Please select the redis port for this instance: [6379] 6380
Please select the redis config file name [/etc/redis/6380.conf] 
Selected default - /etc/redis/6380.conf
Please select the redis log file name [/var/log/redis_6380.log] 
Selected default - /var/log/redis_6380.log
Please select the data directory for this instance [/var/lib/redis/6380] 
Selected default - /var/lib/redis/6380
Please select the redis executable path [/opt/zhuwei/redis5/bin/redis-server] 
Selected config:
Port           : 6380
Config file    : /etc/redis/6380.conf
Log file       : /var/log/redis_6380.log
Data dir       : /var/lib/redis/6380
Executable     : /opt/zhuwei/redis5/bin/redis-server
Cli Executable : /opt/zhuwei/redis5/bin/redis-cli
Is this ok? Then press ENTER to go on or Ctrl-C to abort.
Copied /tmp/6380.conf => /etc/init.d/redis_6380
Installing service...
Successfully added to chkconfig!
Successfully added to runlevels 345!
Starting Redis server...
Installation successful!
```
安装同样的方式在安装一台
安装完成后，把他们的配置文件都拷贝到家目录下的test目录中
这样在家目录下就有三个redis实例的配置文件
```
-rw-r--r--. 1 root root 61875 Nov 19 10:05 6379.conf
-rw-r--r--. 1 root root 61873 Nov 19 10:05 6380.conf
-rw-r--r--. 1 root root 61873 Nov 19 10:05 6381.conf
```
3.对3个实例的配置文件做以下几点的配置修改
a.daemonize no 将这个设置文前台运行
b.#logfile /var/log/redis_6379.log 将日志输出注释掉，让其输出在屏幕上
c.appendonly no #将aof文件关闭，只让其进行rdb方式的持久化
4.到3个实例的持久化目录中删除已有的备案文件
/var/lib/redis/6379
/var/lib/redis/6380
/var/lib/redis/6381
5.运行这三个实例
redis-server ~/test/6379.conf
redis-server ~/test/6380.conf
redis-server ~/test/6381.conf
6.启动过后，我们想让6379成为主，其余两个成为从，可以这样做：
第一种方式：
在需要成为从的机器上执行如下命令
在5.0版本之前使用命令SLAVEOF，之后使用REPLICAOF命令
用客户端登陆到6380
redis-cli -p 6380
执行追随命令
REPLICAOF 127.0.0.1 6379   #注意如果在配置文件中绑定了ip 就只能写ip 不能写localhost
7.执行完以上命令观察6379和6380输出到屏幕的日志信息
6379的日志信息：
```
63727:M 19 Nov 2020 22:30:08.793 * Replica 127.0.0.1:6380 asks for synchronization
63727:M 19 Nov 2020 22:30:08.793 * Partial resynchronization not accepted: Replication ID mismatch (Replica asked for '837aabd2ac146f39d412cf1a8dbf4c3309dd66ac', my replication IDs are 'b7e3444f75206ff8d1bfa2c0ad32b76e68493ddf' and '0000000000000000000000000000000000000000')
63727:M 19 Nov 2020 22:30:08.793 * Starting BGSAVE for SYNC with target: disk
63727:M 19 Nov 2020 22:30:08.795 * Background saving started by pid 64254
64254:C 19 Nov 2020 22:30:08.807 * DB saved on disk
64254:C 19 Nov 2020 22:30:08.808 * RDB: 6 MB of memory used by copy-on-write
63727:M 19 Nov 2020 22:30:08.889 * Background saving terminated with success
63727:M 19 Nov 2020 22:30:08.889 * Synchronization with replica 127.0.0.1:6380 succeeded
```
6379执行了bgsave命令并且将保存的数据同步给了6380


6380的日志信息：
```
63862:S 19 Nov 2020 22:30:08.459 * Before turning into a replica, using my master parameters to synthesize a cached master: I may be able to synchronize with the new master with just a partial transfer.
63862:S 19 Nov 2020 22:30:08.459 * REPLICAOF 127.0.0.1:6379 enabled (user request from 'id=3 addr=127.0.0.1:48798 fd=7 name= age=37 idle=0 flags=N db=0 sub=0 psub=0 multi=-1 qbuf=44 qbuf-free=32724 obl=0 oll=0 omem=0 events=r cmd=replicaof')
63862:S 19 Nov 2020 22:30:08.789 * Connecting to MASTER 127.0.0.1:6379
63862:S 19 Nov 2020 22:30:08.790 * MASTER <-> REPLICA sync started
63862:S 19 Nov 2020 22:30:08.790 * Non blocking connect for SYNC fired the event.
63862:S 19 Nov 2020 22:30:08.792 * Master replied to PING, replication can continue...
63862:S 19 Nov 2020 22:30:08.793 * Trying a partial resynchronization (request 837aabd2ac146f39d412cf1a8dbf4c3309dd66ac:1).
63862:S 19 Nov 2020 22:30:08.802 * Full resync from master: 3ad84b792c5a2a1ba3ef9d6af9e8f539e6b70480:0
63862:S 19 Nov 2020 22:30:08.802 * Discarding previously cached master state.
63862:S 19 Nov 2020 22:30:08.890 * MASTER <-> REPLICA sync: receiving 175 bytes from master
63862:S 19 Nov 2020 22:30:08.890 * MASTER <-> REPLICA sync: Flushing old data
63862:S 19 Nov 2020 22:30:08.890 * MASTER <-> REPLICA sync: Loading DB in memory
63862:S 19 Nov 2020 22:30:08.890 * MASTER <-> REPLICA sync: Finished with success

```
6380首先删除旧的数据，然后再加载输出过来的命令

至此，6379和6380会保持数据同步，并且6380只能读，不能写（可以通过配置达到写的效果），从6379写入的数据会同步到6380上。

再6381上执行与6380同样的操作，也会达到与6379同步的效果


8.演示6381实例down掉，然后再主上写一些数据，最后再启动6381，
当6381down掉的时候再6379日志中会出现以下信息
```
Connection with replica 127.0.0.1:6381 lost.
```
在6379中写入一些新的信息
set k5 123
此时在启动6381用如下命令
redis-server ~/test/6381.conf --replicaof 127.0.0.1 6379
在主上就会出现6381请求同步的日志信息
```
63727:M 19 Nov 2020 22:47:12.538 * Replica 127.0.0.1:6381 asks for synchronization
63727:M 19 Nov 2020 22:47:12.538 * Partial resynchronization request from 127.0.0.1:6381 accepted. Sending 282 bytes of backlog starting from offset 1333.
```
这次同步只是同步了增量的日志信息
登陆到6381上查看确实同步到了k5的信息

在启动命令中如果下--appendonly yes 曾会触发6379执行bgsave，进行全量同步
6381上就会出现aof重写，aof文件中是不存在replic-id信息的，rdb中是有的。

注意一个问题，在主的身上是可以发现谁追随了自己

9，如果主挂掉了，则从就知道了，报连接异常，但是此时的从事不能自动切换成主，我们在其上只能进行查询，而不能写，在没有哨兵的情况下，我们手动可以执行这个命令
来处理：
replicaof no noe 这样从就会切回成主
另一个从可以追随这个新的主，这样就手动把从切换成了主，并且让另一个从追随新的主

10.集群配置文件讲解
以上启动命令中的都是可以在配置文件中进行配置
replica-serve-stale-data yes #这个表示当前从节点在同步完成数据之前是否暴漏出自己有的老数据，yes表示暴漏，no表示不暴露
replica-read-only yes #从机是否只支持读
repl-diskless-sync no #通过磁盘然后再通过网络发送同步数据rdb，如果为yes则直接通过网络发送数据
repl-backlog-size 1mb #增量复制大小，也就是队列大小，超过队列容量，则进行全量复制，否则就是增量复制
min-replicas-to-write 3  #最小写几个算成功，根据业务定，
min-replicas-max-lag 10  #

主从复制配置需要人工维护主的故障问题，然后我们再看主从复制的高可用方案
#Redis主从复制的高可用方案
Redis 的 Sentinel 系统用于管理多个 Redis 服务器（instance）， 该系统执行以下三个任务：

监控（Monitoring）： Sentinel 会不断地检查你的主服务器和从服务器是否运作正常。
提醒（Notification）： 当被监控的某个 Redis 服务器出现问题时， Sentinel 可以通过 API 向管理员或者其他应用程序发送通知。
自动故障迁移（Automatic failover）： 当一个主服务器不能正常工作时， Sentinel 会开始一次自动故障迁移操作， 它会将失效主服务器的其中一个从服务器升级为新的主服务器， 并让失效主服务器的其他从服务器改为复制新的主服务器； 当客户端试图连接失效的主服务器时， 集群也会向客户端返回新主服务器的地址， 使得集群可以使用新主服务器代替失效服务器。

###启动Sentinel
对于 redis-sentinel 程序， 你可以用以下命令来启动 Sentinel 系统：

对于 redis-server 程序， 你可以用以下命令来启动一个运行在 Sentinel 模式下的 Redis 服务器：

redis-server /path/to/sentinel.conf --sentinel
两种方法都可以启动一个 Sentinel 实例。

启动 Sentinel 实例必须指定相应的配置文件， 系统会使用配置文件来保存 Sentinel 的当前状态， 并在 Sentinel 重启时通过载入配置文件来进行状态还原。

如果启动 Sentinel 时没有指定相应的配置文件， 或者指定的配置文件不可写（not writable）， 那么 Sentinel 会拒绝启动。

###配置 Sentinel
sentinel monitor mymaster 127.0.0.1 6379 2   #监控主服务器 ip port 2表示至少有两个监控程序任务认为主失败了，才会发生转移


###演示哨兵
1.在家目录中创建3台哨兵的配置文件
vi 26379.conf        
它的内容如下：
```
port 26379
sentinel monitor mymaster 127.0.0.1 6379 2
```
vi 26380.conf
它的内容如下：
```
port 26380
sentinel monitor mymaster 127.0.0.1 6379 2
```
vi 26381.conf
它的内容如下：
```
port 26381
sentinel monitor mymaster 127.0.0.1 6379 2
```
2.先启动3台redis-server
redis-server ~/test/6379.conf
redis-server ~/test/6380.conf --replicaof 127.0.0.1 6379
redis-server ~/test/6381.conf --replicaof 127.0.0.1 6379

3.再启动3个哨兵
启动哨兵的命令可以单独成立一个命令，也可以包含再redis-server中，我们再我们的安装目录中
/opt/zhuwei/redis5/bin下发现redis-sentinel是一个软连接
```
-rwxr-xr-x. 1 root root 4365336 Oct 21 22:14 redis-benchmark
-rwxr-xr-x. 1 root root 8115378 Oct 21 22:14 redis-check-aof
-rwxr-xr-x. 1 root root 8115378 Oct 21 22:14 redis-check-rdb
-rwxr-xr-x. 1 root root 4805680 Oct 21 22:14 redis-cli
lrwxrwxrwx. 1 root root      12 Oct 21 22:14 redis-sentinel -> redis-server
-rwxr-xr-x. 1 root root 8115378 Oct 21 22:14 redis-server
```
如果我们要用redis-server启动哨兵，则我们要加入一些启动参数，告诉redis,启动的是哨兵，不是接收数据的redis，例如
redis-server ~/test/26379.conf --sentinel  #26379.conf配置哨兵的文件，--sentinel表示要启动一个哨兵
启动日志如下
```
80343:X 20 Nov 2020 21:15:59.618 # Sentinel ID is 37e696196543b86ede7c6d3d6c0573f854e3b4d5
80343:X 20 Nov 2020 21:15:59.618 # +monitor master mymaster 127.0.0.1 6379 quorum 2
80343:X 20 Nov 2020 21:15:59.619 * +slave slave 127.0.0.1:6380 127.0.0.1 6380 @ mymaster 127.0.0.1 6379
80343:X 20 Nov 2020 21:15:59.621 * +slave slave 127.0.0.1:6381 127.0.0.1 6381 @ mymaster 127.0.0.1 6379
```
从输出可以看出哨兵监控了主，而且还知道这个主有两个从节点，那么哨兵怎么知道有这两个从节点呢，我们只是配置了监控的主节点呀？
其实主知道它有哪些从，哨兵监控了主，自然也就知道有哪些从节点，那这个是怎么实现的呢？其实用到了发布订阅的功能

我们再启动一个哨兵
redis-server ~/test/26380.conf --sentinel
日志如下
```sql
80584:X 20 Nov 2020 21:20:19.706 # Sentinel ID is 56f93a2d6bd48bcce2160e5ab70e4f919406085d
80584:X 20 Nov 2020 21:20:19.706 # +monitor master mymaster 127.0.0.1 6379 quorum 2
80584:X 20 Nov 2020 21:20:19.707 * +slave slave 127.0.0.1:6380 127.0.0.1 6380 @ mymaster 127.0.0.1 6379
80584:X 20 Nov 2020 21:20:19.709 * +slave slave 127.0.0.1:6381 127.0.0.1 6381 @ mymaster 127.0.0.1 6379
80584:X 20 Nov 2020 21:20:20.468 * +sentinel sentinel 37e696196543b86ede7c6d3d6c0573f854e3b4d5 127.0.0.1 26379 @ mymaster 127.0.0.1 6379
```
这个哨兵除了知道主和两个从，还知道之前的那个启动的哨兵
同时再26379的哨兵日志也会出现一句这样的输出
80343:X 20 Nov 2020 21:20:21.705 * +sentinel sentinel 56f93a2d6bd48bcce2160e5ab70e4f919406085d 127.0.0.1 26380 @ mymaster 127.0.0.1 6379
说明26379的哨兵也知道了这个刚启动的26380哨兵


我们再启动最后一个哨兵
redis-server ~/test/26381.conf --sentinel
```
80744:X 20 Nov 2020 21:25:15.237 # Sentinel ID is 714dc1762f3f591a866ef124ae98a25729ea3a49
80744:X 20 Nov 2020 21:25:15.237 # +monitor master mymaster 127.0.0.1 6379 quorum 2
80744:X 20 Nov 2020 21:25:15.239 * +slave slave 127.0.0.1:6380 127.0.0.1 6380 @ mymaster 127.0.0.1 6379
80744:X 20 Nov 2020 21:25:15.240 * +slave slave 127.0.0.1:6381 127.0.0.1 6381 @ mymaster 127.0.0.1 6379
80744:X 20 Nov 2020 21:25:15.750 * +sentinel sentinel 56f93a2d6bd48bcce2160e5ab70e4f919406085d 127.0.0.1 26380 @ mymaster 127.0.0.1 6379
80744:X 20 Nov 2020 21:25:16.446 * +sentinel sentinel 37e696196543b86ede7c6d3d6c0573f854e3b4d5 127.0.0.1 26379 @ mymaster 127.0.0.1 6379
```
这个哨兵知道了主、2个从和之前启动了2个哨兵
同样的，之前启动的2个哨兵也知道了现在启动好的这个哨兵了

4.演示把6379的主down掉
现象：
6380和6381开始报以下错误
```
79875:S 20 Nov 2020 22:43:26.309 # Connection with master lost.
79875:S 20 Nov 2020 22:43:26.309 * Caching the disconnected master state.
79875:S 20 Nov 2020 22:43:26.352 * Connecting to MASTER 127.0.0.1:6379
79875:S 20 Nov 2020 22:43:26.352 * MASTER <-> REPLICA sync started
79875:S 20 Nov 2020 22:43:26.352 # Error condition on socket for SYNC: Connection refused
```
过一会3个哨兵就开始投票选举出一个新的主了，让另外两台成为从。
而且哨兵会修改配置文件，我们打开之前的26379.conf
```
port 26379
sentinel myid 37e696196543b86ede7c6d3d6c0573f854e3b4d5
# Generated by CONFIG REWRITE
dir "/root"
protected-mode no
sentinel deny-scripts-reconfig yes
sentinel monitor mymaster 127.0.0.1 6381 2
sentinel config-epoch mymaster 1
sentinel leader-epoch mymaster 1
sentinel known-replica mymaster 127.0.0.1 6379
sentinel known-replica mymaster 127.0.0.1 6380
sentinel known-sentinel mymaster 127.0.0.1 26380 56f93a2d6bd48bcce2160e5ab70e4f919406085d
sentinel known-sentinel mymaster 127.0.0.1 26381 714dc1762f3f591a866ef124ae98a25729ea3a49
sentinel current-epoch 1
```


问题：
哨兵启动后怎么知道其他哨兵的存在？
它其实也用了redis的发布订阅功能，验证
用客户端连接redis-cli -p 6381
输入PSUBSCRIBE *，就会用正则匹配任何通道上的消息，读取到的信息如下：
```
Reading messages... (press Ctrl-C to quit)
1) "psubscribe"
2) "*"
3) (integer) 1
1) "pmessage"
2) "*"
3) "__sentinel__:hello"
4) "127.0.0.1,26379,37e696196543b86ede7c6d3d6c0573f854e3b4d5,1,mymaster,127.0.0.1,6381,1"
1) "pmessage"
2) "*"
3) "__sentinel__:hello"
4) "127.0.0.1,26380,56f93a2d6bd48bcce2160e5ab70e4f919406085d,1,mymaster,127.0.0.1,6381,1"
1) "pmessage"
2) "*"
3) "__sentinel__:hello"
4) "127.0.0.1,26381,714dc1762f3f591a866ef124ae98a25729ea3a49,1,mymaster,127.0.0.1,6381,1"
1) "pmessage"
2) "*"
3) "__sentinel__:hello"
4) "127.0.0.1,26379,37e696196543b86ede7c6d3d6c0573f854e3b4d5,1,mymaster,127.0.0.1,6381,1"
1) "pmessage"
2) "*"
3) "__sentinel__:hello"
4) "127.0.0.1,26380,56f93a2d6bd48bcce2160e5ab70e4f919406085d,1,mymaster,127.0.0.1,6381,1"

```
其中__sentinel__:hello就是哨兵用于发送消息的通道，哨兵就是通过这个知道其他哨兵的存在

以上哨兵的配置文件只是一种简单的写法，它有自己专门的配置文件再源码目录中是sentinel.conf
比较重要的几个配置是
port 26379
sentinel monitor mymaster 127.0.0.1 6381 2


###
