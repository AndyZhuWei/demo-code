###管道
将一些列的命令一次都发送到redis中
如果没有redis客户端，我们可以使用nc和redis建立一个socket连接，发送命令也是可以执行的
nc localhost 6379
管道的使用方式如下：
echo -e "set k2 99\nincr k2\n get k2" | nc localhost 6379
将echo -e "set k2 99\nincr k2\n get k2" 这些输出通过管道命令放入到nc连接的客户端中发送给redis
```
[root@localhost ~]# echo -e "set k2 99\nincr k2\n get k2" | nc localhost 6379
+OK
:100
$3
100
[root@localhost ~]#
```
通过管道让我们的三条命令一次发送给了redis，让我们通信的成本变低了

###冷启动
需要大量插入数据，或者从文件中批量插入数据
从文件中批量插入数据时可能会用到管道命令
cat dl.txt | redis-cli
使用--pipe这个参数来启用pipe协议，它不仅仅能减少返回结果的输出，还能更快的执行指令
cat dl.txt | redis-cli --pipe
要求这个文件dl.txt的换行符
linux系统和windows系统对换行要求不一样
redis-cli支支持dos格式的换行符\r\n,在linux、max或者windows下创建文件时，最好转码
unix2dos dl.txt
 unix2dos 这个是一个转码工具
 
###redis发布订阅功能 Pub/Sub
help @pubsub

 PSUBSCRIBE pattern [pattern ...]
  summary: Listen for messages published to channels matching the given patterns
  since: 2.0.0

  PUBLISH channel message
  summary: Post a message to a channel
  since: 2.0.0

  PUBSUB subcommand [argument [argument ...]]
  summary: Inspect the state of the Pub/Sub subsystem
  since: 2.8.0

  PUNSUBSCRIBE [pattern [pattern ...]]
  summary: Stop listening for messages posted to channels matching the given patterns
  since: 2.0.0

  SUBSCRIBE channel [channel ...]
  summary: Listen for messages published to the given channels
  since: 2.0.0

  UNSUBSCRIBE [channel [channel ...]]
  summary: Stop listening for messages posted to the given channels
  since: 2.0.0


**基本用法**
如果先执行publish ooxx hello:向ooxx通道放入消息hello，然后另一个客户端执行subscribe ooxx。此时是收不到消息的。
所以只有先订阅，然后再发布的时候才能收到消息

如果有这样的需求，进入聊天室后除了可以看到实时消息外，还希望看到以往的历史消息，这个应该怎么设计？
这个历史消息一般人都会查看，但是时间范围大有不同，一般可能就翻看3周之内，所以全量数据一定是再数据库中。3周之内的数据可以放到redis中

综上所述：
1. 实时数据直接来自发布订阅功能
2. 3周以前的数据可以来自数据库
2. 3周之内的数据来自redis，用redis的什么数据结构呢？sorted_set   用到了其中的这个命令ZREMRANGEBYRANK
   把消息对应的日期作为分值，把消息作为元素。
   
整体过程：
一个发送消息的客户端pub一个消息后，有至少3类的sub客户端，
a.接收实时消息的客户端
b.接收消息的一个service,这个servcie是要向kafka传递数据，最后保存到数据库作为全量数据
c.接收消息的另一个redis客户端，这个客户端把接收到的消息放入到sorted_set的对象中，作为3周之内的数据来提供


###Redis的事务
redis的事务不像mysql事务那么完整，redis核心特征是速度快，如果选用redis使它速度变慢了，那还不如不是redis。
redis的作者也是按照这个核心功能来开发redis的。
redis中没有一个类似回滚的东西

 DISCARD -
  summary: Discard all commands issued after MULTI
  since: 2.0.0

  EXEC -
  summary: Execute all commands issued after MULTI
  since: 1.2.0

#multi
  MULTI -
  summary: Mark the start of a transaction block
  since: 1.2.0

  UNWATCH -
  summary: Forget about all watched keys
  since: 2.2.0

  WATCH key [key ...]
  summary: Watch the given keys to determine execution of the MULTI/EXEC block
  since: 2.2.0



###基本用法
multi：标识开启一个事务，然后发送要redis执行的命令，发送过去后redis并不会立即执行
    当执行exec命令时，redis才会把刚才发送的命令按顺序执行，如果出现错误就会回滚，有这个概念，但不是100%的
    
注意：redis是单进程的，当多个客户端发送multi,以及需要事务执行的命令。当多个客户端谁先发送exec是，就先执行那个客户端标记的一系列命令
这样可能就有这么一种情况，先执行的事务可能会删除后边要执行事务中的一个key，导致后边的事务命令报错，这时我们可以加一个监控，监控key
，意思就是当后边事务命令监控某些key被之前的事务命令执行过，则当前事务就不再执行了。

