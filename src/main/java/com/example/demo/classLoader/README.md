#理解JVM中的类加载机制
.class文件只是一个静态的文件，那么JVM是加载.class文件是什么样的一个过程呢？这就涉及到JVM的类加载机制了。
所谓的JVM的类加载机制是指JVM把描述类的数据从.class文件加载到内存，并对数据进行校验、转换解析和初始化，最终
形成可以被虚拟机直接使用的Java类型，这就是JVM的类加载机制
Java语言中的加载、连接、初始化都是在运行期完成的，这样虽然对性能会有影响，但是却十分灵活。Java语言的动态
扩展行很强，其原因就是依赖于Java运行期动态加载和动态连接的特性，动态加载是指我们可以通过预定义的类加载器
和自定义的类加载器在运行期从本地、网络或其他地方加载.class文件；动态连接是指在面向接口编程中，在运行时才会指定
其实际的实现类。
![JVM类加载器](https://upload-images.jianshu.io/upload_images/4179925-da24c8878606eec9.png)
`JVMClassLoader.png`

## 一.类加载时机

首先将一下类的生命周期。
类的生命周期总共分为7个阶段：加载、校验、准备、解析、初始化、使用和卸载，如下图所示，
其中验证、准备、解析三个步骤又可统称为连接。
![类加载时机](https://upload-images.jianshu.io/upload_images/4179925-6e6ffb2824bf9385.png)
`使用相对路径是图片在idea中不显示 对应classLifeCycle.png`


加载、验证、准备、初始化和卸载五个步骤的顺序都是确定的，解析阶段在某些情况下有可能发生初始化之后，这是为了
支持Java语言的运行期绑定的特性。
在JVM虚拟机规范中并没有规定加载的时机，但是却规定了初始化的时机。而加载、验证、准备三个步骤是在初始化
之前。有以下五种情况需要必须立即对类进行初始化

1.遇到new、getstatic、pustatic或invokestatic这4条字节码指令时，如果类没有进行过初始化，则需要
先触发其初始化。生成这4条指令最常见的Java代码场景是：使用new关键字实例化对象、读取或设置一个类的静态
字段（被final修饰、已在编译期把结果放入到常量池的静态字段除外）以及调用一个类的静态方法的时候。
2.使用java.lang.reflect包的方法对类进行反射调用的时候
3.当初始化一个类的时候，如果发现其父类还没有被初始化过，则需要先触发其父类的初始化
4.当虚拟机启动时，用户需要指定一个要执行的主类（包含main()方法的类），虚拟机会先初始化这个类
5.当使用JDK1.7的动态语言支持时，如果一个java.lang.invoke.MethodHandle实例最后的解析结果
REF_getStatic、REF_putStatic、REF_invokeStatic的方法句柄，并且这个方法句柄所对应的类没有
进行过初始化，则需要先触发其初始化

**主动引用:** 上面这五种行为称为对一个类的主动引用，会触发类的初始化
**被动引用:** 除上面五种主动引用之外，其他引用类的方式都不会触发类的初始化，称为类的被动引用

被动引用的例子可以参考com.example.demo.jvm.NotInitialization中的示例

## 二.类加载的过程
### 2.1 加载
在加载阶段有三个步骤：

1.通过一个类的全限定名获取定义此类的二进制字节流
2.将二进制字节流所代表的静态存储结构转换为方法区中的运行时数据结构
3.在内存中生成一个代表此类的java.lang.Class的对象，作为方法区中这个类的访问入口

在这个阶段，有两点需要注意：
1.并没有规定从哪里获取二进制字节流。我们可以从.class静态存储文件中获取，也可以从zip、
jar等包中读取，可以从数据库中读取，也可以从网络中获取，甚至我们自己可以在运行时自动生成。
2.在内存中实例化一个代表此类的java.lang.Class对象之后，并没有规定此Class对象是方法
Java堆中的，有些虚拟机机会将Class对象放到方法区中，比如HotSpot

### 2.2验证
验证是连接阶段的第一个步骤，验证的目的是为了确保.class文件中的字节流所包含的信息是符合
当前虚拟机的要求，并且不会危害到虚拟机自身的安全的。
验证主要包括四个方面的验证：文件格式验证、元数据验证、字节码验证和符号引用验证

1.文件格式验证:主要验证二进制字节流数据是否符合.class文件的规范，并且该.class文件是否
在虚拟机的处理范围之内（版本号验证）。只有通过了文件格式的验证之后，二进制的字节流才会进入到
内存的方法区进行存储。而且只有通过了文件格式的验证之后，才会进行后面三个验证，后面三个验证都是
基于方法区的存储结构进行的。

2.元数据验证：主要是对类的元数据信息进行语义检查，保证不存在不符合java语义规范的元数据信息

3.字节码验证：字节码验证是整个验证中最复杂的一个过程，在元数据验证中，验证了元数据信息中的数据
类型做完校验后，字节码验证主要对类的方法体进行校验分析，保证被校验的类的方法不会做出危害虚拟机的行为

4.符号引用验证：符号引用验证发生在连接的第三个阶段解析阶段中，主要是保障解析过程可以正确地执行。
符合引用验证是类本身引用的其他类的验证，包括：通过一个类的全限定名是否可以找到对应的类，访问的
其他类中的字段和法规范是否存在，并且访问性是否合适等。

### 2.3 准备
在准备阶段所作的工作就是，在方法区中为类Class对象的类变量分配内存并初始化类变量，有三点需要注意：

1.在方法区中分配内存的只有类变量（被static修饰的变量），而不包括实例变量，实例变量将会跟随
着对象在Java堆中为其分配内存

2.初始化类变量的时候，是将类变量初始化为其类型对应的0值，比如有如下类变量，在准备阶段完成之后，val
的值是0而不是123，为val赋值为123，是在后面要讲的初始化阶段之后
```public static int val = 123;```

3.对于常量，其对应的值会在编译阶段就存储在字段表的ConstantValue属性中，所以在准备结束之后，常量的值
就是ConstantValue所指定的值了，比如如下，在准备阶段结束之后，val的值就是123了。
```public static final int val = 123;```

### 2.4 解析
解析是将符号引用解析为直接引用的过程，符号引用是值在.class常量池中存储的CONSTANT_Class_info、CONSTANT_Fieldref_info等
常量，直接引用则是直接指向目标的指针、相对偏移量或是一个能间接定位到目标的句柄，如果有了直接引用，那引用的目标必定已经在内存
中了。对于解析有以下3点需要注意：

1.虚拟机规范中并未规定解析阶段发生的具体时间，只规定了在执行newarray、new、putfidle、putstatic、getfield、getstatic
等16个指令之前，对它们所使用的符号引用进行解析。所以虚拟机可以在类被加载器加载之后就进行解析，也可以在执行这个几个指令之前才进行解析

2.对同一个符号引用进行多次解析是很常见的事，除invokedynamic指令以外，虚拟机实现可以对第一次解析的结果继续缓存，以后解析相同的符号引用
时，只要取缓存的结果就可以了

3.解析动作主要对类或接口、字段、类方法、接口方法、方法类型、方法句柄和调用点限定符7类符号引用进行解析

### 2.5初始化
类的初始化阶段才是真正开始执行类中定义的java程序代码。初始化说白了就是调用类构造器<clinit>()的过程，在类
的构造器中会为类变量初始化定义的值，会执行静态代码块中的内容。下面将介绍几点和开发者关系较为紧密的注意点。

1.类构造器<clinit>()是由编译器自动收集类中出现的类变量、静态代码块中的语句合并产生的，收集的顺序是在源文件
中出现的顺序决定的，静态代码块可以访问出现在静态代码块之前的类变量，出现在静态代码块之后的类变量，只可以赋值，但是不能
访问，比如如下代码
```java
public class Demo {
    private static String before = "before";
    static {
        after = "after";        //赋值合法
        System.out.println(before); //访问合法，因为出现在static{}之前
        System.out.println(after); //访问不合法，因为出现在static{}之后
    }
    private static String after;
}
```

2.<clinit>()类构造器和<init>()实例构造器不同，类构造器不需要显示调用的父类的构造器，在子类的类构造器调用之前，
会自动的调用父类的类构造器。因此虚拟机中第一个被调用的<clinit>()方法是java.lang.Object的类构造器

3.由于父类的类构造器优先于子类的类构造器执行，所以父类中的static{}代码块也优先于子类的static{}执行

4.类构造器<clinit>()对于类来说并不是必需的，如果一个类中没有类变量，也没有static{}，
那这个类不会有类构造器<clinit>()

5.接口中不能有static{},但是接口中也可以有类变量，所以接口中也可以有类构造器<clinit>{},但是接口的类构造器和类的
类构造器有所不同，接口在调用类构造器的时候，如果不需要，不用调用父接口的类构造器，除非用到了父接口中的类变量，接口的
实现类在初始化的时候也不会调用接口的类构造器

6.虚拟机会保证一个类的<clinit>()方法在多线程环境中被正确地加锁、同步如果多个线程同时去初始化一个类，那么只有一个线程
去执行这个类的类构造器<clinit>()，其他线程会被阻塞，直到活动线程执行完类构造器<clinit>()方法

## 三.类加载器
关于类加载器，我们带着几个问题去学习，什么是类加载器，类加载器分为哪几类，提到JVM类加载器就会提到双亲委派模型，双亲委派
模型有什么好处呢？我们就一一来解答这几个问题

### 3.1类加载器
类加载器是完成“通过一个类的全限定名获取这个类的二进制字节流”的工作，类加载器是独立与虚拟机之外存在的。
对于每一个类，都需要加载这个类的类加载和类本身来确认这个类在JVM虚拟机中的唯一性，每个类加载器都有
独立的类名称空间。换句话说，比较两个类是否“相等”，必须是在这两个类是由同一个类加载器加载的前提下
比较，如果两个类是由不同的类加载加载的，即使它们两个来自于同一个.class文件，那这两个类也是不同的。

### 3.2类加载器的分类

从不同的角度看，加载器可以有不同的分类方式
1. 从Java虚拟机角度来看呢，存在两种不同的类加载器
    * 一种是启动类加载器（Bootstrap ClassLoader）,这个类是由C++语言实现的，是虚拟机本身的一部分
    * 另一种是除了启动类加载器之外，所有的其他类加载，是由Java语言实现的，是独立于Java虚拟机之外的，
    并且全部继承自抽象类java.lang.ClassLoader
2. 从Java开发人员的角度来看的，可以分为以下3种类加载器
    * 启动类加载器(Bootstrap ClassLoader):加载<JAVA_HOME>\lib目录下和-Xbootclasspath参数
    所指定的可以被虚拟机识别的类库到内存中
    * 扩展类加载器（Extension ClassLoader）:加载<JAVA_HOME>\lib\ext目录中的和java.ext.dirs
    系统变量所指定的路径中的类库加载到内存中
    * 应用程序类加载器(Application ClassLoader):加载用户类路径上所指定的类库，开发者可以直接使用
    这个类加载器，是默认的类加载器
> 我们的应用程序就是由上面三种类加载器相互配合被加载进来的，如果有必要可以自定义类加载

### 3.3 双亲委派模型
对于上面提到的3种类加载，他们有如下图所示的关系
![双亲委派模型](https://upload-images.jianshu.io/upload_images/4179925-e1925c7e92e43b57.png)
`sqwp.png`

对于上图所示的这种关系呢，我们就称之为类类加载的双亲委派模型。在双亲委派模型中，除了顶层的Bootstrap ClassLoader之外，
其他的类加载器都有自己的父加载器。

双亲委派模型的工作流程是这样的，如果一个类加载器收到了一个加载类的请求，会首先把这个请求委派给自己的父加载器去加载，
这样的所有的类加载请求都会向上传递到 Bootstrap ClassLoader 中去，只有当父类加载器无法完成这个类加载请求时，
才会让子类加载器去处理这个请求。

使用双亲委派模型的好处就是，被加载到虚拟机中的类会随着加载他们的类加载器有一种优先级的层次关系。比如，
开发者自定义了一个 java.lang.Object 的类，但是你会发现，自定义的 java.lang.Object 永远无法被调用，
因为在使用自定义的类加载器去加载这个类的时候，自定义的类加载器会将加载请求传递到 Bootstrap ClassLoader 
中去，在 Bootstrap ClassLoader 中会从 rt.jar 中加载 Java 本身自带的 Java.lang.Object，这个时候加载
请求已经完成，找到了这个类，就不需要自定义的 ClassLoader 去加载用户路径下的 java.lang.Object 这个类了。

双亲委派模型对于 Java 程序的稳定运行十分重要，实现却非常简单

1. 首先会判断是否已经加载过此类了，如果已经加载过就不用再加载了
2. 如果没有加载过，则调用父类加载器去加载
3. 若父类加载器为空，则默认使用启动类加载器作为父类加载器加载
4. 若父类加载器加载未能成功会抛出 ClassNotFoundException 的异常
5. 再调用自己的 findClass() 方法进行加载
如下代码所示：
```java
protected synchronized Class<?> loadClass(String name, boolean resolve) throw ClassNotFoundException {
    Class c = findLoadedClass(name);
    if(c == null){
        try{
            if(parent != null){
                c = parent.loadClass(name, resolve);
            } else {
                c = findBootstrapClassOrNull(name);
            }
        } catch (ClassNotFoundException e){

        }
        if(c == null){
            c = findClass(name);
        }
    }
    if(resolve){
        resolveClass(c);
    }
    return c;
}
```
写在最后，在这篇文章中，我们对类的生命周期中的各个阶段进行了详细的介绍，并且对类的加载器及双亲委派模型进行了详细的介绍。
































