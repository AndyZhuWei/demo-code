#API接口设计

## 如何保证API接口数据安全
前后端分离的开发方式，我们以接口为标准来进行推动，定义好接口，各自开发自己的功能，最后进行联调整合。无论是开发原生的APP还是webapp还是PC端的软件,只要是前后端分离的模式，就避免不了调用后端提供的接口来进行业务交互。

网页或者app，只要抓下包就可以清楚的知道这个请求获取到的数据，也可以伪造请求去获取或攻击服务器；也对爬虫工程师来说是一种福音，要抓你的数据简直轻而易举。那我们怎么去解决这些问题呢？

接口签名

我们先考虑一下接口数据被伪造，以及接口被重复调用的问题，要解决这个问题我们就要用到接口签名的方案，

签名流程

签名规则

1、线下分配appid和appsecret，针对不同的调用方分配不同的appid和appsecret

2、加入timestamp（时间戳），5分钟内数据有效

3、加入临时流水号 nonce（防止重复提交），至少为10位。针对查询接口，流水号只用于日志落地，便于后期日志核查。针对办理类接口需校验流水号在有效期内的唯一性，以避免重复请求。

4、加入签名字段signature，所有数据的签名信息。

以上字段放在请求头中。


签名的生成

签名signature字段生成规则

所有动态参数 = 请求头部分 + 请求URL地址 + 请求Request参数 + 请求Body

上面的动态参数以key-value的格式存储，并以key值正序排序，进行拼接

最后拼接的字符串 在拼接appSecret

signature = DigestUtils.md5DigestAsHex(sortParamsMap + appSecret)

即拼接成一个字符串，然后做md5不可逆加密


请求头部分

请求头=“appId=xxxx&nonce=xxxx×tamp=xxxx&sign=xxx”

请求头中的4个参数是必须要传的，否则直接报异常


请求URL地址

这个就是请求接口的地址包含协议，如

https://mso.xxxx.com.cn/api/user


请求Request参数

即请求为Get方式的时候，获取的传入的参数


请求Body

即请求为Post时，请求体Body

从request inputstream中获取保存为String形式


签名算法实现

基本原理其实也比较简单，就是自定义filter，对每个请求进行处理；整体流程如下

1）验证必须的头部参数

2）获取头部参数，request参数，Url请求路径，请求体Body，把这些值放入SortMap中进行排序

3）对SortMap里面的值进行拼接

4）对拼接的值进行加密，生成sign

5）把生成的sign和前端传入的sign进行比较，如果不相同就返回错误

我们来看一下代码

以上是filter类，其中有个appSecret需要自己业务去获取，它的作用主要是区分不同客户端app。并且利用获取到的appSecret参与到sign签名，保证了客户端的请求签名是由我们后台控制的，我们可以为不同的客户端颁发不同的appSecret。

我们再来看看验证头部参数

上图其实就是验证是否传入值；不过其实有个很重要的一点，就是对此请求进行时间验证，如果大于10分钟表示此链接已经超时，防止别人来到这个链接去请求。这个就是防止盗链。

我们在来看看，如何获取各个参数

上面我们获取了各个参数，相对比较简单；我们在来看看生成sign，和验证sign

上面的流程中，会有个额外的安全处理，

· 防止盗链，我们可以让链接有失效时间

· 利用nonce参数，防止重复提交

在签名验证成功后，判断是否重复提交；

原理就是结合redis，判断是否已经提交过

总结

今天我们用签名的方式，对我们对外提供的接口起到了保护作用；但这种保护仅仅做到了防止别人篡改请求，或者模拟请求。

但是还是缺少对数据自身的安全保护，即请求的参数和返回的数据都是有可能被别人拦截获取的，而这些数据又是明文的，所以只要被拦截，就能获得相应的业务数据。

## API接口返回格式如何优雅设计？
前言

在移动互联网，分布式、微服务盛行的今天，现在项目绝大部分都采用的微服务框架，前后端分离方式，（题外话：前后端的工作职责越来越明确，现在的前端都称之为大前端，技术栈以及生态圈都已经非常成熟；以前后端人员瞧不起前端人员，那现在后端人员要重新认识一下前端，前端已经很成体系了）。

一般系统的大致整体架构图如下：

需要说明的是，有些小伙伴会回复说，这个架构太简单了吧，太low了，什么网关啊，缓存啊，消息中间件啊，都没有。因为老顾这篇主要介绍的是API接口，所以我们聚焦点，其他的模块小伙伴们自行去补充。

