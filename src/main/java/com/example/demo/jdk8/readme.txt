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