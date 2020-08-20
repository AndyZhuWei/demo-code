1.在JDK1.8里面有一个最重要的概念：内部类访问方法参数的时候可以不加上final关键字。
 	所有出现的这些新特性，完全打破了Java已有的代码组成形式。
2.接口里面使用default或static定义方法的意义是避免子类重复实现同样的代码。
3.接口的使用还应该以抽象方法为主。
4.对于Lamda语法 有三种形式：
  (参数)->单行语句。
  (参数)->{多行语句}。
  (参数)->表达式
  利用Lamda表达式最终解决的问题：避免了匿名内部类定义过多无用的操作。
5.方法引用
一直以来都只是在对象上能够发现引用的声影，而对象引用的特点：不同的对象可以操作同一块内容。
而所谓的方法引用就是指为一个方法设置别名，相当于一个方法定义了不同的名字。
方法引用在Java8中一共定义了四种形式：
引用静态方法：类名称：：static方法名称
引用某个对象的方法：实例化对象：：普通方法
引用特定类型的方法：特定类：：普通方法
引用构造方法：类名称：：new
6.内建函数式接口
在JDK1.8里面提供一个包：java.util.function，提供有以下四个核心接口：
<1.功能性接口(Function):public interface Function<T,R>{public R apply(T t)}
-此接口需要接收一个参数，并且返回一个处理结果
<2.消费型接口(Consumer):public interface Consumer<T> {public void accept(T t)}
-此接口只是负责接收数据（引用数据是不需要返回），并且不返回处理结果
<3.供给型接口(Supplier):public interface Supplier<T>{public T get()}
-此接口不接受参数，但是可以返回结果
<4.断言型接口(Predicate):public interface Predicate<T> {public boolean test(T t)}
-进行判断操作使用
	所有在JDK1.8之中由于存在有以上的四个功能型接口，所以一般很少会由用户去定义新的函数式接口。



JDK 1.8 新特性之Stream
首先对stream的操作可以分为两类，中间操作(intermediate operations)和结束操作(terminal operations):

中间操作总是会惰式执行，调用中间操作只会生成一个标记了该操作的新stream。
结束操作会触发实际计算，计算发生时会把所有中间操作积攒的操作以pipeline的方式执行，这样可以减少迭代次数。计算完成之后stream就会失效。
虽然大部分情况下stream是容器调用Collection.stream()方法得到的，但stream和collections有以下不同：

无存储。stream不是一种数据结构，它只是某种数据源的一个视图，数据源可以是一个数组，Java容器或I/O channel等。
为函数式编程而生。对stream的任何修改都不会修改背后的数据源，比如对stream执行过滤操作并不会删除被过滤的元素，而是会产生一个不包含被过滤元素的新stream。
惰式执行。stream上的操作并不会立即执行，只有等到用户真正需要结果的时候才会执行。
可消费性。stream只能被“消费”一次，一旦遍历过就会失效，就像容器的迭代器那样，想要再次遍历必须重新生成。