###通过乐观锁实现CAS
redis可以通过乐观锁实现CAS

例子1：
> multi      #开启事务
OK
> set k1 aaa    #命令不会立即执行会放到队列中
QUEUED
> set k2 bbb
QUEUED
> exec          #开始执行
1) OK
2) OK

例子2：
第一个客户端：
127.0.0.1:6379> multi
OK
127.0.0.1:6379> get k1
QUEUED
127.0.0.1:6379> exec
1) (nil)
127.0.0.1:6379>
第二个客户端：
127.0.0.1:6379> multi
OK
127.0.0.1:6379> del k1
QUEUED
127.0.0.1:6379> exec
1) (integer) 1
127.0.0.1:6379> 

第二个客户端先执行exec，则第一个客户端执行exec后发现get k1是获取不到值的

例子3：（用watch监控）
客户端一：
127.0.0.1:6379> set k1 aaa  #设置k1
OK
127.0.0.1:6379> watch k1     #监控k1
OK
127.0.0.1:6379> multi       #开启事务
OK
127.0.0.1:6379> get k1      #得到k1
QUEUED
127.0.0.1:6379> keys *     #得到所有的key
QUEUED
127.0.0.1:6379> exec      #执行事务，后执行，客户端二先执行
(nil)                #没有得到任何输出，相当于因为客户端二执行了，修改可k1.让watch监控到了，所以客户端一的事务命令就不执行了
127.0.0.1:6379> 

客户端二：
127.0.0.1:6379> multi  #开启事务
OK
127.0.0.1:6379> keys *   #得到所有的key
QUEUED
127.0.0.1:6379> set k1 bbbbb   #设置k1
QUEUED
127.0.0.1:6379> exec   #先于k1执行
1) 1) "k1"
2) OK
127.0.0.1:6379> get k1  #得到了k1的值
"bbbbb"
127.0.0.1:6379> 

###为什么redis不支持回滚
1.redis命令只会因为错误的语法而失败（并且这些错误不能再入队列时发现），或是命令用在了错误的类型上面
   从实用性的角度来说，失败的命令是由编程错误造成的，而这些错误应该再开发的过程中被发现的，而不应该出现在生产环境中。
2.因为不需要对回滚进行支持，所以Redis的内部可以保持简单且快速


###modules 模块
演示按照RedisBloom(布隆过滤器)
这个过滤器
在windows中扩展库是.dll,在linux中是.so

安装及演示：
1.编译安装布隆过滤器的扩展库
https://github.com/RedisBloom/RedisBloom/archive/master.zip
在家目录/root/soft/ 下执行
wget https://github.com/RedisBloom/RedisBloom/archive/master.zip
下载成功后，在当前目录下就会出现master.zip的压缩包
解压：unzip master.zip
解压成功后，就会出现源码目录RedisBloom-master
进入后会发现Makefile文件，这个文件就是编译的配置文件。
执行make编译命令后，就会编译出一个扩展库redisbloom.so,这个扩展库可以拷贝到任意位置，一般拷贝到redis的目录中。
我们执行拷贝 cp redisbloom.so /opt/zhuwei/redis5/
2.redis加载布隆过滤器
首先 service redis_6379 stop
然后 redis-server --loadmodule /opt/zhuwei/redis5/redisbloom.so  
注意事项：redisbloom.so要写绝对路径，而且后边不能跟配置文件路径，不然启动会失败
3.演示
加载这个过滤器后就会有相应的命令
bf.xxx 之类的
这个过滤器可以解决**缓存穿透**的问题
缓存穿透：访问的内容在redis缓存和数据库中都没有的，最后落在数据库白白消耗资源。
我们可以这样解决这个问题，我们把数据库中所有的东西放在缓存中，如果查询到了我们就到数据库然后缓存，如果没有则直接返回到数据库中都不用检查了
但是数据库中那么多的数据怎么放到缓存中呢，这个就是布隆过滤器要解决的问题。

127.0.0.1:6379> BF.ADD ooxx abc
(integer) 1
127.0.0.1:6379> BF.exists ooxx
127.0.0.1:6379> BF.exists ooxx abc
(integer) 1
127.0.0.1:6379> BF.exists ooxx abcfdsafdsaf
(integer) 0
127.0.0.1:6379>
安装完这个模块后，除了布隆过滤器(bloom)，升级版的布隆过滤器(counting bloom)
还有布谷鸟过滤器(Cuckoo)


4.布隆过滤器有以下三种方式
客户端实现布隆算法,自己重载bitmap，redis只做缓存
客户端实现布隆算法，redis+bitmap
客户端不用做任何事情，redis+redisbloom.so+bitmap

