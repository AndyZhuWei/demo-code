# list
redis中的value类型是list的
查看list组都有什么命令
help @list

#blpop
BLPOP key [key ...] timeout
  summary: Remove and get the first element in a list, or block until one is available
  since: 2.0.0
例子：
BLPOP key [key ...] timeout：阻塞的从key中拿去数据（从左到右），timeout如果为0则一直阻塞，否则等待相应的秒数

#brpop
  BRPOP key [key ...] timeout
  summary: Remove and get the last element in a list, or block until one is available
  since: 2.0.0
例子：
brpop key [key ...] timeout：阻塞的从key中拿去数据（从右到左），timeout如果为0则一直阻塞，否则等待相应的秒数

  BRPOPLPUSH source destination timeout
  summary: Pop a value from a list, push it to another list and return it; or block until one is available
  since: 2.2.0

#lindex
  LINDEX key index
  summary: Get an element from a list by its index
  since: 1.0.0
例子：
通过指定的索引查询元素
lindex k1 -1 :取出最后一个元素

#linsert
  LINSERT key BEFORE|AFTER pivot value
  summary: Insert an element before or after another element in a list
  since: 2.2.0
例子：
在指定位置上插入数据
LINSERT key BEFORE|AFTER pivot value：表示在key这个链表中的pivot前边或者后边插入元素value

#llen
  LLEN key
  summary: Get the length of a list
  since: 1.0.0
例子：
查询list元素的长度


#lpop
  LPOP key
  summary: Remove and get the first element in a list
  since: 1.0.0
例子：
从左边弹出一个元素
lpop k3

#lpush
  LPUSH key value [value ...]
  summary: Prepend one or multiple values to a list
  since: 1.0.0
例子：
这个命令是从左到右开始放元素
127.0.0.1:6379> lpush k3 a b c d e f             #放到k3中 是这样存储f e d c b a
(integer) 6
127.0.0.1:6379> type k3
list
127.0.0.1:6379> object encoding k3
"quicklist"

 LPUSHX key value
  summary: Prepend a value to a list, only if the list exists
  since: 2.2.0

#lrange
  LRANGE key start stop
  summary: Get a range of elements from a list
  since: 1.0.0
例子：
得到指定范围的元素，l表示的是用于操作list的value
lrange k3 0 -1

#lrem
  LREM key count value
  summary: Remove elements from a list
  since: 1.0.0
例子：
移除指定的元素 count表示移除几个
lrem k3 2 a:将k3列表中的a移除2个（count如果是正数则从head的右边开始移除，count如果是负数则从tail的左边开始移除，count为0则不进行移除操作）
  
  
#lset
  LSET key index value
  summary: Set the value of an element in a list by its index
  since: 1.0.0
例子：
更新指定的索引的元素值
lset k1 0 1:将0索引的值更新为1

#ltrim
  LTRIM key start stop
  summary: Trim a list to the specified range
  since: 1.0.0
例子：
删除指定范围外的元素
ltrim k2 0 -1：一个元素都不会删除
ltrim k2 2 -2:删除前边2个和后边1个元素
  
#rpop
  RPOP key
  summary: Remove and get the last element in a list
  since: 1.0.0
例子
从右边弹出

  RPOPLPUSH source destination
  summary: Remove the last element in a list, prepend it to another list and return it
  since: 1.2.0

#rpush
  RPUSH key value [value ...]
  summary: Append one or multiple values to a list
  since: 1.0.0
 例子：
 从右到左开始放元素
 rpush k4 a b c d e f   #在k2中实际是a b c d e f

  RPUSHX key value
  summary: Append a value to a list, only if the list exists
  since: 2.2.0
  
  
  
  
提到list我们就会想到链表
链表：单向 双向 有环和无环
对应key中会存储head和tail

###list可以描述栈（正向命令）
lpush/lpop或者rpush/rpop

###list可以描述队列（先进先出）（反向命令）
lpush/rpop或者rpush/lpop

###list可以代替数组
lindex/lset

###以b开头的命令，可以支持简单的阻塞队列或单播队列（多个阻塞的客户端，只有第一个阻塞的会先拿到值，后续的继续阻塞）
blpop/brpop