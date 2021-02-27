#Eureka优化
@EnableEurekaServer 注解里面会new一个内部类Marker的对象用于做标记
当classpath中有上边做的标记时类EurekaServerAutoConfiguration配置类才起作用，
在这个类中还导入了EurekaServerInitializerConfiguration

##EurekaServerInitializerConfiguration



在这个类中有剔除过期服务实例的时间间隔秒数
AbstractInstanceRegistry#postInit方法 其中这个值getEvictionIntervalTimerInMs（）控制剔除过期服务的间隔时间
这个逻辑是用Timer做的。Timer不推荐使用，因为


不同数量服务的自我保护
renewal-percent-threshold: 0.85
快速下线
eviction-interval-timer-in-ms: 1000
#默认为true,设置为false可以更快的拉取服务
use-read-only-response-cache: false


##eureka的三级缓存
https://www.cnblogs.com/shihaiming/p/11590748.html

一个服务向eureka注册时除了向register注册外还让readWriteCacheMap失效(取数据的时候会加入信息进来)
readOnlyCacheMap和readWriteCacheMap中的数据30秒同步一次，不是强一致性的。
use-read-only-response-cache默认为true,如果为false则直接从readWriteCacheMap中取得，否则
时从readOnlyCacheMap取，取不到在到readWriteCacheMap中取

GET http://localhost:7900/eureka/app/1 取1的服务


# SpringCloud Eureka自我保护机制
https://www.cnblogs.com/xishuai/p/spring-cloud-eureka-safe.html


Eureka Server 在运行期间会去统计心跳失败比例在 15 分钟之内是否低于 85%，如果低于 85%，Eureka Server 会将这些实例保护起来，
让这些实例不会过期，但是在保护期内如果服务刚好这个服务提供者非正常下线了，
此时服务消费者就会拿到一个无效的服务实例，此时会调用失败，对于这个问题需要服务消费者端要有一些容错机制，如重试，断路器等。

## Renews threshold
Eureka Server:期望每分钟收到客户端实例续约的总数。
计算方式
```java
this.expectedNumberOfRenewsPerMin = count * 2;
this.numberOfRenewsPerMinThreshold = (int) (this.expectedNumberOfRenewsPerMin * serverConfig.getRenewalPercentThreshold());
```
其中count表示注册的微服务的数量，乘以2是因为默认每30秒发一次心动。
serverConfig.getRenewalPercentThreshold()默认是0.85 可以通过eureka.server.renewal-percent-threshold配置

##Renews (last min)
Eureka Server：最后 1 分钟收到客户端实例续约的总数。
计算方式 count * 2


自我保护模式被激活的条件是：在 1 分钟后，Renews (last min) < Renews threshold



生产环境中重启服务时，先停服务，在手动触发下线，否则可能会下线失败，因为可能会自动续约上

Eureka区域设置