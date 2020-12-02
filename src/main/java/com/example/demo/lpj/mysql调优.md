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
##使用show profile查询，可以指定具体的type
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

##使用performance schema来更加容易的监控mysql
show variables like 'performance_schema';
默认是打开的，不可以直接修改，是只读的，修改需要改配置文件
set performance_schema=off
具体用法参见13文档







#schema与数据类型优化



#执行计划



#通过索引进行优化