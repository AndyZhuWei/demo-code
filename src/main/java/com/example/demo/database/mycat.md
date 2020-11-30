Mycat 是一个开源的分布式数据库系统，但是由于真正的数据库需要存储引擎，而 Mycat 并没有存 储引擎，所以并不是完全意义的分布式数据库系统
mycat是一个数据库中间件，也可以理解为是数据库代理。在架构体系中是位于数据库和应用层之间的一个组件，并且对于应用层是透明的，即数据库 感受不到mycat的存在，认为是直接连接的mysql数据库（实际上是连接的mycat,mycat实现了mysql的原生协议）
mycat的三大功能：分表、读写分离、主从切换

1.分表
对于数据量很大的表（千万级以上），mysql性能会有很大下降，因此尽量控制在每张表的大小在百万级别。对于数据量很大的一张表，可以考虑将这 些记录按照一定的规则放到不同的数据库里面。这样每个数据库的数据量不是太大，性能也不会有太大损失。
mycat分表的实现：首先在mycat的scheme.xml中配置逻辑表，并且在配置中说明此表在哪几个物理库上。此逻辑表的名字与真实数据库中的名字一致！然后需要配置分片规则，即按照什么逻辑分库！

2.读写分离
经过统计发现，对数据库的大量操作是读操作，一般占到所有操作70%以上。所以做读写分离还是很有必要的，如果不做读写分离，那么从库也是一种很大的浪费。

3.MyCat 基本元素

   1.逻辑库，mycat中存在，对应用来说相当于mysql数据库，后端可能对应了多个物理数据库，逻辑库中不保存数据
   2.逻辑表，逻辑库中的表，对应用来说相当于mysql的数据表，后端可能对应多个物理数据库中的表，也不保存数据
逻辑表分类
     1.分片表，进行了水平切分的表，具有相同表结构但存储在不同数据库中的表，所有分片表的集合才是一张完整的表
     2.非分片表，垂直切分的表，一个数据库中就保存了一张完整的表
     3.全局表，所有分片数据库中都存在的表，如字典表，数量少，由mycat来进行维护更新
     4.ER关系表，mycat独有，子表依赖父表，保证在同一个数据库中

MyCat 实现读写分离
部署环境
  MyCAT 是使用 JAVA 语言进行编写开发，使用前需要先安装 JAVA 运行环境(JRE),由于 MyCAT 中使用了 JDK7 中的一些特性，所以要求必须在 JDK7 以上的版本上运行。
1.下载jdk


shell> wget --no-cookies \--no-check-certificate \--header \"Cookie: oraclelicense=accept-securebackup-cookie" \http://download.oracle.com/otn-pub/java/jdk/8u181-\b13/96a7b8442fe848ef90c96a2fad6ed6d1/jdk-8u181-linux-\x64.tar.gz// --no-check-certificate 表示不校验SSL证书，因为中间的两个302会访问https，会涉及到证书的问题，不校验能快一点，影响不大.

2.解压文件


shell> tar -xf jdk-8u181-linux-x64.tar.gz  -C  /usr/local/shell> ln -s /usr/local/jdk1.8.0_181/ /usr/local/java

3.配置环境变量


shell> vi /etc/profile.d/java.shexportJAVA_HOME=/usr/local/javaexportPATH=$JAVA_HOME/bin:$PATHexportCLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar

注意 CLASSPATH 中有个点.不要省略

4.使环境变量生效


// 立刻在当前的 shell 会话中生效shell>source/etc/profile

全部生效需要重启机器

部署MyCat
下载


shell> wget http://dl.mycat.io/1.6-RELEASE/Mycat-server-1.6-RELEASE-20161028204710-linux.tar.gz

解压


shell> tar -xf Mycat-server-1.6-RELEASE-20161028204710-linux.tar.gz -C /usr/local/shell> ls /usr/local/mycat/bin  catlet  conf  lib  logs  version.txt

配置MyCat
认识配置文件
MyCAT 目前主要通过配置文件的方式来定义逻辑库和相关配置:
1./usr/local/mycat/conf/server.xml

定义用户以及系统相关变量，如端口等。其中用户信息是前端应用程序连接 mycat 的用户信息。

2./usr/local/mycat/conf/schema.xml

定义逻辑库，表、分片节点等内容。

3./usr/local/mycat/conf/rule.xml

中定义分片规则。

配置 server.xml
以下为代码片段

下面的用户和密码是应用程序连接到 MyCat 使用的，可以自定义配置

而其中的schemas 配置项所对应的值是逻辑数据库的名字，也可以自定义，但是这个名字需要和后面 schema.xml 文件中配置的一致。

shell> vim server.xml
<user name="mycatdb">
    <property name="password">123456</property>
    <property name="schemas">schema_shark_db</property>

    <!-- 表级 DML 权限设置 -->
    <!--
    <privileges check="false">
        <schema name="TESTDB" dml="0110" >
            <table name="tb01" dml="0000"></table>
            <table name="tb02" dml="1111"></table>
        </schema>
    </privileges>
    -->
</user>

<!--下面是另一个用户，并且设置的访问 TESTED 逻辑数据库的权限是 只读
<user name="user">
        <property name="password">user</property>
        <property name="schemas">TESTDB</property>
        <property name="readOnly">true</property>
