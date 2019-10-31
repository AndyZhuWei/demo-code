package com.example.demo.xieyu.chapter01;

import org.junit.Test;

/**
 * @Author: zhuwei
 * @Date:2019/10/21 10:07
 * @Description:
 */

public class StringTests {

    /**
     * == 匹配内存单元上的内容，其实就是一个数字
     * 如果是基本类型就比较他们的值，如果是引用类型就比较他们的引用值，引用值可以理解为逻辑地址
     */
    @Test
    public void test01() {
        //JVM编译时优化，当编译器编译str = "a" + "b" + 1时，会将其编译为str="ab1" 后边可以通过javap查看
        //因为都是“常量”,编译器任务这3个常量叠加会得到固定的值，无须运行时再进行计算，所以就会这样优化
        //为什么要优化？为了提升整体工作效率和节约资源，能提前做的事情就提前做。
        //同理证明的道理：String的“+”操作并不一定比StringBuilder.append()慢，如果是编译时合并就会更快，因为在运行时是直接获取的
        //根本不需要再去运算。。所以不要坚定认为什么方式快、什么方式慢，一定要讲究场景。
        String str = "a" + "b" + 1;
        String ab1 = "ab1";
        System.out.println(str == ab1); //输出true

        //如果引用为null,JVM也会赋予某个指定的值
        String a = null;
        String b = null;
        System.out.println(a == b);//输出true
    }

    /**
     * equals: 该方法定义在Ojbect类中，默认是实现是用==比较两个对象的引用值，
     * 如果某个类的父类没有重写此方法，那么equals比较的就是引用的值。
     * <p>
     * equals之所以存在就是希望子类去重写，实现比较值的功能。String就重写了equals方法。
     * 根据自己的业务比较其中关键字的确定他们是否是“相等或相似的”
     * <p>
     * equals重写后，是否需要重写hashCode方法呢？
     * <p>
     * Java中的hashCode提供了對象的hashCode的值，在Object类中提供，是一个native方法，返回值默认与System.identityhashCode(object)
     * 一致，通常情况下，这值是对象头部的一部分二进制位组成的数字，这个数字具有一定的标识对象的意义存在，但绝不等价于地址。
     * hashCode的作用即使产生一个可以标识对象的数字，为什么需要这个数字呢，因为想将对象用在算法中，如果不这样，许多算法还得自己去组装数字，
     * 因为算法的基础是建立在数字基础之上的。
     * hashCode是为了算法快速定位数据而存在的，equals是为了对比真实值而存在的.
     */
    @Test
    public void test02() {
    }


    @Test
    public void test3() {
        String a = "a";
        final String c = "a";

        String b = a + "b";
        String d = c + "b";
        String e = getA() + "b";

        String compare = "ab";

        //b = a + "b"中a的值在引用上并未强制约束是不可以改变的，所以其值在运行时可能会被改变，尤其是在字节码增强技术面前，所以编译器就不会优化，此时+运算会被编译为下面类似的结果
        //StringBuilder temp = new StringBuilder();
        //temp.append(a).append("b");
        //String b = temp.toString();
        //后边可以通过javap查看
        System.out.println(b == compare);//false
        //c有一个final修饰符，在定义上强制约束了是不允许被改变的，所以编译器自然认为结果是不可变的，所以会进行优化
        System.out.println(d == compare);//true
        //e的内容来源于一个方法，虽然方法内返回一个常量的引用，但是编译器不会去看方法内部做了什么，因为这样的优化会使编译器困惑，因为编译器可能需要递归才能返回。
        //而递归返回的也是一个常量的引用，这个值不是final。
        //也许在JIT的优化中会实现动态inline(),但是编译器是肯定不会去做这个动作的。
        System.out.println(e == compare);//false
    }

    private static String getA() {
        return "a";
    }

    @Test
    public void test03() {
        String a = "a";
        String b = a + "b";
        String c = "ab";
        String d = new String(b);
        System.out.println(b == c);//false
        System.out.println(c == d);//false
        //HotSpot VM7及以前的版本中都是有Perm Gen(永久代)这个板块的，在前面例子中所谈到的String
        //引用所指向的对象，他们存储的空间正是这个板块中的一个“常量池”区域，他对于同一值的字符串保证全局唯一
        System.out.println(c == d.intern());//true
        System.out.println(b.intern() == d.intern());//true
    }


//    String.intern()分析
// 判断这个常量是否存在于常量池。
//              如果存在
//   判断存在内容是引用还是常量，
//                如果是引用，
//                 返回引用地址指向堆空间对象，
//                如果是常量，
//                 直接返回常量池常量
//  如果不存在，
//               将当前对象引用复制到常量池,并且返回的是当前对象的引用
    @Test
    public void test04() {
      /*  String s = new String("1");
        s.intern();
        String s2 = "1";
        System.out.println(s == s2);
*/
        String s3 = new String("1") + new String("1");
        s3.intern();
        String s4 = "11";
        System.out.println(s3 == s4);


        String a5 = new String("A") + new String("A");//只在堆上创建对象
        a5.intern();//在常量池上创建引用
        String a6 = "AA";//此时不会再在常量池上创建常量AA，而是将a5的引用返回给a6
        System.out.println(a5 == a6); //true

    }


    /**
     * StringBuilder.append()和String +
     * 首先确认一点，不是String的“+”操作本身慢，而是大循环中大量的内存使用使得它的内存开销变大，导致
     * 了系统频繁GC（在很多时候程序慢都是因为GC多造成的），而且是更多的FULL GC,效率才会急剧下降
     *
     * 在JVM中，提倡的重点是让这个“线程内所使用的内存”尽快结束，以便让JVM认为它是垃圾，在Young空间就
     * 尽量释放掉，尽量不要让它进入Old区域。一个很重要的因素是代码是否跑的够快，其次是分配的空间要足够小
     *
     * StringBuilder.append()扩容的规则：
     * 使用当前“StringBuilder的count值+传入字符串的长度”作为新的char[]数组的参考值，这个参考值将会与StringBuilder
     * 的char[]数组总长度2倍来取最大值，也就是最少会扩展为原来的2倍
     *
     * count是StringBuilder中有效元素的个数
     */
    @Test
    public void test05() {

    }











}
