#sorted set
数据去重且排序

首先sorted set是一个集合，数据可以去重，还可以指定排序规则

使用维度
分值，分值一样按照名称字典序排列
排名
正负向索引

BZPOPMAX key [key ...] timeout
  summary: Remove and return the member with the highest score from one or more sorted sets, or block until one is available
  since: 5.0.0

  BZPOPMIN key [key ...] timeout
  summary: Remove and return the member with the lowest score from one or more sorted sets, or block until one is available
  since: 5.0.0

  ZADD key [NX|XX] [CH] [INCR] score member [score member ...]
  summary: Add one or more members to a sorted set, or update its score if it already exists
  since: 1.2.0

  ZCARD key
  summary: Get the number of members in a sorted set
  since: 1.2.0

  ZCOUNT key min max
  summary: Count the members in a sorted set with scores within the given values
  since: 2.0.0

  ZINCRBY key increment member
  summary: Increment the score of a member in a sorted set
  since: 1.2.0

  ZINTERSTORE destination numkeys key [key ...] [WEIGHTS weight] [AGGREGATE SUM|MIN|MAX]
  summary: Intersect multiple sorted sets and store the resulting sorted set in a new key
  since: 2.0.0

  ZLEXCOUNT key min max
  summary: Count the number of members in a sorted set between a given lexicographical range
  since: 2.8.9

  ZPOPMAX key [count]
  summary: Remove and return members with the highest scores in a sorted set
  since: 5.0.0

  ZPOPMIN key [count]
  summary: Remove and return members with the lowest scores in a sorted set
  since: 5.0.0

  ZRANGE key start stop [WITHSCORES]
  summary: Return a range of members in a sorted set, by index
  since: 1.2.0

  ZRANGEBYLEX key min max [LIMIT offset count]
  summary: Return a range of members in a sorted set, by lexicographical range
  since: 2.8.9

  ZRANGEBYSCORE key min max [WITHSCORES] [LIMIT offset count]
  summary: Return a range of members in a sorted set, by score
  since: 1.0.5

  ZRANK key member
  summary: Determine the index of a member in a sorted set
  since: 2.0.0

  ZREM key member [member ...]
  summary: Remove one or more members from a sorted set
  since: 1.2.0

  ZREMRANGEBYLEX key min max
  summary: Remove all members in a sorted set between the given lexicographical range
  since: 2.8.9

  ZREMRANGEBYRANK key start stop
  summary: Remove all members in a sorted set within the given indexes
  since: 2.0.0

  ZREMRANGEBYSCORE key min max
  summary: Remove all members in a sorted set within the given scores
  since: 1.2.0

  ZREVRANGE key start stop [WITHSCORES]
  summary: Return a range of members in a sorted set, by index, with scores ordered from high to low
  since: 1.2.0

  ZREVRANGEBYLEX key max min [LIMIT offset count]
  summary: Return a range of members in a sorted set, by lexicographical range, ordered from higher to lower strings.
  since: 2.8.9

  ZREVRANGEBYSCORE key max min [WITHSCORES] [LIMIT offset count]
  summary: Return a range of members in a sorted set, by score, with scores ordered from high to low
  since: 2.2.0

  ZREVRANK key member
  summary: Determine the index of a member in a sorted set, with scores ordered from high to low
  since: 2.0.0

  ZSCAN key cursor [MATCH pattern] [COUNT count]
  summary: Incrementally iterate sorted sets elements and associated scores
  since: 2.8.0

  ZSCORE key member
  summary: Get the score associated with the given member in a sorted set
  since: 1.2.0

#zunionstore
  ZUNIONSTORE destination numkeys key [key ...] [WEIGHTS weight] [AGGREGATE SUM|MIN|MAX]
  summary: Add multiple sorted sets and store the resulting sorted set in a new key
  since: 2.0.0
  
例子：
ZUNIONSTORE destination numkeys key [key ...] [WEIGHTS weight] [AGGREGATE SUM|MIN|MAX]
其中
destination:目标key
numkeys:即将并集的key的个数
key:相应的key
[WEIGHTS weight]:对应key的权重，如果不屑，默认是1
[AGGREGATE SUM|MIN|MAX]：分数怎么聚合，可以时sum或min或max。默认时sum
```
127.0.0.1:6379> zadd k1 80 tom 60 sean 70 baby
(integer) 3
127.0.0.1:6379> zadd k2 60 tome 100 sean 40 yiming
(integer) 3
127.0.0.1:6379> zunionstore unkey 2 k1 k2 
(integer) 5
127.0.0.1:6379> zrange unkey 0 -1 withscores
 1) "yiming"
 2) "40"
 3) "tome"
 4) "60"
 5) "baby"
 6) "70"
 7) "tom"
 8) "80"
 9) "sean"
10) "160"
127.0.0.1:6379> zunionstore unkey1 2 k1 k2 weights 1 0.5
(integer) 5
127.0.0.1:6379> zrange unkey1 0 -1 withscores
 1) "yiming"
 2) "20"
 3) "tome"
 4) "30"
 5) "baby"
 6) "70"
 7) "tom"
 8) "80"
 9) "sean"
10) "110"
127.0.0.1:6379> zunionstore unkey1 2 k1 k2 aggregate max
(integer) 5
127.0.0.1:6379> zrange unkey1 0 -1 withscores
 1) "yiming"
 2) "40"
 3) "tome"
 4) "60"
 5) "baby"
 6) "70"
 7) "tom"
 8) "80"
 9) "sean"
10) "100"
127.0.0.1:6379>
```


  
  
