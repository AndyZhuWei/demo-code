#性能监控
mysql架构层次
三个层次：client--->mysql server---> 存储引擎
客户端：
向数据库发送请求，采用数据库连接池，减少频繁的开关连接
mysql server：
 * 连接器
 控制用户的连接
 * 分析器
  词法分析
  语法分析
  AST
 * 优化器
  优化sql语句，规定执行流程
  RBO:基于规则的优化
  CBO:基于成本优化
 * 执行器
  sql语句的实际执行组件
 注意：再mysql8之后查询缓冲器已经被剔除了，原因是命中率太低
存储引擎
  不同的存放位置，不同的文件格式
  InnoDB:磁盘
  MyISAM:磁盘
  memory:内存
## 1使用show profile查询，可以指定具体的type
*查询一条sql语句执行的时间
1.set profiling=1;
2.查询语句
3.show profiles
*显示详细花费时间
show profile   #默认显示最近的一条sql语句所执行的时间，如果想看其他的则执行
show profile for query 'query_id';
###示例，显示各个type
show profile all for query n #显示所有性能信息
show profile block io for query n #显示io操作的次数
show profile context switches for query n #显示上下文切换次数，被动和主动
show profile cpu for query n #显示用户cpu时间、系统cpu时间
show profile ipc for query n #显示发送和接收的消息数量
memory:暂未实现
show profile page faults for query n #显示页错误数量
show profile source for query n #显示源码中的函数名称与位置
show profile swaps for query n #显示swap的次数

注意：show profile这个语句在未来可能被替代，有更好的工具（performance schema）

## 2使用performance schema来更加容易的监控mysql
show variables like 'performance_schema';
默认是打开的，不可以直接修改，是只读的，修改需要改配置文件
set performance_schema=off
具体用法参见13文档

## 3使用show processlist查看连接的线程个数，来观察是否有大量线程处于不正常的状态或者其他不正常的特征
```sql
mysql> show processlist;
+----+------+---------------------+---------------------+---------+-------+----------+------------------+
| Id | User | Host                | db                  | Command | Time  | State    | Info             |
+----+------+---------------------+---------------------+---------+-------+----------+------------------+
|  4 | root | localhost           | performance_schema  | Sleep   | 49767 |          | NULL             |
|  7 | root | 192.168.80.10:50048 | shanhu-manager-test | Sleep   | 51740 |          | NULL             |
|  8 | root | 192.168.80.10:50059 | shanhu-manager-test | Sleep   | 51740 |          | NULL             |
|  9 | root | 192.168.80.10:50064 | shanhu-manager-test | Sleep   | 51740 |          | NULL             |
| 10 | root | 192.168.80.10:50068 | shanhu-manager-test | Sleep   | 51740 |          | NULL             |
| 12 | root | localhost           | performance_schema  | Query   |     0 | starting | show processlist |
+----+------+---------------------+---------------------+---------+-------+----------+------------------+
6 rows in set (0.00 sec)
```
其中:
id：表示session id
user:表示操作的用户
Host:表示操作的主机
db：表示操作的数据库
command:表示命令类型
info:表示详细的sql语句
time:表示相应命令执行时间
state:表示命令执行状态

一般情况下，数据库连接池不会有问题，因为我们都使用的是数据库连接池。
数据库连接池都有哪些？
DBCP 基本没人用了
C3P0 老的项目还在用
druid 德鲁伊





#schema与数据类型优化

## 数据类型的优化
* 更小的通常更好
  
  应该尽量使用可以正确存储数据的最小数据类型，更小的数据类型通常更快，因为占用更少
  的磁盘，内存和CPU缓存，并且处理时需要的CPU周期更少，但是要确保没有低估需要存储
  的值的范围，如果无法确认哪个数据类型，就选择你认为不会超过的范围的最小类型
  
  案例：
  设计两个表，设计不同的数据类型，查看表的容量
  
  