###redis作为数据库/缓存的区别
缓存中的数据不“重要”
缓存不是全量数据
缓存应该随着访问变化
热数据

redis作为缓存，redis里面的数据怎么能随着业务数据变化，只保留热数据，因为内存大小有限哦，也就是瓶颈
key的有效期，key的有效期有业务逻辑推动。
随着访问的变化应该淘汰冷数据。

内存多大呢？
vi /etc/redis_6379.conf
maxmemory <bytes>  #控制在1G到10G之间
maxmemory-policy noeviction  #后边跟的是策略

```
 volatile-lru -> Evict using approximated LRU among the keys with an expire set.
# allkeys-lru -> Evict any key using approximated LRU.
# volatile-lfu -> Evict using approximated LFU among the keys with an expire set.
# allkeys-lfu -> Evict any key using approximated LFU.
# volatile-random -> Remove a random key among the ones with an expire set.
# allkeys-random -> Remove a random key, any key.
# volatile-ttl -> Remove the key with the nearest expire time (minor TTL)
# noeviction -> Don't evict anything, just return an error on write operations.
```
LFU 碰了多少次
LRU 多久没碰它

如果是把redis当作数据库的话我们选择noeviction这个策略
如果是把redis当作缓存的话我们选择allkeys-lru或者volatile-lfu

key的有效期
set k1 aaa ex 20  #ex 20 表示20秒后会过期
ttl k1 #查看还有多少秒就过期了 
除了以上方式设置过期外，还可以有专门的命令来设置
expire k1 50
如果对k1发送了写命令，就会剔除过期时间
expireat k1 时间戳   #倒计时，redis不能延长，ttl k1会返回-1，如果对一个不存在的key ttl操作会返回-2

###Redis中如何淘汰过期的keys
有两种：被动和主动
被动的方式是指，redis的一些过期key，如果不访问它，redis是不会删除的，这时会占用空间。
主动的方式是指，间接式轮询判定（增量）。
   测试随机的20个key进行相关过期检测
   删除所有已经过期的keys
   如果有多于25%的key过期，重复步骤


###缓存常见问题：
缓存击穿
缓存穿透
缓存雪崩
缓存一致性（双写）



1.场景：
把redis当作数据库，在redis有一份数据，在mysql数据库中同样也有一份数据，而且redis前置。
如果你想要把数据写入内存中的redis,同时要下入有IO延迟的mysql中，怎么保证他们数据的一致性（双写一致性），一般都是通过异步来处理，中间加一个消息队列，
 不追求强一致性。
 
内存中的数据易掉电易失，我们看看它如何做持久化
一般的存储层都有以下两种方式来做持久化
1.快照/副本
2.日志

###RDB
在redis中有两种技术RDB和AOF,分别对应以上的1和2
RDB:就是redis的快照，具有时点性，非阻塞则能持久化
    linux中管道
        衔接前一个命令的输出作为后一个命令的输入
        管道会触发创建【子进程】
        echo $$ | more     #$$优先级高于管道|
        echo $BASHPID | more
    使用linux的时候：父子进程
    父进程的数据，子进程是不可以看到的（除非使用export，子进程修改后，父进程是看不到的。父进程修改的数据，子进程可以看到）
    常规思想：进程是数据隔离的
    进阶思想：父进程其实可以让子进程看见数据
    创建子进程的速度应该是什么程度？如果父进程是redis，那么内存数据10G如果想要做到非阻塞的情况下开辟一个子进程，需要考虑速度和空间？
    在linux中有一个fork的系统调用，它是怎么实现的呢？
    在linux中内存的地址空间都是一个线性的，程序默认认为自己占用所有的内存空间，它会有一个虚拟地址，这个虚拟地址会映射到物理内存。比如redis中有一个
    key在物理内存的3号地址，那么虚拟内存中对应的地址可能就是8号。如果此时想要再来一个子进程，可以把数据拷贝到这个子进程中，也可以使用一种比较快的方式fork。
    fork后的子进程中，是直接拷贝了指向物理内存中的元素指针，把元素指针和当前进程中的虚拟地址绑定。其实数据是没有拷贝，都是指向了物理地址.
    如果此时在父进程中有元素被修改了则触发copyonwrite（fork实现了写时复制机制），则首先在物理内存中写入，然后父进程指向以前元素的指针就拷贝成新元素的指针
    ，子进程中相应的元素对应的还是以前的指针。
    子进程负责把数据写入大rdb文件中，看不到父进程中对元素的修改。只要8点钟fork的子进程，子进程就会把当前时间点的所有元素指针拷贝到子进程，父进程对元素的修改
    都不会导致子进程的变化。（时点数据）
    