命令以z卡头，s被set占用了

###基本用法
127.0.0.1:6379> zadd k1 8 apple 2 banana 3 orange #在物理内存中是按左小右大的顺序排列，不随命令而发生变化
(integer) 3
127.0.0.1:6379> zrange k1 0 -1 #按照元素索引取出指定的所有元素
1) "banana"
2) "orange"
3) "apple"
127.0.0.1:6379> zrange k1 0 -1 withscores #按照元素索引取出指定的所有元素并且取出相应的分值
1) "banana"
2) "2"
3) "orange"
4) "3"
5) "apple"
6) "8"
127.0.0.1:6379> zrangebyscore k1 3 8  #按照分值大小取出元素，取出分值在3和8之间的元素
1) "orange"
2) "apple"
127.0.0.1:6379> zrange k1 0 1   #按照分值升序取出前两位
1) "banana"
2) "orange"
127.0.0.1:6379> zrevrange k1 0 1 #按照分值降序取出前两位
1) "apple"
2) "orange"
127.0.0.1:6379> zrange k1 -2 -1  #按照分值升序取出最后两位
1) "orange"
2) "apple"
127.0.0.1:6379> zscore k1 apple  #得到相应元素的分值
"8"
127.0.0.1:6379> zrank k1 apple  #得到相应元素的排名
(integer) 2
127.0.0.1:6379> zincrby k1 2.5 banana #对banana的分值增加2.5
"4.5"
127.0.0.1:6379> zrange k1 0 -1 withscores 
1) "orange"
2) "3"
3) "banana"
4) "4.5"
5) "apple"
6) "8"
127.0.0.1:6379>

###集合操作 并集 交集
例子：
ZUNIONSTORE destination numkeys key [key ...] [WEIGHTS weight] [AGGREGATE SUM|MIN|MAX]
其中
destination:目标key
numkeys:即将并集的key的个数
key:相应的key
[WEIGHTS weight]:对应key的权重，如果不屑，默认是1
[AGGREGATE SUM|MIN|MAX]：分数怎么聚合，可以时sum或min或max。默认时sum
```
127.0.0.1:6379> zadd k1 80 tom 60 sean 70 baby
(integer) 3
127.0.0.1:6379> zadd k2 60 tome 100 sean 40 yiming
(integer) 3
127.0.0.1:6379> zunionstore unkey 2 k1 k2 
(integer) 5
127.0.0.1:6379> zrange unkey 0 -1 withscores
 1) "yiming"
 2) "40"
 3) "tome"
 4) "60"
 5) "baby"
 6) "70"
 7) "tom"
 8) "80"
 9) "sean"
10) "160"
127.0.0.1:6379> zunionstore unkey1 2 k1 k2 weights 1 0.5
(integer) 5
127.0.0.1:6379> zrange unkey1 0 -1 withscores
 1) "yiming"
 2) "20"
 3) "tome"
 4) "30"
 5) "baby"
 6) "70"
 7) "tom"
 8) "80"
 9) "sean"
10) "110"
127.0.0.1:6379> zunionstore unkey1 2 k1 k2 aggregate max
(integer) 5
127.0.0.1:6379> zrange unkey1 0 -1 withscores
 1) "yiming"
 2) "40"
 3) "tome"
 4) "60"
 5) "baby"
 6) "70"
 7) "tom"
 8) "80"
 9) "sean"
10) "100"
127.0.0.1:6379>
```

###在使用sorted_set的时候排序原理，以及增删改查的速度增么样？
其实底层时使用的跳跃表结构skip_list，类平衡树
之前链表中只有指向前后的指针，在跳跃表中多了一个指向层次的指针。相同层次的元素还有有指针相连。

插入过程：
当在跳跃表中插入一个元素的时候，首先会和头指针中的元素的最高一层进行比较，在最高一层比较的时候发现大于当前节点，向
这一层的后边元素进行比较，如果后边元素为空，则到下一层接着进行比较，如果在下一层的右边节点元素大于当前插入的元素，则到下一层
接着和左边的进行比较，如此进行，最终达到最底层元素进行比较插入。插入成功后会进行随机造层。可能会把当前插入的元素造3层放到各个层次上。
以上过程其实就时间接的牺牲存储空间来换取查询时间

修改过程：
先删除指定的元素，然后走一遍插入过程即可

这个跳跃表增改删查平均值相对最稳定