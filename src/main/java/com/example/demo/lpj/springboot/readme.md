1.概念介绍，重点区分与微服务的区别
2.看文章，介绍微服务的
cnblogs.com/liuning8023/p/4493156.html

3.自定义注解

中台四杰与阿里

配置文件使用了yaml后缀

配置文件中如果有多个数据形式的值，使用了-
可以使用@Value或者@ConfigurationProperties

4.在JDK中有4个元注解
@Target
@Retention
@Doocumented
@Inherited


在JDBC代码中
```java
 // 加载 Mysql 驱动
        Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        // 数据库连接地址
        String dbConnStr = "jdbc:mysql://localhost:3306/ormtest?user=root&password=root";
        // 创建数据库连接
        Connection conn = DriverManager.getConnection(dbConnStr);
```
DriverManager怎么知道第一步Class.forName("com.mysql.cj.jdbc.Driver").newInstance();实例化出的对象呢
其实是通过SPI机制
具体参见  #Java SPI机制详解


gitbook

spring boot 对国际化的支持


引入第三方配置属性时一般都会有对应的Properties类来承载对应的属性值

加入druid的监控

设计模式推荐书籍：设计模式之禅

getSpringFactoriesInstances

setInitializers((Collection) getSpringFactoriesInstances(ApplicationContextInitializer.class));
setListeners((Collection) getSpringFactoriesInstances(ApplicationListener.class));
getSpringFactoriesInstances(SpringApplicationRunListener.class, types, this, args),


EventPublishingRunListener    
ApplicationStartingEvent

SimpleApplicationEventMulticaster

"ServletInitParams"
"ServletContextInitParams"

		