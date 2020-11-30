# String
字符串类型的可以操作字符类型、数值类型和bitmaps
help @string

#append
  APPEND key value
  summary: Append a value to a key
  since: 2.0.0
例子：
append k1 " world" :对k1的值追加" world"

#bitcount
  BITCOUNT key [start end]
  summary: Count set bits in a string
  since: 2.6.0
例子：
这个命令是统计在字节序号[start end]范围内，二进制位出现1的个数
bitcount k1 0 1：统计k1在字节0和1范围内二进制位1出现的个数


  BITFIELD key [GET type offset] [SET type offset value] [INCRBY type offset increment] [OVERFLOW WRAP|SAT|FAIL]
  summary: Perform arbitrary bitfield integer operations on strings
  since: 3.2.0

#bitop
  BITOP operation destkey key [key ...]
  summary: Perform bitwise operations between strings
  since: 2.6.0
例子
BITOP operation destkey key [key ...]:operation表示操作 destkey目标key,最终的结果放在这个key中。 后边的key可以接入多个，表示参与计算的key
```
127.0.0.1:6379> setbit k1 1 1  #k1的二进制位位 01000000
(integer) 0
127.0.0.1:6379> setbit k1 7 1  #k1的二进制位位 01000001
(integer) 0
127.0.0.1:6379> get k1
"A"
127.0.0.1:6379> setbit k2 1 1 #k2的二进制位位 01000000
(integer) 0
127.0.0.1:6379> setbit k2 6 1 #k2的二进制位位 01000010
(integer) 0
127.0.0.1:6379> get k2
"B"
127.0.0.1:6379> bitop and andkey k1 k2  #对k1和k2进行与运算 01000001 &  01000010 = 01000000
(integer) 1
127.0.0.1:6379> get andkey   #01000000的值就是@
"@"
127.0.0.1:6379> bitop or orkey k1 k2 #对k1和k2进行或运算 01000001 |  01000010 = 01000011
(integer) 1
127.0.0.1:6379> get orkey #01000011的值就是@
"C"
```


#bitpos
  BITPOS key bit [start] [end]
  summary: Find first bit set or clear in a string
  since: 2.8.7
例子：
bitpos key bit start end:start和end表示的是字节索引
bitpos k1 1 0 0:在第一个（0，0）字节中查找第一个位数是1的二进制小标索引（全量的索引/偏移量offset）




#decr
  DECR key
  summary: Decrement the integer value of a key by one
  since: 1.0.0
例子
decr k1 对k1减1
#decrby 
  DECRBY key decrement
  summary: Decrement the integer value of a key by the given number
  since: 1.0.0
例子：
decrby k1 22 对k1减22

#get 
  GET key
  summary: Get the value of a key
  since: 1.0.0
例子：
get k1 得到k1的值

  GETBIT key offset
  summary: Returns the bit value at offset in the string value stored at key
  since: 2.2.0

#getrange
  GETRANGE key start end
  summary: Get a substring of the string stored at a key
  since: 2.4.0
例子：
getrange k1 6 10:从k1中下标从6的位置到10的位置进行截取。这个下标取值比较麻烦，所以redis
    中提供了正反向索引
getrange k1 6 -1:-1表示最后一位，倒数第二位就是-2
getrange k1 0 -1：表示取出k1的所有字符

#getset
  GETSET key value
  summary: Set the string value of a key and return its old value
  since: 1.0.0
例子：
set k1 hello
get k1
getset k1 zhuwei :执行后返回hello并且设置了k1位zhuwei。这个命令可以分开get然后再set
但这样就会发送过去两个命令，如果是getset只需要发送一次命令即一次IO就可完成，成本更低

#incr
  INCR key
  summary: Increment the integer value of a key by one
  since: 1.0.0
例子
incr k1 对数值型的字符串自增1

#incrby
  INCRBY key increment
  summary: Increment the integer value of a key by the given amount
  since: 1.0.0
例子：
incrby k1 22 对k1加22

#incrbyfloat
  INCRBYFLOAT key increment
  summary: Increment the float value of a key by the given amount
  since: 2.6.0
例子：
incrbyfloat k1 0.5 对k1加0.5
#mget
  MGET key [key ...]
  summary: Get the values of all the given keys
  since: 1.0.0
例子：
mget k3 k4:一次取出多个key对应的值

#mset
  MSET key value [key value ...]
  summary: Set multiple keys to multiple values
  since: 1.0.1