命令：save和bgsave,其中bgsave会触发fork创建子进程。在配置文件中有一个save选择，这个就是配置bgsave自动触发条件的。如果要关闭自动执行bgsave，则需要配置成save ""即可

RDB的弊端：
1.不支持拉链，只有一个dump.rdb
2.易丢失数据，不是实时写数据的
RDB的有点：
类似java中的序列化，恢复的速度相对较快

基于以上的优缺点就发展出了AOF
###AOF
redis的写操作记录在文件中，故丢失数据较少。
Redis中RDB和AOF可以同时开启，如果同时开启只会用AOF恢复
Redis4.0以后AOF中包含RDB全量，增加记录新的写操作

redis运行了10年，开启了AOF，在10年头redis挂了，请问
1.aof多大
2.恢复要多久
    
弊端：体量无线变大 恢复慢

redis怎么克服这个弊端呢？
4.0以前：重写（删除抵消的命令，合并重复的命令），最终也是一个纯指令的日志文件
4.0以后：重写（将老的数据rdb到aof文件中，将增量的以指令的方式append到aof中），最终aof是一个混合体，利用了rdb的快，利用了日志的全量

类似的方式在hdfs中也有
hdfs:fsimage+edits.log让日志只记录增量，合并的过程
    

redis是内存数据库，在append写操作时会触发IO,有写个级别可以调
首先开启 appendonly yes
调节级别在这个属性appendfsync,这个属性有3个值可以选择
no        #内核buffer满了在刷到磁盘
always      #每次都调flush写到磁盘
everysec    #每秒调flush写到磁盘 鉴于no和always之间 
默认everysec



aof-use-rdb-preamble yes    #开启混合模式


###演示rdb和aof
1.首先不要让其在后台启动,主要是观察日志
daemonize no
2.关闭日志文件
#logfile /var/log/redis_6379.log
注释调后，会把日志打印到屏幕上，否则就会写到这个日志文件
3.配置rdb
dbfilename dump.rdb
dir /var/lib/redis/6379
4.开始aof
appendonly yes
5.混合关闭或者开启
aof-use-rdb-preamble no/yes
6.启动redis-server
redis-server /etc/redis/6379.conf
启动后，在数据目录文件夹/var/lib/redis/6379下就有appendonly.aof空文件
7.在另一个客户端中输入set k1 aaa后再观察appendonly.aof这个种就有内容了，打开如下
```
*2
$6
SELECT
$1
0
*3
$3
set
$2
k1
$3
aaa
```
其中*2   *表示下边有几个个元素组成  后边的2表示的就是2个元素  select和0
$描述元素有几个字符或字节组成 $6表示的就是select由6个元素组成

*3 表示 set k1 aaa 这三个元素

这个就是aof种记录指令的协议

再满足配置参数save条件后，当前目录下还会生成dump.rdb文件，
打开后，发现开头是REDIS，其余的看不懂，因为它是二进制的，快照是序列化的

我们可以使用以下命令来检查这个二进制文件dump.rdb
redis-check-rdb dump.rdb
```
[offset 0] Checking RDB file dump.rdb
[offset 26] AUX FIELD redis-ver = '5.0.5'
[offset 40] AUX FIELD redis-bits = '64'
[offset 52] AUX FIELD ctime = '1605688320'
[offset 67] AUX FIELD used-mem = '812520'
[offset 83] AUX FIELD aof-preamble = '0'
[offset 85] Selecting DB ID 0
[offset 116] Checksum OK
[offset 116] \o/ RDB looks OK! \o/
[info] 2 keys read
[info] 0 expires
[info] 0 already expired
```

###AOF重写
我们再redis客户端中写入以下指令
set k1 a
set k1 b
set k1 c
再没有进行aof重写时，这些指令都会记录再aof文件中，但显然有很多垃圾记录，此时我们执行aof重写指令
bgrewriteaof
执行过后，aof文件体积就会变小，里面的指令记录也会删除没用的，按照例子中的只会保存set k1 c的命令记录

auto-aof-rewrite-percentage 100
auto-aof-rewriter-min-size 64mb  #redis会记忆最后一次重写的aof体积大小，刚开始增长到64M的时候就aof，后边增长到100%的时候就再重写


###演示aof和rdb的混合结构
1.将混合选项打开
aof-use-rdb-preamble yes
2.启动redis-server /etc/redis/6379.conf
3.执行以下命令
set k1 a
set k1 b
set k1 c
set k1 d
4.再没有进行aof重写时，aof文件中和以前aof文件格式一样
5.执行bgrewriteaof
6.打开aof文件，发现aof文件中开头是REDIS的rdb文件的格式
7.继续执行写入的命令
8.再次打开文件发现新增的命令是以前aof文件格式的指令记录
这个就是rdb和aof的混合体。redis4.0以后就有这个特性
















