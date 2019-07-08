package com.example.demo.jdk8;


import ch.qos.logback.core.net.SyslogOutputStream;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @Author: zhuwei
 * @Date:2019/7/6 21:37
 * @Description:
 */
public class TestDemo {

    public static void main(String[] args) {
//        IMessage msg = new MessageImpl();
//        msg.fun();//此方法是在接口里面直接定义的
//        IMessage.get();

        //实际上整个代码之中，如果是fun()方法，最终需要的只是一个输出而已，
        //但是由于Java的开放结构性完整要求所以不得不在这个核心的语句上嵌套更多的内容。
        //
      /*  fun(new IMessage(){
            @Override
            public void print() {
                System.out.println("Hello World!");
            }
        });*/

        //以上的做法要求的实在是过于严谨了，所以在JDK1.8时代引入了函数式的编程，可以简化以上的代码
        // 使用Lamda表达式
       // fun(()->System.out.println("Hello World!"));

        //首先要定义此处表达式里面需要接收变量，单行语句直接进行输出
       // fun((s)-> System.out.println(s));

        //编写多行语句
        /*fun((s)->{
            s = s.toUpperCase();//转大写
            System.out.println(s);
        });*/

        //编写表达式
       /* fun((s1,s2)->s1+s2);*/

        //即：将String.valueOf()方法变为IMessage4接口里的zhuanhuan()方法
        /*IMessage4<Integer,String> msg = String :: valueOf;
        String str = msg.zhuanhuan(1000);
        System.out.println(str.replaceAll("0","9"));*/

        //String类的toUpperCase()定义：public String toUpperCase()
        //这个方法没有参数，但是有返回值，并且这个方法一定要有实例化对象的情况下才了可以调用
        //"hello"字符串是String类的实例化对象，所以可以直接调用toUpperCase()方法
        //将toUpperCase()函数的引用交给IMessage5接口
       /* IMessage5<String> msg = "hello"::toUpperCase;
        String str = msg.upper();//相当于"hello".toUpperCase()
        System.out.println(str);*/

        //通过两个代码演示应该已经发现了，如果要实现方法的引用，
        //那么必须要有接口，而且最为关心的是接口里面需要只存在一个方法。否则方法是无法进行引用的。
        //所以为了保证被引用接口里面只能够有一个方法，那么就需要增加一个注解的声明。

        //特定类引用
        /*IMessage6<String> msg = String :: compareTo;
        System.out.println(msg.compare("A","B"));*/

        //构造方法应用
       /* IMessage7<Book> msg = Book :: new;
        Book book = msg.create("Java开发",20.2);
        System.out.println(book);*/

        //内嵌的函数式接口
        //功能性
        Function<String,Boolean> fun = "##hello" :: startsWith;
        System.out.println(fun.apply("##"));
        //消费型接口
        Consumer<String> cons = new MyDemo() :: print;
        cons.accept("Hello World!");
        Consumer<String> cons2 = System.out :: println;
        cons2.accept("Hello World!");
        //供给型接口
        Supplier<String> sup = "hello" :: toUpperCase;
        System.out.println(sup.get());
        //断言型接口
        Predicate<String> pre = "hello" :: equalsIgnoreCase;
        System.out.println(pre.test("hello"));



    }

    public static void fun(IMessage msg) {
        msg.print();
    }

    public static void fun(IMessage2 msg) {
        msg.print("Hello World!");//设置参数的内容
    }

    public static void fun(IMessage3 msg) {
        System.out.println(msg.add(10,20));
    }
}

class MyDemo{
    //此方法没有返回值，但是有参数
    public void print(String str) {
        System.out.println(str);
    }
}

class Book {
    private String title;
    private double price;
    public Book(String title,double price) {
        this.title=title;
        this.price=price;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", price=" + price +
                '}';
    }
}