例子：
mset k3 a k4 b:一次设置多个key-value对的值

#msetnx
  MSETNX key value [key value ...]
  summary: Set multiple keys to multiple values, only if none of the keys exist
  since: 1.0.1
例子：
```
127.0.0.1:6379> msetnx k1 a k2 b
1
127.0.0.1:6379> mget k1 k2
a
b
127.0.0.1:6379> msetnx k2 c k3 d
0
127.0.0.1:6379> mget k1 k2 k3
a
b

127.0.0.1:6379>
msetnx是一个原子操作，有一个失败所有的就都失败了
```





  PSETEX key milliseconds value
  summary: Set the value and expiration in milliseconds of a key
  since: 2.6.0

#Set
  SET key value [expiration EX seconds|PX milliseconds] [NX|XX]
  summary: Set the string value of a key
  since: 1.0.0
例子：
set k1 ooxx nx : k1不存在的时候设置k1为ooxx。这个命令可以使用在分布式锁上。只能新建
set k2 hello xx: xx表示只有k2存在的时候才可以操作，只能做更新 
  
#setbit
  SETBIT key offset value
  summary: Sets or clears the bit at offset in the string value stored at key
  since: 2.2.0
  
例子
```
help setbit
setbit key offset value:其中offset是二进制位索引（从左到右）偏移量
setbit k1 1 1:其中第一个1表示二进制索引的下标值是1的二进制位变为1
              用二进制位表示就是01000000
strlen k1 :k1的长度就是1
get k1:显示的是@
setbit k1 7 1: 就是二进制位01000001 可以通过执行
get k1:显示的就是A
strlen k1:目前长度还是1
setbit k1 9 1:变为01000001 01000000
strlen k1:长度就变为2
get k1:显示的就是A@
```

  SETEX key seconds value
  summary: Set the value and expiration of a key
  since: 2.0.0

  SETNX key value
  summary: Set the value of a key, only if the key does not exist
  since: 1.0.0

#setrange
  SETRANGE key offset value
  summary: Overwrite part of a string at key starting at the specified offset
  since: 2.2.0
例子：
setrange k1 6 zhuwei:从k1的第6位开始覆盖zhuwei

#strlen
  STRLEN key
  summary: Get the length of the value stored in a key
  since: 2.2.0
例子：
strlen k1:返回k1对应value的长度


#type
上边讲到的都是string组下的命令，我们要看某一个key的value类型，则可以使用type
type k1

redis主要有5种基本类型，每种基本类型的操作都是和具体的value绑定在一起的
在key种有一个type属性，这个属性就描述的是value的类型
help set

  SET key value [expiration EX seconds|PX milliseconds] [NX|XX]
  summary: Set the string value of a key
  since: 1.0.0
  group: string

127.0.0.1:6379> set k1 99
OK
127.0.0.1:6379> type k1
string
127.0.0.1:6379> 
也就是说命令是哪个分组的，那么未来这个key对应的value就是哪个类型的

#Object
在key种还有一个属性是encoding，可以用object命令来观察
127.0.0.1:6379> object help
1) OBJECT <subcommand> arg arg ... arg. Subcommands are:
2) ENCODING <key> -- Return the kind of internal representation used in order to store the value associated with a key.
3) FREQ <key> -- Return the access frequency index of the key. The returned integer is proportional to the logarithm of the recent access frequency of the key.
4) IDLETIME <key> -- Return the idle time of the key, that is the approximated number of seconds elapsed since the last access to the key.
5) REFCOUNT <key> -- Return the number of references of the value associated with the specified key.

object encoding k2

这个输出的值取决于k2具体的内容，如果时数值型字符串，就会输出int。
否则就是embstr或raw

对于时数值型的字符串value我们可以执行以下命令
incr k1 对k1自增1
incrby k1 22 对k1加22
decr k1 对k1减1
decrby k1 22对k1减22
incrbyfloat k1 0.5 对k1加0.5