接口交互

前端和后端进行交互，前端按照约定请求URL路径，并传入相关参数，后端服务器接收请求，进行业务处理，返回数据给前端。

针对URL路径的restful风格，以及传入参数的公共请求头的要求（如：app_version,api_version,device等），老顾这里就不介绍了，小伙伴们可以自行去了解，也比较简单。

老顾注重介绍一下后端服务器如何实现把数据返回给前端？

返回格式

后端返回给前端我们一般用JSON体方式，定义如下：

{  #返回状态码  code:integer,    #返回信息描述  message:string,  #返回值  data:object }

CODE状态码

code返回状态码，一般小伙伴们是在开发的时候需要什么，就添加什么。

如接口要返回用户权限异常，我们加一个状态码为101吧，下一次又要加一个数据参数异常，就加一个102的状态码。这样虽然能够照常满足业务，但状态码太凌乱了

我们应该可以参考HTTP请求返回的状态码

下面是常见的HTTP状态码： 200 - 请求成功 301 - 资源（网页等）被永久转移到其它URL 404 - 请求的资源（网页等）不存在 500 - 内部服务器错误

我们可以参考这样的设计，这样的好处就把错误类型归类到某个区间内，如果区间不够，可以设计成4位数。

 -- 1000～1999 区间表示参数错误 #2000～2999 区间表示用户错误 #3000～3999 区间表示接口异常

这样前端开发人员在得到返回值后，根据状态码就可以知道，大概什么错误，再根据message相关的信息描述，可以快速定位。

Message

这个字段相对理解比较简单，就是发生错误时，如何友好的进行提示。一般的设计是和code状态码一起设计，如

再在枚举中定义，状态码

状态码和信息就会一一对应，比较好维护。

Data

返回数据体，JSON格式，根据不同的业务又不同的JSON体。

我们要设计一个返回体类Result

控制层Controller

我们会在controller层处理业务请求，并返回给前端，以order订单为例

我们看到在获得order对象之后，我们是用的Result构造方法进行包装赋值，然后进行返回。小伙伴们有没有发现，构造方法这样的包装是不是很麻烦，我们可以优化一下。

美观优化

我们可以在Result类中，加入静态方法，一看就懂

那我们来改造一下Controller

代码是不是比较简洁了，也美观了。

优雅优化

上面我们看到在Result类中增加了静态方法，使得业务处理代码简洁了。但小伙伴们有没有发现这样有几个问题：

1、每个方法的返回都是Result封装对象，没有业务含义

2、在业务代码中，成功的时候我们调用Result.success，异常错误调用Result.failure。是不是很多余

3、上面的代码，判断id是否为null，其实我们可以使用hibernate validate做校验，没有必要在方法体中做判断。

我们最好的方式直接返回真实业务对象，最好不要改变之前的业务方式，如下图

这个和我们平时的代码是一样的，非常直观，直接返回order对象，这样是不是很完美。那实现方案是什么呢？

实现方案

小伙伴们怎么去实现是不是有点思路，在这个过程中，我们需要做几个事情

1.定义一个注解@ResponseResult，表示这个接口返回的值需要包装一下

2.拦截请求，判断此请求是否需要被@ResponseResult注解

3.核心步骤就是实现接口ResponseBodyAdvice和@ControllerAdvice，判断是否需要包装返回值，如果需要，就把Controller接口的返回值进行重写。

注解类

用来标记方法的返回值，是否需要包装

拦截器

拦截请求，是否此请求返回的值需要包装，其实就是运行的时候，解析@ResponseResult注解

此代码核心思想，就是获取此请求，是否需要返回值包装，设置一个属性标记。

重写返回体

上面的代码就是判断是否需要返回值包装，如果需要就直接包装。这里我们只处理了正常成功的包装，如果方法体报异常的怎么办？处理异常也比较简单，只要判断body是否为异常类。

怎么做全局的异常处理，篇幅原因，老顾这里就不做介绍了，只要思路理清楚了，自行改造就行。

重新Controller

在控制器类上或者方法体上加上@ResponseResult注解，这样就ok了，简单吧。到此返回的设计思路完成，是不是又简洁，又优雅。

总结

这个方案还有没有别的优化空间，当然是有的。如：每次请求都要反射一下，获取请求的方法是否需要包装，其实可以做个缓存，不需要每次都需要解析。当然整体思路了解，小伙伴们就可以在此基础上面自行扩展。谢谢！！！

参考当前目录的示例



## 接口测试工具
通常我们使用postman,但是好多