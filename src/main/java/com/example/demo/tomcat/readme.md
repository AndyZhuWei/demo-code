#Tomcat架构设计与底层源码分析

##1 Tomcat与Servlet的关系
Servlet容器
servlet = server + applet == 服务端的应用程序

###Servlet规范
Servelt --->   Servlet API----->规范---->Servlet,ServletRequest,ServletResponse
这个规范的提出就是为了方便我们java程序员进行网络应用程序的开发。其中Servlet,ServletRequest,ServletResponse这些都是接口，
定义了http请求相关的信息和方法，程序员就可以通过这些定义好的接口进行开发，但是这些接口的实现类是什么呢？其实就是一些应用服务器（tomcat,jetty,weblogic）

###Socket
Socket接收的浏览器中的请求时会得到一些http协议的一些数据信息，如果程序员基于Socket的得到的这些信息进行网络应用编程将会很麻烦，这个麻烦就会被
Servlet的实现者进行解决。我们基于Servlet编程时不用考虑这些网络协议解析的过程。

ServletRequest

socket --->解析 ---> TomcatServletRequest --->...--->最终传递给我们相应Servlet时的入参HttpServletRequest其实就是RequestFacade(这个
类其实就是tomcat里实现类，如果是其他容器相应的就可能是其他的类)


Request-命令

Applet:客户端应用程序

##2 Socket底层原理解析


java api ---> jdk ---> 系统方法（例如window系统winsock2.h）

HTTP协议是Tomcat实现的
TCP协议是Socket那一层实现的

##3 Tomcat底层架构分析

<Connector connectionTimeout="20000" port="8088" protocol="HTTP/1.1" redirectPort="8443"/>
 
 
Tomcat从Socket中读取数据是设计了IO.Tomcat中有以下几种IO
1.Http11Protocol  （BIO实现）
2.Http11NioProtocol（NIO实现）
3.HttpAprProtocol（其实是从Apache Web Server项目发展而来，是用C语言写的。他们也发现BIO比较慢，所以才发展这个，现在用NIO,用这个比较少了）
以上三种都是Http协议但是他们使用的IO是不同的

Tomcat在启动的时候会读取配置文件，初始化Connector连接器
如果我们配置如下，则会使用NIO实现的连接器
<Connector connectionTimeout="20000" port="8088" protocol="org.apache.coyote.http11.Http11NioProtocol" redirectPort="8443"/>
在Tomcat7中默认HTTP/1.1对应的是BIO但是到Tomcat8时HTTP/1.1对应的就是NIO的实现了

在Connector中取数据的有一个组件是EndPoint
BIO实现的协议中使用的这个组件是JioEndPoint
NIO实现的协议中使用的这个组件是NioEndPoint

Tomcat支持虚拟主机





