###二进制安全
观察以下的操作
127.0.0.1:6379> set k1 hello
OK
127.0.0.1:6379> strlen k1
(integer) 5
127.0.0.1:6379> set k2 9
OK
127.0.0.1:6379> object encoding k2
"int"
127.0.0.1:6379> strlen k2
(integer) 1
127.0.0.1:6379> append k2 999
(integer) 4
127.0.0.1:6379> get k2
"9999"
127.0.0.1:6379> object encoding k2
"raw"
127.0.0.1:6379> incr k2
(integer) 10000           #在redis做计算的时候会按照字节读取到内存后转换位int类型在做结算，计算成功后就会把
key对应的编码更新成int，后续在计算是直接读取内存做计算，不用再做转换了
127.0.0.1:6379> object encoding k2
"int"
127.0.0.1:6379> strlen k2
(integer) 5               #redis在取长度的时候并不因为这个是int编码而有所区别，对他而言5位数字的每一位就是一个字节
127.0.0.1:6379> set k3 a
OK
127.0.0.1:6379> get k3
"a"
127.0.0.1:6379> strlen k3
(integer) 1
127.0.0.1:6379> append k3 中
(integer) 4   #xshell中和redis通信是编码是utf-8 汉字是占3个字节,如果将编码改为gbk则是2个字节
127.0.0.1:6379> strlen k3
(integer) 4
127.0.0.1:6379> 

二进制安全：（hbase也是二进制安全的）
在redis进程与外界交互的时候，它只取字节流。

编码并不会影响数据的存储
演示：
再xshell中以utf-8编码连接后，输入
set k2 中
strlen k2 3
如果是gbk，则
set k3 中
strlen k3 2
退出后，用redis-cli --raw ：--raw表示格式化连接
此时
get k2 会正常显示中，而k3则不会。
如果不带--raw，redis只会识别ascill，超出的则会显示十六进制的信息
所以在使用redis的时候各客户端要约定要编码，redis中式不存在“数据类型”的

###二进制位操作（位图）
help setbit
setbit key offset value:其中offset是二进制位索引（从左到右）偏移量
setbit k1 1 1:其中第一个1表示二进制索引的下标值是1的二进制位变为1
              用二进制位表示就是01000000
strlen k1 :k1的长度就是1
get k1:显示的是@
setbit k1 7 1: 就是二进制位01000001 可以通过执行
get k1:显示的就是A
strlen k1:目前长度还是1
setbit k1 9 1:变为01000001 01000000
strlen k1:长度就变为2
get k1:显示的就是A@

标准的字符集 ascii(0开头的)
其他一般的叫扩展字符集
扩展：其他字符集不再对ascii重编码 0xxxxxxx
redis只会显示ascii，超出的再根据相应编码显示

在redis中最值钱的就是这个位图，举一个经典的例子
setbit
bitpos
bitcount
bitop

1.如果公司有用户，当统计用户的登陆天数且窗口随机，在这个需求情况下如何组织数据
以前可能会创建一个数据表保存数据每天登陆信息，这个表中每一次的登陆信息记录会占用很大的空间，每年很多人登陆上来后，这个表中会有很多数据，后边统计可能会很慢
如果用数据库成本会很高。我们可以用redis的位图解决

我们可以使用400个二进制位表示一个用户在一年的登陆信息，也就是50个字节的大小就可以表示。这个大小和数据比较起来成本很低，例如
setbit sean 1 1 表示设置sean在第2天登陆了
setbit sean 7 1 表示设置sean在第8天登陆了
setbit sean 364 1 表示设置sean在第365天登陆了
查询sean用户在最后两天是否登陆了就可以用以下命令
bitcount sean  -2 -1                 这样就可以低成本（空间低和时间快 是二进制位的计算）的解决这个问题
```
127.0.0.1:6379> setbit sean 1 1
(integer) 0
127.0.0.1:6379> setbit sean 7 1
(integer) 0
127.0.0.1:6379> setbit sean 364 1
(integer) 0
127.0.0.1:6379> bitcount sean -2 -1
(integer) 1
```

2.假如京东618做活动，活动内容是凡是登陆的用户就要送礼物，每人送一件，大库备货多少礼物？，假设京东有2亿用户
用户分为僵尸用户、冷热用户/忠诚用户
所以这个问题就变成了如何统计活跃用户

这个结构的设计只需要把第一个问题的结构旋转以下，让日期作为key，用户作为位信息（将用户id映射到位上即可）
setbit 20190101 1 1: 20190101这一天把第二个进制位设置位1，代表我登陆
setbit 20190102 1 1： 20190102这一天把第二个进制位设置位1，代表我又登陆
setbit 20190102 7 1： 20190102这一天把第八个进制位设置位1，代表其他人登陆
现在计算这两天的活跃用户数，则用以下命令
bitop or destkey 20190101 20190102
bitcount destkey 0 -1


3.秒杀，抢购，详情页点赞 评论数 收藏数等可以用redis中的用数值型的来操作代替数据库

































