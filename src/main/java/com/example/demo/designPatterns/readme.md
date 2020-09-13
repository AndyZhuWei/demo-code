# 1.建造者模式
## 定义
将一个复杂对象的构建与其表示分离，使得同样的构建过程可以创建不同的表示
## 使用场景
当一个类的构造函数参数个数超过4个，而且这些参数有些是可选的参数，考虑使用构造者模式。
## 解决的问题
当一个类的构造函数参数超过4个，而且这些参数有些是可选的时，我们通常有两种办法来构建它的对象。 例如我们现在有如下一个类计算机类Computer，其中cpu与ram是必填参数，而其他3个是可选参数，那么我们如何构造这个类的实例呢,通常有两种常用的方式：

public class Computer {
    private String cpu;//必须
    private String ram;//必须
    private int usbCount;//可选
    private String keyboard;//可选
    private String display;//可选
}
第一：折叠构造函数模式（telescoping constructor pattern ），这个我们经常用,如下代码所示

public class Computer {
     ...
    public Computer(String cpu, String ram) {
        this(cpu, ram, 0);
    }
    public Computer(String cpu, String ram, int usbCount) {
        this(cpu, ram, usbCount, "罗技键盘");
    }
    public Computer(String cpu, String ram, int usbCount, String keyboard) {
        this(cpu, ram, usbCount, keyboard, "三星显示器");
    }
    public Computer(String cpu, String ram, int usbCount, String keyboard, String display) {
        this.cpu = cpu;
        this.ram = ram;
        this.usbCount = usbCount;
        this.keyboard = keyboard;
        this.display = display;
    }
}
第二种：Javabean 模式，如下所示

public class Computer {
        ...

    public String getCpu() {
        return cpu;
    }
    public void setCpu(String cpu) {
        this.cpu = cpu;
    }
    public String getRam() {
        return ram;
    }
    public void setRam(String ram) {
        this.ram = ram;
    }
    public int getUsbCount() {
        return usbCount;
    }
...
}
那么这两种方式有什么弊端呢？ 第一种主要是使用及阅读不方便。你可以想象一下，当你要调用一个类的构造函数时，你首先要决定使用哪一个，然后里面又是一堆参数，如果这些参数的类型很多又都一样，你还要搞清楚这些参数的含义，很容易就传混了。。。那酸爽谁用谁知道。 第二种方式在构建过程中对象的状态容易发生变化，造成错误。因为那个类中的属性是分步设置的，所以就容易出错。

为了解决这两个痛点，builder模式就横空出世了。

## 如何实现
1.在Computer 中创建一个静态内部类 Builder，然后将Computer 中的参数都复制到Builder类中。
2.在Computer中创建一个private的构造函数，参数为Builder类型
3.在Builder中创建一个public的构造函数，参数为Computer中必填的那些参数，cpu 和ram。
4.在Builder中创建设置函数，对Computer中那些可选参数进行赋值，返回值为Builder类型的实例
5.在Builder中创建一个build()方法，在其中构建Computer的实例并返回
参见builder.demo1
其实上面的内容是Builder在Java中一种简化的使用方式，经典的Builder 模式与其有一定的不同。
## 经典Builder模式(传统Builder 模式)
如上图所示，builder模式有4个角色。
Product: 最终要生成的对象，例如 Computer实例。
Builder： 构建者的抽象基类（有时会使用接口代替）。其定义了构建Product的抽象步骤，其实体类需要实现这些步骤。
其会包含一个用来返回最终产品的方法Product getProduct()。
ConcreteBuilder: Builder的实现类。
Director: 决定如何构建最终产品的算法. 其会包含一个负责组装的方法void Construct(Builder builder)， 
在这个方法中通过调用builder的方法，就可以设置builder，等设置完成后，就可以通过builder的 getProduct()
 方法获得最终的产品。
参见builder.demo2

可以看到，文章最开始的使用方式是传统builder模式的变种， 首先其省略了director 这个角色，
将构建算法交给了client端，其次将builder 写到了要构建的产品类里面，最后采用了链式调用。

##总结
设计模式值得你刻意练习！ 


创建型模式
1.AbstractFactory
2.Builder
3.FactoryMethod
4.Prototype
5.Singleton

结构性模式
1.Adapter
2.Bridge
3.Composite
4.Decorator
5.Facade
6.Flyweight
7.Proxy

行为模式
1.Chain of Responsibilty
2.Command
3.Interpreter
4.Iterator
5.Mediator
6.Memento
7.Observer
8.State
9.Strategy
10.Template Method
11.Visitor
























