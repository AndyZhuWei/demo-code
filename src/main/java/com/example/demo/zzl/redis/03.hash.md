#hash
如果每个人有多个维度的信息需要存储 年龄 性别 姓名。用redis进行存储，用何种数据结构比较合适，
在没有hash之前我们可以以这样的方式处理
set sean::name sean
set sean::age 18
set sean::sex f

客户端在使用的时候可以这样
从keys sean*这个命令的返回值中拿到sean的所有维度信息，如何循环遍历执行get sean::name/sean:age/sean:sex等信息了

从以上的处理过程可以发现处理的成本还是很高，需要处理好几次的IO。
这个时候hash的类型就出现了


hash的命令都是以h开头的


HDEL key field [field ...]
  summary: Delete one or more hash fields
  since: 2.0.0

  HEXISTS key field
  summary: Determine if a hash field exists
  since: 2.0.0

  HGET key field
  summary: Get the value of a hash field
  since: 2.0.0

  HGETALL key
  summary: Get all the fields and values in a hash
  since: 2.0.0

  HINCRBY key field increment
  summary: Increment the integer value of a hash field by the given number
  since: 2.0.0

  HINCRBYFLOAT key field increment
  summary: Increment the float value of a hash field by the given amount
  since: 2.6.0

  HKEYS key
  summary: Get all the fields in a hash
  since: 2.0.0

  HLEN key
  summary: Get the number of fields in a hash
  since: 2.0.0

  HMGET key field [field ...]
  summary: Get the values of all the given hash fields
  since: 2.0.0

  HMSET key field value [field value ...]
  summary: Set multiple hash fields to multiple values
  since: 2.0.0

  HSCAN key cursor [MATCH pattern] [COUNT count]
  summary: Incrementally iterate hash fields and associated values
  since: 2.8.0

  HSET key field value
  summary: Set the string value of a hash field
  since: 2.0.0

  HSETNX key field value
  summary: Set the value of a hash field, only if the field does not exist
  since: 2.0.0

  HSTRLEN key field
  summary: Get the length of the value of a hash field
  since: 3.2.0

  HVALS key
  summary: Get all the values in a hash
  since: 2.0.0



##存储用户多个维度的信息
```
127.0.0.1:6379> clear
127.0.0.1:6379> hset sean name sean
(integer) 1
127.0.0.1:6379> hset sean age 18
(integer) 1
127.0.0.1:6379> hset sean address beijing
(integer) 1
127.0.0.1:6379> hget sean age
"18"
127.0.0.1:6379> hget sean address
"beijing"
127.0.0.1:6379> hget sean name
"sean"
127.0.0.1:6379> hmget sean name age address
1) "sean"
2) "18"
3) "beijing"
127.0.0.1:6379> hkeys sean    取出sean这个key所有的fields
1) "name"
2) "age"
3) "address"
127.0.0.1:6379> hvals sean   取出sean这个key所有的values
1) "sean"
2) "18"
3) "beijing"
127.0.0.1:6379> hgetall sean   取出sean这个key所有的fields和values
1) "name"
2) "sean"
3) "age"
4) "18"
5) "address"
6) "beijing"
127.0.0.1:6379> 
```
##对数值计算的支持
```
127.0.0.1:6379> hincrbyfloat sean age 0.5
"18.5"
127.0.0.1:6379> hget sean age
"18.5"
127.0.0.1:6379> hincrbyfloat sean age -1
"17.5"
127.0.0.1:6379>
```
使用场景：
1.在一个商品的详情页中，里面有很多元素信息，我们通过很多接口调用还是从redis中直接拿出这个商品的相应信息呢？答案显而易见，redis中的hash存储商品的信息，
我们通过一个命令就可以拿出这个商品相关的任何信息了
2.微博中点赞数，评论数等等

hash就是一个简单的document





