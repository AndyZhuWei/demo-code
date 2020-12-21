# mysql日志
## 分类
binlog:二进制日志，归档日志
redolog: 
undolog:

对于MySql server中存在的时binlog
对于存储引擎存储的是redolog和undolog
说日志的时候必须要清楚哪个日志归属哪个层次

事务的ACID底层是通过什么来保证的？
原子性是通过undolog日志来保证的
持久化特性就是通过redolog日志来保证的
隔离性是通过锁机制
一致性则是通过以上3个来保证的



###Redo日志----innodb存储引擎的日志文件（前滚日志）
*当发生数据修改的时候，innodb引擎会先将记录写到redo log中，并更新内存，此时更新就算
完成了，同时innodb引擎会在合适的时机将记录操作到磁盘中
* Redolog是固定大小的，是循环写的过程
* 有了redolog之后，innodb就可以保证即使数据库发生异常重启，之前的记录也不会丢失，叫做
  crash-safe
WAL:(write ahead log 预写日志) 提高性能

疑惑：
既然要避免io，为什么写redo log的时候不会造成io的问题？
![redo_log](D:\IT\demo-code\src\main\java\com\example\demo\lpj\redo_log.png)

###Undo log（也可以叫回滚日志）
*Undo log是为了实现事务的原子性，在MySql数据库InnoDB存储引擎中，还用Undo Log来实现多版本并发控制（简称：MVCC）
* 在操作任何数据之前，首先将数据备份到一个地方（这个存储数据备份的地方称为Undo Log）.任何进行数据的修改。
  如果出现了错误或者用户执行了ROLLBACK语句，系统可以利用Undo log中的备份将数据恢复到事务开始之前的状态
* 注意：undo log是逻辑日志，可以理解为：
  -当delete一条记录时，undo log中会记录一条对应的insert记录
  -当inset一条记录时，undo log会记录一条对应的delete记录
  -当update一条记录时，它记录一条对应相反的update记录

###binlog--服务端的日志文件
*Binlog是Server层的日志，主要做mysql功能层面的事情
*与redo日志的区别：
  -1.redo是innodb存储独有的，binlog是所有存储引擎都可以使用的
  -2.redo是物理日志，记录的是在某个数据页上做了什么修改，binlog是逻辑日志，记录的是这个语句的原始逻辑
  -3.redo是循环写的，空间会用完，binlog是可以追加写的，不会覆盖之前的日志信息
  
binlog日志默认是不开启的
log_bin  OFF
binlog_format  STATEMENT/ROW

*Binlog中会记录所有的逻辑，并且采用追加写的方式
*一般在企业中数据库会有备份系统，可以定期执行备份，备份的周期可以自己设置
*恢复数据的过程：
  -1.找到最近一次的全量备份数据
  -2.从备份的时间点开始，将备份的binlog取出来，重放到要恢复的那个时刻
  

## 数据更新的流程
执行流程：
1.执行器先从引擎中找到数据，如果在内存中直接返回，如果不在内存中，查询后返回
2.执行器拿到数据之后会先修改数据，然后调用引擎接口重新写入数据
3.引擎将数据更新到内存，同时写数据到redo中，此时处于prepare阶段，并通知执行器执行完成，
 随时可以操作
4.执行器生成这个操作的binlog
5.执行器调用引擎的事务提交接口，引擎把刚刚写完的redo改成commit状态，更新完成。
![流程图](D:\IT\demo-code\src\main\java\com\example\demo\lpj\image\数据更新流程.png)

面试题：
为什么redolog要分两个阶段？为什么不可以先写binlog,在写redolog？
其实这些都是为了保证redolog和binlog数据的一致性

## redo log的两阶段提交
* 先写redolog后写binlog:假设在redolog写完，binlog还欸呦写完的时候，MySql进行异常重启。由于我们前面说过，
  redolog写完后，系统即使崩溃，任然能够把数据恢复回来，所以恢复后这一行c的值是1.但是由于binlog没有写完就crash了，
  这个时候binlog里面就没有记录这个语句。因此，之后备份日志的时候，存起来的binlog里面就没有这条语句。然后
  你会发现，如果需要用这个binlog来恢复临时库的话，由于这个语句的binlog会丢失，这个临时库就会少了这一次更新，恢复出来的这
  一行c的值就是0，与原库的值不同。
* 先写binlog后写redo log:如果在binlog写完之后crash，由于redolog还没写，崩溃恢复以后这个事务无效，所以这一行c的值
是0，但是binlog里面已经记录了“把c从0改成1”这个日志。所以，在之后用binlog来恢复的时候就多了一个事务出来，恢复出来的这一行c的值就是1，
  与原库的值不同。



