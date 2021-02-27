#set
list类型的时候就是放入很多的元素，set也是那为什么还需要这个set呢
，因为list是有序的（存入或插入的顺序）而且可以重复

但是set是去重，而且是无序的


SADD key member [member ...]
  summary: Add one or more members to a set
  since: 1.0.0

  SCARD key
  summary: Get the number of members in a set
  since: 1.0.0

  SDIFF key [key ...]
  summary: Subtract multiple sets
  since: 1.0.0

  SDIFFSTORE destination key [key ...]
  summary: Subtract multiple sets and store the resulting set in a key
  since: 1.0.0

  SINTER key [key ...]
  summary: Intersect multiple sets
  since: 1.0.0

  SINTERSTORE destination key [key ...]
  summary: Intersect multiple sets and store the resulting set in a key
  since: 1.0.0

  SISMEMBER key member
  summary: Determine if a given value is a member of a set
  since: 1.0.0

  SMEMBERS key
  summary: Get all the members in a set
  since: 1.0.0

  SMOVE source destination member
  summary: Move a member from one set to another
  since: 1.0.0

  SPOP key [count]
  summary: Remove and return one or multiple random members from a set
  since: 1.0.0

  SRANDMEMBER key [count]
  summary: Get one or multiple random members from a set
  since: 1.0.0

  SREM key member [member ...]
  summary: Remove one or more members from a set
  since: 1.0.0

  SSCAN key cursor [MATCH pattern] [COUNT count]
  summary: Incrementally iterate Set elements
  since: 2.8.0

  SUNION key [key ...]
  summary: Add multiple sets
  since: 1.0.0

  SUNIONSTORE destination key [key ...]
  summary: Add multiple sets and store the resulting set in a key
  since: 1.0.0
  
  
###基本操作
```
127.0.0.1:6379> sadd k1 tom sean xxoo ooxx
(integer) 4
127.0.0.1:6379> scard k1
(integer) 4
127.0.0.1:6379> smembers k1
1) "ooxx"
2) "tom"
3) "xxoo"
4) "sean"
127.0.0.1:6379> sadd k1 tom
(integer) 0
127.0.0.1:6379> smembers k1
1) "ooxx"
2) "tom"
3) "xxoo"
4) "sean"
127.0.0.1:6379> srem k1 ooxx xxoo
(integer) 2
127.0.0.1:6379> smembers k1
1) "tom"
2) "sean"
127.0.0.1:6379>
```

### 集合操作 交集 并集和差集
```
127.0.0.1:6379> sadd k2 1 2 3 4 5
(integer) 5
127.0.0.1:6379> sadd k3 4 5 6 7 8
(integer) 5
127.0.0.1:6379> smembers k2
1) "1"
2) "2"
3) "3"
4) "4"
5) "5"
127.0.0.1:6379> smembers k3
1) "4"
2) "5"
3) "6"
4) "7"
5) "8"
127.0.0.1:6379> sinter k2 k3       #交集 直接输出
1) "4"
2) "5"
127.0.0.1:6379> sinterstore dest k2 k3 #交集 保存到dest中
(integer) 2
127.0.0.1:6379> smembers dest
1) "4"
2) "5"
127.0.0.1:6379> sunion k2 k3 #并集 输出后会对重复的元素进行过滤
1) "1"
2) "2"
3) "3"
4) "4"
5) "5"
6) "6"
7) "7"
8) "8"
127.0.0.1:6379> sdiff k2 k3 #差集 有方向性
1) "1"
2) "2"
3) "3"
127.0.0.1:6379> sdiff k3 k2 #差集 有方向性
1) "6"
2) "7"
3) "8"
127.0.0.1:6379>  
```

###随机事件
srandmember key1 count: count为正数的时候，redis会尽量选取count的不重复的元素，如果key1中不够count个元素，则会实际个数的元素
                        count为负数，取数的可能会重复，数量一定是|count|个
                        count为0不返回
场景：
1.抽奖
有3件礼物，给很多人抽奖
```
127.0.0.1:6379> sadd k1 tom ooxx xxoo xoxo oxox xoox oxxo
(integer) 7
127.0.0.1:6379> srandmember k1 5 
1) "xoox"
2) "oxox"
3) "xxoo"
4) "tom"
5) "xoxo"
127.0.0.1:6379> srandmember k1 10 
1) "oxox"
2) "xxoo"
3) "ooxx"
4) "xoox"
5) "oxxo"
6) "tom"
7) "xoxo"
127.0.0.1:6379> srandmember k1 -5
1) "oxox"
2) "xoox"
3) "ooxx"
4) "ooxx"
5) "oxxo"
127.0.0.1:6379> srandmember k1 -10
 1) "xxoo"
 2) "tom"
 3) "tom"
 4) "tom"
 5) "tom"
 6) "oxox"
 7) "oxox"
 8) "ooxx"
 9) "xoxo"
10) "xoox"
127.0.0.1:6379> srandmember k1 0
(empty list or set)
```

2.家庭争斗
洗碗 取名字 


srandmember VS spop
spop随机得到一个元素并且删除这个元素 而srandmember不会删除元素




