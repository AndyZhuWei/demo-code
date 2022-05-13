#微服务拆分原则

拆解分层的方法论
按流程拆分
按服务拆分（类似SOA时代中按服务拆分，这个服务偏业务）
按功能拆分（微服务时代，按功能点，把功能点变成一个服务） 这个功能可以很细，但是要考量拆分成本，是否有必要把功能拆分的很细嘛

AKF方法论：
x轴解决可用性的问题
    数据依然是全量的。这个轴上可以实现主备、主从+读写分离，从而可以做一些性能上的优化
y轴是对业务的拆分
     当并发量访问很大，超过了CPU算力，超过了内存或磁盘的存储能力，超过了它响应时间合适的程度。
   这个时候就需要拆分业务。
   拆分后有这样的好处：高内聚、性能提高、网关
Z轴 倾斜出现
   sharding 通过代理来做分治

#前后端分离

C/S:（client/server）
前后端的概念？不清晰

B/S:（browser/server）
前后端的概念？清晰

C/P:（consumer/producer）  C/S-ISO app
前后端的概念？非常清晰了 API接口

VIP 通过此ip进行渲染主页
AIP 通过此API的IP地址进行json数据的获取，前端已经渲染了主页面（可能是ISO app通过ajax获取数据）

C/B都会考虑以下的技术
##动静分离技术
一般主页index.html不会部署到CDN。
访问主页的请求进来一般要进入数据中心，通过lvs lb（四层 面向数据包）,然后转发给nginx,主页一般就在nginx（七层 URI 三次握手 反向代码+LB）中的静态数据里
主页是要和域名相关。
这个主页中包含好多标签，包括图片什么的，这个图片地址一般就是在其他域名下，这些静态资源不会做倒排索引，爬虫优化，就会走CDN

动静分离在这里就体现了，静就是nginx和cdn中的静态资源，动就是要nginx后边的api网关后的动态资源





##负载均衡技术
LVS
四层 面向数据包

Nginx
七层 URI 三次握手 反向代码+LB


##多级缓存技术
在nginx中将不太变化的数据（页面中的数据，用户的行为数据）缓存在redis，


##防穿透技术
基于缓存的数据可以做防穿透


##限流技术


##熔断技术
熔断 有这么几个状态 开 关 半开/半关




##降级技术，快速失败



##灰度发布技术

# 服务无状态 分布式问题
为什么有这么个概念？
就比如sessionId分布在多个节点上的时候，通过lb时改如何负载就是一个问题。
所以一般服务我们要设计成无状态的。把数据抽取到一个公共的地方



状态即数据
程序=算法+数据结构

数据在C端
数据在P端
数据在S端
数据在D端

算法在C端
算法在P端
算法在S端

数据在R时
数据在C时
数据在D时
数据在U时

基于成本的tradeoff


一致性
幂等性
事务性

可用性
最终一致性

并发下分布式锁
串行化
同步转异步
状态机流转
原子性乐观锁CAS

服务是无状态的
业务一定是有状态的
应用一定是用状态的























#服务无状态