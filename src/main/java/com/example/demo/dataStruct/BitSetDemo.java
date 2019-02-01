package com.example.demo.dataStruct;

import java.util.BitSet;

/**
 * @Author: zhuwei
 * @Date:2018/9/11 14:47
 * @Description:
 * https://blog.csdn.net/kongmin_123/article/details/82225172
 * 一个Bitset类创建一种特殊类型的数组来保存位值。BitSet中数组大小会随需要增加
 *
 *     1.Bitset是Java中的一种数据结构。Bitset中主要存储的是二进制位，做的也都是位运算，每一位只用来存储0，1值，主要用于对数据的标记。
 *     Bitset的基本原理是，用1位来表示一个数据是否出现过，0为没有出现过，1表示出现过。使用的时候可以根据某一个位是否为0表示此数是否出现过。
 *     JDK中的BitSet集合对是布隆过滤器中经常使用的数据结构Bitmap的相对简单的实现。BitSet采用了Bitmap的算法思想。
 *
 *     使用场景：整数，无重复
 *
 *     2.BitSet的底层实现是使用long数组作为内部存储结构的，这就决定了BitSet至少为一个long的大小，而且BitSet的大小为long类型大小(64位)的整数倍。 
 *
 *     long数组的每一个元素都可以当做是64位的二进制数，也是整个BitSet的子集。在BitSet中把这些子集叫做[Word]。
 *
 *     3.调用无参构造函数将会默认分配64bit,如果调用有参构造函数指定的大小没有超过64bit时也会分配64bit的大小
 *
 *     4.
 */
public class BitSetDemo {

    public static void main(String[] args) {
//        createDemo();
        bitsetSize();
    }

    private static void createDemo() {
        BitSet bits1 = new BitSet(16);
        BitSet bits2 = new BitSet(16);

        //set some bits
        //设置某一指定位
        for(int i=0;i<16;i++) {
            if((i%2)==0) bits1.set(i);
            if((i%5)==0) bits2.set(i);
        }

        System.out.println("Initial pattern in bits1:");
        System.out.println(bits1);
        System.out.println("\nInitial pattern in bits2:");
        System.out.println(bits2);

        //AND bits
        bits2.and(bits1);
        System.out.println("\nbits2 AND bits1: ");
        System.out.println(bits2);

        // OR bits
        bits2.or(bits1);
        System.out.println("\nbits2 OR bits1: ");
        System.out.println(bits2);

        // XOR bits
        bits2.xor(bits1);
        System.out.println("\nbits2 XOR bits1: ");
        System.out.println(bits2);
    }

    /**
     * 初始化一个bitset
     */
    public static void bitsetSize() {
        //无参构造函数的默认大小是64bit
        BitSet bitSet = new BitSet();
        System.out.println("default size="+bitSet.size()+" "+bitSet.length());

        //在新建BitSet时默认大小是64位，如果BitSet指定的初始大小没有超过64 bit时也会分配64 bit的大小
        BitSet bitSet2 = new BitSet(1);
        System.out.println("        size="+bitSet2.size()+" "+bitSet2.length());

        //这个时候bitSet.size() = 128;
        BitSet bitSet3 = new BitSet(100);
        System.out.println("allocate size="+bitSet3.size()+" "+bitSet3.length());

        //这个时候bitSet.size() = 256；
        BitSet bitSet4 = new BitSet(200);
        System.out.println("allocate size="+bitSet4.size()+" "+bitSet4.length());
    }

}