* 简单就好
  
  简单数据类型的操作通常需要更少的CPU周期，例如：
  1. 整型比字符串操作代价更低，因为字符集和校对规则时字符比较比整型比较更复杂
  2. 使用mysql自建类型而不是字符串来存储日期和时间
  3. 用整型存储ip地址  INET_ATON('IP')/INET_NTOA('xxxx')
  
  案例：
  创建两张相同的表，改变日期的数据类型，查看SQL语句执行的速度

* 尽量避免null
  
  如果查询种包含可以为NULL的列，对mysql来说很难优化，因为可为null的列使得索引、
  索引统计和值比较都更加复杂，坦白来说，通常情况下null的列改为not null带来的性能
  提升比较小，所有没有必要将所有的表的schema进行修改，但是应该尽量避免设计成可为null的列
  
* 实际细则

  * 整数类型
     
     可以使用的几种整数类型：tinyint、smallint、mediumint、int、bigint分别使用
     8、16、24、32、64位存储空间
     尽量使用满足需求的最小数据类型
  * 字符和字符串类型
     * varchar 根据实际内容长度保存数据
       1. 使用最小的符合需求的长度
       2. varchar(n)n小于等于255使用额外一个字节保存长度，n>255使用额外两个字节保存长度
       3. varchar(5)与varchar(255)保存同样的内容，硬盘存储空间相同，但内存空间占用不同，是
          指定的大小
       4. varchar在mysql5.6之前变更长度，或者从255一下变更到255以上时，都会导致锁表。
       
       应用场景
         1. 存储长度波动较大的数据，如：文章，有的会很短有的会很长
         2. 字符串很少更新的场景，每次更新后都会重算并使用额外存储空间保存长度
         3. 适合保存 多字节字符，如：汉字，特殊字符等。
     * char固定长度的字符串
        1. 最大长度：255
        2. 会自动删除末尾的空格
        3. 检索效率、写效率会比varchar高，以空间换时间
        4. 应用场景：
           1. 存储长度变化不大的数据，如：md5摘要
           2. 存储短字符串、经常更新的字符串
     * BLOB和TEXT类型
        
        MySQL把每个BLOB和TEXT值当作一个独立的对象处理
        两者都是为了存储很大数据而设计的字符串类型，分别采用二进制和字符方式存储
        一般没人用
        
  * datetime和timestamp
    1. datetime
      1. 占用8个字节
      2. 与时区无关，数据库底层时区配置，对datetime无效
      3. 可保存到毫秒
      4. 可保存时间范围大
      5. 不要使用字符串存储日期类型，占用空间大，损失日期类型函数的便捷性 
     
    2. timestamp
      1. 占用4个字节
      2. 时间范围：1970-01-01到2038-01-39
      3. 精确到秒
      4. 采用整形存储
      5. 依赖数据库设置的时区
      6.自动更新timestamp列的值
   
    3. date
      1. 占用的字节数比使用字符串、datetime、int存储要少，使用date类型只需要3个字节
      2. 使用date类型还可以利用日期时间函数进行日期之间的计算
      3. date类用于保存1000-01-01到9999-12-31之间的日期
    
  * 使用枚举代替字符串类型
     
     有时候可以使用枚举类代替常用的字符串类型，mysql存储枚举类型会非常紧凑，会根据列表值的数据压缩到一个或两个字节中，
     mysql在内部会将每个值在列表中的位置保存位整数，并且在表的.frm文件中保存‘数字 字符串’映射关系的查找表
  
    ```sql
      create table enum_test(e enum('fish','apple','dog') not null);
    
      insert into enum_test(e) values('fish'),('dog'),('apple');
    
      select e+0 from enum_test;
    ```
  
  
  * 特殊类型数据    
    
    人们经常使用varchar(15)来存储ip地址，然而，它的本质是32位无符号整数不是字符串，可以使用
     INET_ATON()和IENT_NTOA函数在这两种表示方法之间转换
     
     案例：
     select inet_aton('1.1.1.1'')
     select inet_ntoa(123123123)
        
     
如果int、varchar不指定长度，默认则是11和255  


## 合理使用范式和反范式
### 范式
优点：
范式化的更新通常比反范式要快
当数据较好的范式化后，很少或者没有重复的数据
范式化的数据比较小，可以放在内存中，操作比较快

缺点：
通常需要进行关联

### 反范式
优点：
所有的数据都在同一张表中，可以避免关联
可以设计有效的索引

缺点：
表格内的冗余较多，删除数据时候会造成表有些有用的信息丢失

### 注意
在企业中很好能左到严格意义上的范式或反范式，一般需要混合使用
案例
参见13
## 主键的选择

### 代理主键

    与业务无关的，无意义的数字序列
### 自然主键
   
    事务属性中的自然唯一标识

### 推荐使用代理主键
    它们不与业务耦合，因此更容易维护
    一个大多数表，最好是全部表，通用的键策略能够减少需要编写的源码数量，减少系统
    的总体拥有成本
    
主键生成策略


##字符集的选择
1. 纯拉丁字符能表示的内容，没必要选择Latin1之外的其他字符编码，因为
   这会节省大量的存储空间
2. 如果我们可以确定不需要存方多种语言，就没必要非得使用UTF8或者其他
   UNICODE字符类型，这回造成大量的存储空间浪费。一般中文使用utf8mb4
3. MySQL的数据类型可以精确到字段，所以当我们需要大型数据库中存方多字节
   数据的时候，可以通过对不同表不同字段使用不同的数据类型来较大程度减少
   数据存储量，进而降低IO操作次数并提高缓存命中率


## 存储引擎的选择
默认innodb
可以在配置文件中修改


## 适当的数据冗余
1. 被频繁引用且只能通过join2张（或者更多）大表的方式
   才能得到的独立小字段
2. 这样的场景由于每次join仅仅是为了取得某个小字段的值，join到
   的记录又大，会造成大量不必要的IO,完全可以通过空间换取时间的方式来优化。
   不过，冗余的同时需要确保数据的一致性不会遭到破坏，确保更新的同时冗余字段也被更新。
  



## 适当拆分
当我们的表中存在类似于TEXT或者是很大的VARCHAR类型的大字段的时候，如果我们大部分访问这张表的四hi后都
不需要这个字段，我们就该义无反顾的将器拆分到另外的独立表中，以减少常用数据所占用的存储空间。这样做的一个
明显好处就是每个数据块可以存储的数据条数可以大大增加，既减少物理IO次数，也能大大提高内存张的缓存命中率。



#执行计划
参考13

# 通过索引进行优化

B*树：在非叶子节点中相互之间进行连接

* 索引基本知识
  1. 索引的优点
    1. 大大减少服务器需要扫描的数据量
    2. 帮助服务器避免排序和临时表
    3. 将随机io变成顺序io
  2. 索引的用处
    1. 快速查找匹配where子句执行
    2. 从consideration中消除行，如果可以再多个索引之间进行选择，
       mysql通常会使用找到最少行的索引
    3. 如果表具有多列索引，则优化器可以使用索引的任何最左前缀来查找行
    4. 当有表连接的时候，从其他表检索行数据
    5. 查找特定索引列的min或max值
    6. 如果排序或分组再可用索引的最左前缀上完成的，则对表进行排序和分组
    7. 再某些情况下，可以优化查询以检索值而无需查询数据行
  3. 索引分类
    1. 主键索引
        数据不能为空
    2. 唯一索引
        数据可以为空
    3. 普通索引
    4. 全文索引
    5. 组合索引
  4. 面试技术名词
    1. 回表
    2. 覆盖索引
    3. 最左匹配
    4. 索引下推 
        where name='' and age =''
        前置条件是有name和age的组合索引，再低版本的时候存储引擎先使用name索引查询出来所有的数据，然后再service层进行age的过滤，高版本后，则使用索引下推
        再存储引擎就会过滤掉age，而不需要在传递给service层了。
       谓词下推:
       
  注意：索引合并效率不一定高（高版本才有索引合并）
  5. 索引采用的数据结构
    1. 哈希表
    2. B+树
  6. 索引匹配方式
    1. 全值匹配
       全值匹配指的是和索引中的所有列进行匹配
       explain select * from staffs where name =‘July’
       and age='23' and pos='dev';
       ```sql
       create table staffs {
            id int primary key auto_increment,
            name varchar(24) not null default '' comment '姓名',
            age int not null default 0 comment '年龄',
            pos varchar(20) not null default '' comment '职位',
            add_time timestamp not null default current_timestamp comment '入职时间'
       ) charset utf8 comment ''员工记录表';
       alter table staffs add index idx_nap(name,age pos);
       ```
       下载官网数据库,参见13 sakila数据库说明
       ```
       open node01
       在mysql客户端执行source xxxx.sql

       ```
    2. 匹配最左前缀
       只匹配前面的几列
       explain select * from staffs where name='July' and age='23';
       explain select * from staffs where name='July'
    3. 匹配列前缀
       可以匹配某一列的值的开头部分
       explain select * from staffs where name like 'J%';
       explain select * from staffs where name like '%y';
    4. 匹配范围值
       可以查找某个范围的数据
       explain select * from staffs where name > 'Mary';
    5. 精确匹配某一列并范围匹配另外一列
        可以查询第一列的全部和第二列的部分
        explain select * from staffs where name='July' and age>25;
        反例
        explain select * from staffs where name='July' and pos>25;
        
        会进行优化 把pos和age顺序进行调整
        explain select * from staffs where name='July' and pos=25 and age=25;
        explain select * from staffs where name='July' and pos='dev' and age=25;
    6. 只访问索引的查询
        查询的时候只需要访问索引，不需要访问数据库，本质上就是覆盖索引
        explain select name,age,pos from staffs where name='July' and age=25 and pos='dev';
* 哈希索引
   1. 基于哈希表的实现，只有精确匹配索引所有列的查询才有效
   2. 在mysql中，只有memory的存储引擎显示支持哈希索引
   3. 哈希索引自身只需存储对应的hash值，所以索引的结构十分紧凑，这让哈希索引查找的速度非常快
   4. 哈希索引的限制
      1. 哈希索引只包含哈希值和行指针，而不存储字段值，索引不能使用索引中的值来避免读取行
      2. 哈希索引数据并不是按照索引值顺序有序存储的，所以无法进行排序
      3. 哈希索引不支持部分列匹配查询，哈希索引是使用索引列的全部内容来计算哈希值
      4. 哈希索引支持等值比较查询，也不支持任何范围查询
      5. 访问哈希索引的数据非常快，除非有很多哈希冲突，当出现哈希冲突的时候，存储引擎必须遍历链表中的所有行指针，逐行进行比较，直到找到所有符合条件的行
      6. 哈希冲突比较多的话，维护的代价也会很高
   5. 案例
      当需要存储大量的URL，并且根据URL进行搜索查找，如果使用B+树，存储的内容就会很大
      select id from url where url=""
      也可以利用将url使用CRC32做哈希，可以使用以下方式：
      select id from url where url="" and url_crc = CRC32("")
      此查询性能较高原因是使用体积很小的索引来完成查找
* 组合索引
   1.当包含多个列作为索引，需要注意的是正确的顺序依赖于该索引的查询，同时需要考虑如何更好的满足排序和分组的需要
   2. 案例，建立组合索引a,b,c。观察以下不同sql语句使用索引情况
      语句                     索引是否发挥作用
      where a = 3             是，只使用了a
      where a=3 and b=5        是，使用了a,b
      where a=3 and b=5 and c=4  是，使用了a,b,c
      where b=3 or where c=4    否
      where a=3 and c=4         是，仅使用了a
      where a=3 and b>10 and c=7 是,使用了a,b  #b是范围查询，后边的不管是不是索引列都会忽略
      where a=3 and b like "%xx%" and c=7 使用了a
* 聚簇索引与非聚簇索引
   1. 聚簇索引
      不是单独的索引类型而是一种数据存储方式，指的是数据行跟相邻的键值紧凑的存储在一起
      优点：
         1. 可以把相关数据保存在一起
         2. 数据访问更快，因为索引和数据保存在同一个树种
         3. 使用覆盖索引扫描的查询可以直接使用页节点种的主键值
      缺点：
         1. 聚簇数据最大限制地提高了IO密集型应用的性能，如果数据全部在内存，那么聚簇索引就没有什么优势
         2.插入速度严重依赖于插入顺序，按照主键的顺序插入是最快的方式
         3. 更新聚簇索引列的代价很高，因为会强制将每个被更新的行移动到新的位置
         4. 基于聚簇索引的表在插入新行，或者主键被更新导致需要移动行的时候，可能面临页分裂的问题
         5. 聚簇索引可能导致全表扫描变慢，尤其是行比较稀疏，或者由于页分裂导致数据存储不连续的时候。
       附加：在大量移动数据到mysql种的时候可以把MySQL创建索引的属性关掉，等移动过去后，在打开，效率会高很多
   2. 非聚簇索引
       数据文件跟索引文件分开存放   
* 覆盖索引
    myisam有覆盖索引
   1.基本介绍
      1. 如果一个索引包含所有需要查询的字段的值，我们称为覆盖索引
      2. 不是所有类型的索引都可以称为覆盖索引，覆盖索引必须要存储索引列的值
      3. 不同的存储实现覆盖索引的方式不同，不是所有的引擎都支持覆盖索引，memory不支持覆盖索引
   2.优势
      1.索引条目通常远小于数据行大小，如果只需要读取索引，那么mysql就会极大的较少数据访问量
      2. 因为索引是按照列值顺序存储的，所以对IO密集型的范围查询会比随机从磁盘读取每一行数据的IO要少的多
      3. 一些存储引擎如MyISAM在内存种只存储索引，数据则依赖于操作系统来缓存，因此要访问数据需要一次系统调用，
        这可能会导致严重的性能问题
      4. 由于INNODB的聚簇索引，覆盖索引对INNODB表特别有用
   3. 案例演示
      参见13的覆盖索引.md 
* 优化小细节
  1. 当使用索引列进行查询的时候尽量不要使用表达式，把计算放到业务层而不是数据库层
     select actor_id from actor where actor_id=4;
     select actor_id from actor where actor_id+1=5;
  2. 尽量使用主键查询，而不是其他索引，因此主键查询不会触发回表查询
  3. 使用前缀索引
    参见13 前缀索引实例说明.md
    alter table citydemo add key(city(7))
    基数 count(distinct xx)
    hyperloglog算法
    kylin
  4. 使用索引扫描来排序
     参见13 使用索引扫描来做排序.md
  5. union all，in，or都能够使用索引，但是推荐使用in
     explain select * from actor where actor_id=1
      union all select * from actor where actor_id=2;
     没有去重要求的时候尽量使用union all
     union 有去重的过程
     explain select * from actor where actor_id in (1,2);
     explain select * from actor where actor_id=1 or actor_id=2;
     
     附件：exists
     select * from emp where exists(select deptno from dept where deptno=20 or deptno=30);
     会查询出emp的所有数据，过程时这样的，exists相当于双层for循环，外层select查询出数据后，看内层是否有结果，不管结果，如果有结果就算1条。
     因为内层始终有结果，所以外层遍历的表数据都会保留，就是全部数据了。
     可以这样改造：
     select * from emp e where exists(select deptno from dept d where (deptno=20 or deptno=30) and e.deptno=d.deptno); 
     exists并不能查询内层的字段信息，如果要用内层的字段信息就不能使用exists了
     
     in和exists推荐exists
  6. 范围列可以用到索引
     范围条件是：<、<=、>、>=、between
     范围列可以用到索引，但是范围列后面的列无法用到索引，索引最多用于一个范围列
  7.强制类型转换会全表扫描
    create table user(id int ,name varchar(10),phone varchar(11));
    alter table user add index idx_1(phone);
    
    explain select * from user where phone=1380002341 不会触发索引
    explain select * from user where phone='1380002341' 会触发索引
    
    
     
  8. 更新十分频繁，数据区分度不高的字段上不宜建立索引
     更新会变更B+树，更新频繁的字段建立索引会大大降低数据库性能
     类似于性别这类区分不大的属性，建立索引是没有意义的，不能有效的过滤数据
     一般区分度在80%以上的时候就可以建立索引，区分度可以使用count(distinct(列名))/count(*)来计算
  9. 创建索引的列，不允许为null，可能会得到不符合预期的结果
  10.当需要进行表连接的时候，最好不要超过三张表，因为需要join的字段，数据类型必须一致
   join的实现原理
   a.使用simple nested-loop join 算法，也就是嵌套循环
   !(join的多种连接方式)[D:\IT\demo-code\src\main\java\com\example\demo\lpj\image\join的多种连接方式0.png]
   b.index nested-loop join
   !(join的多种连接方式)[D:\IT\demo-code\src\main\java\com\example\demo\lpj\image\join的多种连接方式1.png]
   c.block nested-loop join
   !(join的多种连接方式)[D:\IT\demo-code\src\main\java\com\example\demo\lpj\image\join的多种连接方式2.png]
   show variables like '%join_buffer%'
   
   A join B 在优化器作用下不一定先读A.有关键字可以指定先读A
   一般是小表join大表最好
  11.如果明确知道只有一条结果返回，limit 1能够提高效率
   limit是用来做什么的，不要说是做分页的，准确的说是限制输出的。
    能使用limit的时候尽量使用limit
  12.单表索引建议控制在5个以内
  13.单索引字段数不允许超过5个
    组合索引
  14.创建索引的时候应该避免以下错误概念
     索引越多越好
     过早优化，在不了解系统的情况下进行优化
* 索引监控
  show status like 'Handler_read%';
  参数解释
      Handler_read_first:读取索引第一个条目的次数
      Handler_read_key:通过index获取数据次数
      Handler_read_last:读取索引最后一个条数的次数
      Handler_read_next:通过索引读取下一条数据的次数
      Handler_read_prev:通过索引读取上一条数据的次数
      Handler_read_rnd:从固定位置读取数据的次数
      Handler_read_rnd_next:从数据节点读取下一条数据的次数
* 简单案例
  索引优化分析案例 参见13
# 查询优化

   在编写快速的查询之前，需要清楚一点，真正重要的是响应时间，而且要知道在整个SQL语句的执行
   过程中每个步骤都花费了多长时间，要知道哪些步骤是拖垮执行效率的关键步骤，想要做到这点必须要知道查询的生命周期，然后进行优化，
   不同的应用场景有不同的优化方式，不要一概而论，具体情况具体分析。
* 查询慢的原因
   网络
      网络IO
   CPU
   IO
   上下文切换
   系统调用
   生成统计信息
   锁等待时间
* 优化数据访问
   1. 查询性能低下的主要原因是访问的数据太多，某些查询不可避免的需要筛选
   大量的数据，我们可以通过减少访问数据量的方式进行优化
     1. 确认应用程序是否在检索大量超过需要的数据
     2. 确认mysql服务器层是否在分析大量超过需要的数据行
   2. 是否想数据库请求了不需要的数据
     1. 查询不需要的记录
        我们常常会误以为mysql会只返回需要的数据，实际上mysql却是先返回全部
        结果再进行计算，再日常的开发习惯中，经常是先用select语句查询大量的结果，
        然后获取前面的N行后关闭结果集
        优化方式是再查询后面添加limit
     2. 多表关联时返回全部列
        select * from actor inner join film_actor using(actor_id) inner join film
        using(film_id) where file.title='Academy Dinosaur';
        
        select actor.* from actor....
     3. 总是取出全部列
        在公司的企业需求中，禁止使用select * ，虽然这种方式能够简化开发，但是会影响查询的性能，所以尽量不要使用
     4. 重复查询相同的数据
        如果需要不断的重复执行相同的查询，且每次返回完全相同的数据，因此，基于这样的应用场景，我们可以将这部分数据缓存起来，这样的话能够提高查询效率
* 执行过程的优化
    1.查询缓存
      在解析一个查询语句之前，如果查询缓存时开开的，那么mysql会优先检查这个查询是否命中查询缓存中的数据，如果查询恰好命中了查询缓存，那么会在返回结果之前
      会查询用户权限，如果权限没有问题，那么mysql会跳过所有的阶段，就直接从缓存中拿到结果并返回给客户端
    2.查询优化处理
       mysql查询完缓存之后会经过以下几个步骤:解析SQL、预处理、优化SQL执行计划，这几个步骤出现任何的错误，都可能会终止查询
       1. 语法解析器和预处理
         mysql通过关键字将sql语句进行解析，并生成一颗解析树，mysql解析树将使用mysql语法规则验证和解析查询、例如验证使用了错误的关键字或者顺序是否正确等，
         预处理器会进一步检查解析树是否合法，例如表名和列名是否存在，是否有歧义，还有验证权限等等
       2. 查询优化器
          当语法树没有问题之后，相应的要由优化器将其转成执行计划，一条查询语句可以使用非常多的执行方式，最后都可以得到对应的结果，但是不同的执行方式带来效率是不同的，
          优化器的最主要目的就是要选择最优的执行计划
          mysql使用的是基于成本的优化器，在优化的时候会尝试预测一个查询使用某种查询计划时候的成本，并选择其中成本最小的一个。
          1. select count(*) from file_actor;
             show status like 'last_query_cost';
             可以看到这条查询语句大概需要做1104个数据页才能找到对应的数据，这是经过一些列的统计信息计算来的。
             每个表或索引的页面个数
             索引的基数
             索引和数据行的长度
             索引的分布情况
          2. 在很多情况下mysql会选择错误的执行计划，原因如下：
             统计信息不准确
                InnoDB因为其mvcc的架构，并不能维护一个数据表的行数据的精确统计信息
             执行计划的成本估算不等于实际执行的成本
                有时候某个执行计划虽然需要读取更多的页面，但是他的成本却更小，因为如果这些页面都是顺序读或者这些页面都已经在内存中的话，那么它的访问成本将很小，
                mysql层面并不知道哪些页面在内存中，哪些在磁盘，所以查询实际执行过程中到底需要多少次IO是无法得知的
             mysql的最优可能跟你想的不一样
             mysql不考虑其他并发执行的查询
             mysql不会考虑不受其控制的操作成本
                执行存储过程或者用户自定义函数的成本
          3. 优化器的优化策略
             静态优化
               直接对解析树进行分析，并完成优化
             动态优化
               动态优化于查询的上下文有关，也可能跟取值、索引对应的行数有关
             mysql对查询的静态优化只需要一次，但对动态优化在每次执行时候都需要重新评估
          4. 优化器的优化类型
             重新定义关联表的顺序
                数据表的关联并不总是按照在查询中指定的顺序进行，决定关联顺序时优化器很重要的功能
             将外连接转换成内连接，内连接的效率要高于外连接
             使用等价变换规则，mysql可以使用一些等价变化来简化并规划表达式
             优化count()、min()、max()
               索引和列是否可以为空通常可以帮助mysql优化这类表达式：例如，要找到某一列的最小值，只需要查询索引
               的最左端的记录即可，不需要全文扫描比较
             预估并转化为常数表达式，当mysql检测到一个表达式可以转化为常数的时候，就会一直把改表达式作为常数进行处理
             索引覆盖扫描，当索引中的列包含所有查询中需要使用的列的时候，可以使用覆盖索引
             子查询优化
             等值传播
          5. 关联查询
          6. 排序优化 
    3. 关联查询
       join实现方式原理
       案例演示
       参见13 
    4. 排序优化
       两次传输排序
       单次传输排序
       当需要排序的列的总大小加上orderby的列大小超过max_lenght_for_sort_data定义的字节，mysql会选择双次排序，反之使用单次排序，
       当然，用户可以设置此参数的值来选择排序的方式。
* 优化特定类型的查询
  mysql开窗函数
  
  //效率慢
  select *  from rental limit 1000000,5;
  //效率高，可替换
  select * from rental a join (select rental_id from rental limit 1000000,5) b on a.rental_id=b.rental_id;
   
   行转列
   !(行转列)[D:\IT\demo-code\src\main\java\com\example\demo\lpj\image\行转列的方式.png]

# 分区表
参见13


ad_hoc 即席查询