#一
start.spring.io提供了一个生成脚手架的页面
脚手架就是一个空项目的

starter 可以理解为一个启动器，里面封装了相应功能所有的jar和配置，并且不需要担心jar包冲突

spring5之后只支持jdk1.8

spring boot热部署的方式：
1.添加devtools依赖 修改后会自动重启服务。所以其实不是真正的热部署
2.Jrebel 下载好破解版的后，在vm arguments中加入-agentpath:xx/xx/xx/jrebel64.dll即可或其他激活方法

## thymealf
@ResponseBody:用这个标记的方法，返回的对象是不经过渲染的 直接返回json数据，否则就会经过渲染引擎,
默认情况下spring boot使用的是thymealf模板引擎
返回的字符串就是页面的名称，后缀名是html,在src\resources\templates目录下
ModelMap给前端模板引擎传值,于Model差不多就是多一些方法

** 配置参数 **
server.port=80
server.servlet.context-path=/test

 <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
 </dependency>


#二
@RequestParam 不写这个也可以，写上后可以设置一些参数的默认值
@ModelAttribute
spring boot默认的模板引擎就是thymleaf，如果使用jsp，则需要进行配置


##JPA
引包jpa的starter
配置数据源
** 配置参数 **
    spring.datasource.url=
    spring.datasource.username=
    spring.datasource.password=
定义实体类，比如@Entity @Table @Id @Column等
写xxxJpa继承JpaResponsirty<User>
引入相应的jpa就可以使用了

显示sql
spring.jpa.show-sql=true

自定义查询
@Query("hql")


##JPS
引入相关包
<dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>jstl</artifactId>
        </dependency>

        <dependency>
            <groupId>org.apache.tomcat.embed</groupId>
            <artifactId>tomcat-embed-jasper</artifactId>
            <version>provided</version>
        </dependency>
        
修改配置文件
** 配置参数 **

spring.mvc.view.prefix=/WEB-INF/jsp   #和static平级的
spring.mvc.view.suffix=.jsp

注意：需要去掉thymeleaf模板引擎的依赖包

# 三
前后端分离后，已经不需要后端做渲染了，所以thymeleaf已经用的不多了。
前端的分离：
1.后端提供json，前端渲染。比较老的方式
2.服务器端渲染，和thymeleaf、jsp很像，但是还是有些不一样

###Bootstrap
在url上使用@标签可以帮我们自动加上contextpath
@{xxxx}

commons-lang3 jar包中的方法ToStringBuilder.reflectionToString(object)

加入新的依赖后，项目是不能热部署，要重启

## mybatis
### 传统项目里面怎么用
1.首先需要一个mybatis的配置文件，一般为mybaitsConfig.xml,这个里面配置数据库信息,引入orm映射文件
2.还有orm映射的文件等
3.实例化sqlSession等对象开始查询

### springboot中怎么用
1.配置数据源
2.配置参数
mybatis:
  configuration:
    map-underscore-to-camel-case: true
  mapper-locations: classpath:mapping/**/*.xml
3.编写相应的mapper.xml文件
```text
其中namespace对应的就是我们查询时用到的接口，对应上后，这个接口就不需要实例化了
<mapper namespace="com.healthlink.shanhumanager.core.mapper.HealthAssistantWxtipMapper" >
....
</mapper>
```
4.需要注意的时在对应的接口上要加@Mapper
5.显示sql
mybatis:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
或者：
loggin.level.xxxx.mapper=debug

自动代码生成工具
mybatis-generator,基于配置文件的
或者mybatis-generator-gui 

分页查询插件：pagehelper
依赖
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper-spring-boot-starter</artifactId>
    <version>1.2.5</version>
</dependency>

使用：
PageHelper.startPage(pageNo, pageSize);//基于AOP
PageInfo


敏捷开发时设计是不完整的，小步迭代开发
瀑布流开发，就是开始就把所有功能设计完整后才开放


### 拦截器
@WebFilter 可以用于权限拦截

配置war包，需要做一些配置

映射本地静态资源
spring.resources.static-locations=xxx,xx

FASTDFS

RBAC

properties的优先级大于yml

account账户表和user表要区分开


###Odata




allowMultiQueries=true  #mysql链接的配置参数
这样配置后可以在一次执行中调用中执行多条sql语句,类似以下情况
```sql
<insert id="xxx">
delte from xxx;
insert into xxx;

</insert>
```


Icheck前端插件
#Mybatis plus
代码生成器
mybatis plus CRUD



###SSL
数据是由服务器的公钥保证安全的
服务器的公钥是由CA保证安全的














