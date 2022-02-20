package com.example.demo.jdk8;


import ch.qos.logback.core.net.SyslogOutputStream;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

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
//       fun(new IMessage(){
//            @Override
//            public void print() {
//                System.out.println("Hello World!");
//            }
//        });

        //以上的做法要求的实在是过于严谨了，所以在JDK1.8时代引入了函数式的编程，可以简化以上的代码
        // 使用Lamda表达式
        fun(()->System.out.println("Hello World!"));

        //首先要定义此处表达式里面需要接收变量，单行语句直接进行输出
        //fun((s)-> System.out.println(s));

        //编写多行语句
        /*fun((s)->{
            s = s.toUpperCase();//转大写
            System.out.println(s);
        });*/

        //编写表达式
       /* fun((s1,s2)->s1+s2);*/

        //即：将String.valueOf()方法变为IMessage4接口里的zhuanhuan()方法
//        IMessage4<Integer,String> msg = String :: valueOf;
//        String str = msg.zhuanhuan(1000);
//        System.out.println(str.replaceAll("0","9"));

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
//        Function<String,Boolean> fun = "##hello" :: startsWith;
//        System.out.println(fun.apply("##"));
//        //消费型接口
//        Consumer<String> cons = new MyDemo() :: print;
//        cons.accept("Hello World!");
//        Consumer<String> cons2 = System.out :: println;
//        cons2.accept("Hello World!");
//        //供给型接口
          Supplier<String> sup = "hello" :: toUpperCase;
//        System.out.println(sup.get());
//        //断言型接口
//        Predicate<String> pre = "hello" :: equalsIgnoreCase;
//        System.out.println(pre.test("hello"));



        //获取stream
        //1.数组
    //    String[] arr = new String[]{"ab","cd","ef"};
     //   Stream<String> arrStream = Arrays.stream(arr);
        //2.集合
   //     List<String> list = Arrays.asList("ab","cd","ef");
   //     Stream<String> colStream = list.stream();
        //3.值
    //    Stream<String> stream = Stream.of("ab","cd","ef");

        //stream方法使用
//        List<User> listUser = Arrays.asList(
//                new User("张三",11),
//                new User("王五",20),
//                new User("王五",91),
//                new User("张三",8),
//                new User("李四",44),
//                new User("李四",44),
//                new User("李四",44)
//        );
        //1.forEach() 使用该方法迭代流中的每个数据
//        // java 8 前
//        System.out.println("java 8 前");
//        for(User user: listUser){
//            System.out.println(user);
//        }
//        // java 8 lambda
//        System.out.println("java 8 lambda");
//        list.forEach(user -> System.out.println(user));
//
//        // java 8 stream lambda
//        System.out.println("java 8 stream lambda");
//        list.stream().forEach(user -> System.out.println(user));

        //2.sorted() 使用该方法排序数据
//        TestDemo testDemo = new TestDemo();
//        testDemo.testSort(listUser);


        //3.filter()：使用该方法过滤
//        TestDemo testDemo = new TestDemo();
//        testDemo.testFilter(listUser);

        //4.limit()：使用该方法截断
//        TestDemo testDemo = new TestDemo();
//        testDemo.testLimit(listUser);

        //5.skip()：与limit互斥，使用该方法跳过元素
        //TestDemo testDemo = new TestDemo();
        //testDemo.testSkip(listUser);

        //6.distinct()：使用该方法去重，注意：必须重写对应泛型的hashCode()和equals()方法
