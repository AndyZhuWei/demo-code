   ID是数据的唯一标识，传统的做法就是利用UUID和数据库的自增ID,在互联网企业中，大部分公司使用的都是Mysql,并且因为需要事务支持，所以通常会使用Innodb
存储引擎，UUID太长以及无序，所以并不适合在Innodb中来作为主键，自增ID比较合适，但是随着公司的业务发展，数据量将越来越大，需要对数据进行分表，而分表后，
每个表中的数据都会按自己的节奏进行自增，很有可能出现ID冲突，这时就需要一个单独的机制来负责生成唯一ID,生成出来的ID也可以叫做分布式ID,或全局ID.
# 数据库自增ID策略
CREATE TABLE SEQID.SEQUENCE_ID (
     id bigint(20) unsigned NOT NULL auto_increment,
     stub char(10) NOT NULL  default '',
     PRIMARY KEY (id),
     UNIQUE KEY stub(stub)
) ENGINE=MyISAM;

begin;
replace into SEQUENCE_ID(stub) VALUES('anyword');--先查询是否有anyword,如果有则删除进行插入，如果没有则直接进行插入
select last_insert_id();
commit;

其他需要分布式ID的系统直接连接此库进行以上语句就会得到一个唯一的ID.

不高可用，需要解决数据库高可用（搭建集群主从、主主）
主从：从节点的数据会有延迟。所以ID可能会有冲突
主主：此时数据库自增ID主键要用到步长来生成,来达到每个数据库生成的ID不冲突
set @@auto_increment_offset=1;
set @@auto_increment_increment=3;

如果集群中需要在加入数据库机器，步长需要进行修改。但是之前的已经运行的实例步长不好修改


# 号段模式
以上获取id需要每次请求数据库，我们可以进行号段模式批量获取id
CREATE TABLE id_generator (
  id int(10) NOT NULL,
  current_max_id bigint(20) NOT NULL COMMENT '当前最大id',
  increment_step int(10) NOT NULL COMMENT '号段的长度',
  PRIMARY KEY('id')
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
update id_generator set current_max_id=#{newMaxId},version=version+1 where version=${version}
newMaxId是DistributIdService中根据oldMaxId+步长计算出来的，只要上面的update更新成功了就表示号段获取成功了

# 滴滴TinyId生成策略
这个策略就用到了号段模式，详细代码参见
git@github.com:AndyZhuWei/tinyid.git

# 雪花算法
只要能让负责生成分布式ID的每台机器每毫秒内生成不一样的ID就行了。

snowflake是twitter开源的分布式ID生成算法，是一种算法，所以它们和上面的三种生成分布式ID机制不太一样，它不依赖数据库
核心思想是：分布式ID固定是一个long型的数字，一个long型占8个字节，也就是64个bit，原始snowflake算法中对bit的分配如下：

  第一个bit位是标识部分，在java中由于long的最高位是符号位，正数是0，负数是1，一般生成ID为正数，所以固定为0
  往后41位表示时间戳，就是毫秒及的时间，一般实现上不会存储当前的时间戳，而是时间戳的差值（当前时间减去固定的开始时间），这样可以使产生的ID从更小值开始；
41位的时间戳可以使用69年(1L<<41)/(1000L*60*60*24*365)=69年
  往后10位表示机器ID，这里比较灵活，比如，可以使用5位作为数据中心机房标识，后5位作为单机房机器标识，可以部署1024个节点
  最后12位表示序列号，支持统同一毫秒内同一个节点可以生成4096个ID
这样我每台机器在每毫秒内可以生成2的12次方不同的id,也就是4096个不同的id,如果觉得4096不够，我们可以缩小机器ID的位数，扩大序列号的位数
根据这个算法的逻辑，只需要将这个算法用Java语言实现出来，封装为一个工具方法，那么各个业务应用可以直接使用该工具方法来获取分布式ID,只需
保证每个业务应用有自己的工作机器id即可，而不需要单独去搭建一个获取分布式ID的应用。
snowflake算法的实现起来并不难，提供一个github上用Java实现的：
https://github.com/beyondfengyu/SnowFlake
  在大厂里，其实并没有直接使用snowflake，而是进行了改造，因为snowflake算法中最难实践的就是工作机器id，原始snowflake算法需要人工去为每台机器去
指定一个机器id，并配置在某个地方从而让snowflake从此处获取机器id。
  但是在大厂里，机器是很多的，人力成本太大且容易出错，所以大厂对snowflake进行了改造。
时钟回拨后生成id时就会抛错

#uid-generator
详情git@github.com:AndyZhuWei/uid-generator.git
不建议用在github上没有更新

#美团Leaf生成策略（最好）
及提供了号段模式也提供了雪花模式
git@github.com:AndyZhuWei/Leaf.git
在雪花模式下利用zk的有序节点生成机器id
















