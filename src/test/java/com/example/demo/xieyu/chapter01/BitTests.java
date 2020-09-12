package com.example.demo.xieyu.chapter01;

import org.junit.Test;

import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @Author: zhuwei
 * @Date:2019/10/22 17:38
 * @Description: 位相关计算
 *
 */
public class BitTests {

    public static final int PUBLIC           = 0x00000001;

    /**
     * The {@code int} value representing the {@code private}
     * modifier.
     */
    public static final int PRIVATE          = 0x00000002;

    /**
     * The {@code int} value representing the {@code protected}
     * modifier.
     */
    public static final int PROTECTED        = 0x00000004;

    /**
     * The {@code int} value representing the {@code static}
     * modifier.
     */
    public static final int STATIC           = 0x00000008;

    /**
     * The {@code int} value representing the {@code final}
     * modifier.
     */
    public static final int FINAL            = 0x00000010;

    /**
     * The {@code int} value representing the {@code synchronized}
     * modifier.
     */
    public static final int SYNCHRONIZED     = 0x00000020;

    /**
     * The {@code int} value representing the {@code volatile}
     * modifier.
     */
    public static final int VOLATILE         = 0x00000040;

    /**
     * The {@code int} value representing the {@code transient}
     * modifier.
     */
    public static final int TRANSIENT        = 0x00000080;

    /**
     * The {@code int} value representing the {@code native}
     * modifier.
     */
    public static final int NATIVE           = 0x00000100;

    /**
     * The {@code int} value representing the {@code interface}
     * modifier.
     */
    public static final int INTERFACE        = 0x00000200;

    /**
     * The {@code int} value representing the {@code abstract}
     * modifier.
     */
    public static final int ABSTRACT         = 0x00000400;

    /**
     * The {@code int} value representing the {@code strictfp}
     * modifier.
     */
    public static final int STRICT           = 0x00000800;



    /**
     * 例如，需要把某个数字映射到一个数组长度为5000的数组下标中，可以
     * 使用取模的方式或者key和数组长度-1按位与进行计算，即
     * key%5000或key&4999
     * 如果数组长度是2^n次方，那么key%2^n == key&(2^n -1)
     */
    @Test
    public void test01() {
        int[] a = new int[5000];
        int key = 50001;
        System.out.println(key%a.length);//1
        System.out.println(key&(a.length-1));//769

        int[] b = new int[16];
        int key2 = 50001;
        System.out.println(key2%b.length);//1
        System.out.println(key2&(b.length-1));//1

    }

    /**
     * 如果应用中出现大量判断“是否”的问题，我们不用每次判断的时候声明一个int类型来保存结果，
     * 完全可以使用位进行标识
     *
     *
     * 例如java.lang.reflect.Modifier中，它对类型的修饰符有各种各样的判断
     */
    @Test
    public void test02() {
        //在判断某位时也不用遍历各个位，至于如何判断可以参考Modifier中各类型的判断
        System.out.println(Integer.toBinaryString(10));
        //在Modifier中类型的修饰符会用固定的规格编码，比如：
        //是否为public,使用十六进制数据(0x00000001)表示
        //是否为static,使用十六进制数据(0x00000008)表示
        //java程序在读取字节码时，读取到一个数字，该数字只需要与对应的编码进行“&”操作，根据结果是否为0即可得到答案。
        System.out.println(Modifier.isFinal(FINAL));
    }

    /**
     * 数据转换
     */
    @Test
    public void test03() {
        //将十进制转换成“二进制字符串”，注意是二进制字符串
        System.out.println(Integer.toBinaryString(10));
        System.out.println(Long.toBinaryString(10L));
        //将十进制转换成“十六进制字符串”
        System.out.println(Integer.toHexString(10));
        System.out.println(Long.toHexString(10));
        System.out.println(Float.toHexString(1.1f));
        //将其他进制的数转换成二进制
        System.out.println(Integer.parseInt("10",16));
        System.out.println(Integer.valueOf("10",16));
        //数字与byte[]之间的转换
        //在Java语言中，int是右4个字节byte组成的，在网络上发送数据的时候，都是同步byte流来处理的，所以会发送4个byte
        //的内容，4个byte会由高到低顺序排列发送，接收防反向解析。可以通过DataOutputStream\DataInputStream的writeInt(int)
        //readInt()来得到正确的数据
    }

    /**
     * 数字太大，long都存放不下
     * long采用8个字节存放数字，如果超过8个字节，我们就需要使用BigDecimal
     * BigDecimal不是以固定长度的字节来存储数据，而是以对象的方式来管理位的。
     *
     *
     *
     *
     *
     *
     */

    @Test
    public void test04() {
        //这个数字long是放不下的
        BigDecimal bigDecimal =
                new BigDecimal("1231231242423423333332342334234253333423423423423423434233123123");
        System.out.println("数字的原始值是："+bigDecimal);

        bigDecimal = bigDecimal.add(BigDecimal.TEN);
        System.out.println("添加10以后："+bigDecimal);

        //二进制数字
        byte[] bytes = bigDecimal.toBigInteger().toByteArray();
        for(byte b : bytes) {
            System.out.println(b);
            System.out.println(b& 0xff);
            String bitSring = lPad(Integer.toBinaryString(b & 0xff),8,'0');
            System.out.println(bitSring);
        }

        //还原结果
        BigInteger bigInteger = new BigInteger(bytes);
        System.out.println("还原结果为："+bigInteger);

    }


    @Test
    public void test05() {
        BigDecimal a = new BigDecimal("1.0");
        BigDecimal b = new BigDecimal("0.9");
        BigDecimal c = new BigDecimal("0.8");

        BigDecimal x = a.subtract(b);
        BigDecimal y = b.subtract(c);

        //compareTo会忽略精度
        if(x.compareTo(y) == 0) {
            System.out.println("true");
        } else {
            System.out.println("false");
        }

        //equals方法会比较值和精度(1.0与1.00返回的结果为false)
        if(x.equals(y)) {
            System.out.println("true");
        } else {
            System.out.println("false");
        }
    }

    public static void main(String[] args) {
        byte b = -2;
        System.out.println(Integer.toBinaryString(b));
        System.out.println(Integer.toBinaryString(b & 0xff));
        int a = 255;
        System.out.println(Integer.toBinaryString(a));


    }

    private static String lPad(String now,
                               int expectLength,
                               char paddingChar) {
        if(now == null || now.length() >= expectLength ) {
            return now;
        }
        StringBuilder buf = new StringBuilder(expectLength);
        for(int i = 0,paddingLength = expectLength - now.length();
            i<paddingLength;i++) {
            buf.append(paddingChar);
        }
        return buf.append(now).toString();

    }
}