//        TestDemo testDemo = new TestDemo();
//        testDemo.testDistinct(listUser);

        //7.去重+按照年龄大于40以后从小到大+只取前二
        //TestDemo testDemo = new TestDemo();
        //testDemo.demo(listUser);

        //8.max，min，sum，avg，count testNum
        //TestDemo testDemo = new TestDemo();
        //testDemo.testNum(listUser);

        //9.map()：接收一个方法作为参数，该函数会被应用到每个元素上，并将其映射成一个新的元素
        //TestDemo testDemo = new TestDemo();
       // testDemo.testMap(listUser);












    }



    public void testSort(List<User> list) {
        System.out.println("-----排序前-----");
        list.forEach(user -> System.out.println(user));
        System.out.println("-----排序后-----");
        // java 8 前
        System.out.println("java 8 前");
        Collections.sort(list, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o1.getAge() - o2.getAge();
            }
        });
        for (User user : list) {
            System.out.println(user);
        }
        // java 8 stream 方法引用
        System.out.println("java 8 stream 方法引用");
        list.stream().sorted(Comparator.comparing(User::getAge)).forEach(user -> System.out.println(user));
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


    public void testFilter(List<User> list) {
        // 输出年龄大于50的人
        System.out.println("-----过滤前-----");
        list.forEach(user -> System.out.println(user));
        System.out.println("-----过滤后-----");
        // java 8 前
        System.out.println("java 8 前");
        for(User user: list){
            if (user.getAge() > 50) {
                System.out.println(user);
            }
        }
        // java 8 stream
        System.out.println("java 8 stream");
        list.stream().filter((User user) -> user.getAge() > 50).forEach(user -> System.out.println(user));
    }


    public void testLimit(List<User> list) {
        // 从第三个开始截断，只输出前三个
        System.out.println("-----截断前-----");
        list.forEach(user -> System.out.println(user));
        System.out.println("-----截断后-----");
        // java 8 前
        System.out.println("java 8 前");
        for (int i = 0; i < 3; i++) {
            System.out.println(list.get(i));
        }
        // java 8 stream
        System.out.println("java 8 stream");
        list.stream().limit(3).forEach(user -> System.out.println(user));

    }

    public void testSkip(List<User> list) {
        // 跳过前三个元素，从第四个开始输出
        System.out.println("-----跳过前-----");
        list.forEach(user -> System.out.println(user));
        System.out.println("-----跳过后-----");
        // java 8 前
        System.out.println("java 8 前");
        for (int i = 3; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
        // java 8 stream
        System.out.println("java 8 stream");
        list.stream().skip(3).forEach(user -> System.out.println(user));
    }


    public void testDistinct(List<User> list) {
        // 因为Arrays.asList() 返回的是Arrays的内部类ArrayList，操作remove，add会报错
        List<User> users = new ArrayList(list);
        // 为list去除重复数据
        System.out.println("-----去重前-----");
        list.forEach(user -> System.out.println(user));
        System.out.println("-----去重后-----");
        // java 8 前
        System.out.println("java 8 前");
        for (int i = 0; i < users.size() - 1; i++) {
            for (int j = users.size() - 1; j > i; j--) {
                if (users.get(j).getAge() == users.get(i).getAge() && users.get(j).getName()
                        .equals(users.get(i).getName())) {
                    users.remove(i);
                }
            }
        }
        for (User user : users) {
            System.out.println(user);
        }
        // java 8 stream
        System.out.println("java 8 stream");
        users.stream().distinct().forEach(user -> System.out.println(user));
    }

    //去重+按照年龄大于40以后从小到大+只取前二
    public void demo(List<User> list) {
        list.stream().distinct().filter(user -> user.getAge() > 40).sorted(
                Comparator.comparing(User::getAge)).limit(2).forEach(user -> System.out
                .println(user));

    }

    public void testNum(List<User> list) {
        IntSummaryStatistics num = list.stream().mapToInt(u -> u.getAge())
                .summaryStatistics();
        System.out.println("总共人数：" + num.getCount());
        System.out.println("平均年龄：" + num.getAverage());
        System.out.println("最大年龄：" + num.getMax());
        System.out.println("最小年龄：" + num.getMin());
        System.out.println("年龄之和：" + num.getSum());
    }

    /**
     * map 映射.
     */
    public void testMap(List<User> list) {
        // 只输出所有人的年龄
        list.stream().forEach(user -> System.out.println(user));
        System.out.println("映射后----->");
        List<Integer> ages = list.stream().map(user -> user.getAge()).collect(toList());
        ages.forEach(age -> System.out.println(age));

        // 小写转大写
        List<String> words = Arrays.asList("aaa", "vvvv", "cccc");
        System.out.println("全部大写---->");
        List<String> collect = words.stream().map(s -> s.toUpperCase()).collect(toList());
        collect.forEach(s -> System.out.println(s));
    }

    /**
     * flatMap .
     * flatMap()：对每个元素执行mapper指定的操作，并用所有mapper返回的Stream中的元素组成一个新的Stream作为最终返回结果，通俗易懂就是将原来的stream中的所有元素都展开组成一个新的stream
     */
    public void testFlatMap() {
        //创建一个 装有两个泛型为integer的集合
        Stream<List<Integer>> stream = Stream.of(Arrays.asList(1, 2, 3), Arrays.asList(4, 5));
        // 将两个合为一个
        Stream<Integer> integerStream = stream.flatMap(
                (Function<List<Integer>, Stream<Integer>>) integers -> integers.stream());
        // 为新的集合
        List<Integer> collect = integerStream.collect(toList());
        System.out.println("新stream大小:" + collect.size());
        System.out.println("-----合并后-----");
        collect.forEach(o -> System.out.println(o));

    }


    //findFirst() ：使用该方法获取第一个元素
    public void testFindFirst(List<User> list){
        User user = list.stream().findFirst().get();
        System.out.println(user);
    }

    //reduce() ：多面手
    //reduce 操作可以实现从一组元素中生成一个值
    //
    //sum()、max()、min()、count()等都是reduce操作，将他们单独设为函数只是因为常用
    //
    //例如：找到年龄最大的
    public void reduce() {
        List<User> list = Arrays.asList(
                // name，age
                new User("张三", 11),
                new User("王五", 20),
                new User("王五", 91),
                new User("张三", 8),
                new User("李四", 44),
                new User("李四", 44),
                new User("李四", 44)
        );

        Optional<User> reduce = list.stream().reduce((s1, s2) -> s1.getAge() > s2.getAge() ? s1 : s2);
        User user = reduce.get();
        System.out.println(user);

        Optional<User> max = list.stream().max(Comparator.comparing(User::getAge));
        User user2 = max.get();
        System.out.println(user2);

        // 求年龄之和
        Integer reduce2 = list.stream().reduce(0, // 该参数为初始值
                (integer, user3) -> integer + user3.getAge(), // 该参数为累加器，新元素如何累加
                (integer, integer2) -> integer + integer2);// 多个部分如何合并
        System.out.println(reduce2);

    }

    //使用collect()做字符串join
    public void reduce2() {
        // 使用Collectors.joining()拼接字符串
        Stream<String> stream = Stream.of("张三", "李四", "王五", "赵六");
//    String s = stream.collect(Collectors.joining()); // 张三李四王五赵六
//    String s = stream.collect(Collectors.joining("-")); // 张三-李四-王五-赵六
        String s = stream.collect(Collectors.joining("-", "(", ")")); // (张三-李四-王五-赵六)
        System.out.println(s);
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