</user>
-->

==上面的配置中，假如配置了用户访问的逻辑库，那么必须在 schema.xml 文件中也配置这个逻辑库，否则报错，启动 mycat 失败==

配置 schema.xml
以下是配置文件中的每个部分的配置块儿

逻辑库和分表设置

<schema name="schema_shark_db"   // 逻辑库名称
        checkSQLschema="false"   // 不检查
        sqlMaxLimit="100"        // 最大连接数
        dataNode='dn1'>          // 数据节点名称
<!--这里定义的是分表的信息-->
</schema>

数据节点

<dataNode name="dn1"              // 此数据节点的名称
          dataHost="localhost1"   // 主机组
          database="shark_db">    // 真实的数据库名称
</dataNode>

主机组

<dataHost name="localhost1"
            maxCon="1000" minCon="10"   // 连接
            balance="0"                 // 负载均衡
            writeType="0"               // 写模式配置
            dbType="mysql" dbDriver="native" // 数据库配置
            switchType="1" slaveThreshold="100">
    <!--这里可以配置关于这个主机组的成员信息，和针对这些主机的健康检查语句-->
</dataHost>

image
健康检查

<heartbeat>select user()</heartbeat>

读写配置

<writeHost host="hostM1" url="172.16.153.10:3306"
               user="root" password="123">
      <!-- can have multi read hosts -->
      <readHost host="hostS2" url="172.16.153.11:3306"
                user="root" password="123" /></writeHost>

以下是组合为完整的配置文件，适用于一主一从的架构

<!DOCTYPE mycat:schema SYSTEM "schema.dtd">
<mycat:schema xmlns:mycat="http://io.mycat/">

  <schema name="schema_shark_db" 
          checkSQLschema="false"
          sqlMaxLimit="100" 
          dataNode='dn1'>
    <!--这里定义的是分库分表的信息-->
  </schema>

  <!--下面是配置读写分离的信息-->
  <dataNode name="dn1"
            dataHost="localhost1" database="shark_db">
  </dataNode>

  <dataHost name="localhost1"
            maxCon="1000" minCon="10"
            balance="0" writeType="0"
            dbType="mysql" dbDriver="native"
            switchType="1" slaveThreshold="100">

    <heartbeat>select user()</heartbeat>
    <!-- can have multi write hosts -->
    <writeHost host="hostM1" url="172.16.153.10:3306"
               user="root" password="123">
      <!-- can have multi read hosts -->
      <readHost host="hostS2" url="172.16.153.11:3306"
                user="root" password="123" />
    </writeHost>
  </dataHost>
</mycat:schema>

配置 log4j2.xml
<!--设置日志级别为 debug , 默认是 info-->
<asyncRoot level="debug" includeLocation="true">

启动 mycat
shell> /usr/local/mycat/bin/mycat  start

支持一下参数
start | restart |stop | status

在真实的 master 数据库上给用户授权
mysql> grant all on shark_db.* to root@'%' identified by '123';
mysql> flush privileges;

假如你通过远程连接登录到 mysql 后，给用户进行授权操作，提示

mysql> grant all on *.* to root@'172.16.153.%' identified by '123';
ERROR 1045 (28000): Access denied for user ...略...

请检查Grant_priv 的值是否是 N

mysql> select * from mysql.user where user='root' \G
              ... 略 ...
                  Host: mysql-master1
                  User: root
            Grant_priv: N
            ... 略 ...

解决办法

尝试通过本地等录到 mysql 后，再进行授权操作

shell> mysql -uroot -p'123' -hlocalhost
mysql> grant all on *.* to root@'172.16.153.%' identified by '123';
Query OK, 0 rows affected, 1 warning (0.01 sec)

mysql> flush privileges;
Query OK, 0 rows affected (0.01 sec)

测试
在 mycat 的机器上测试用户权限有效性

测试是否能正常登录上 主服务器

shell> mysql -uroot -p'123' -h172.16.153.10

继续测试是否能登录上从服务器

shell> mysql -uroot -p'123' -h172.16.153.11

通过客户端进行测试是否能登录到 mycat 上

172.16.153.162 是 mycat 的主机地址

注意端口号是 8066

shell> mysql -umycatdb -p123456 -P8066 -h172.16.153.162
mysql> show databases;
+-----------------+
| DATABASE        |
+-----------------+
| schema_shark_db |
+-----------------+
1 row in set (0.00 sec)

继续测试读写分离策略

使用 mysql 客户端工具使用 mycat 的账户和密码登录 mycat ,
之后执行 select 语句。

之后查询 mycat 主机上 mycat 安装目录下的 logs/mycat.log 日志。

在日志重搜索查询的语句或者查询 从库的 ip 地址，应该能搜索到

报错
ERROR | wrapper | 2019/06/23 22:52:15 | Startup failed: Timed out waiting for a signal from the JVM.
ERROR | wrapper | 2019/06/23 22:52:15 | JVM did not exit on request, terminated

conf/wrapper.conf


wrapper.startup.timeout=300 //超時時間300秒 
wrapper.ping.timeout=120
