package com.example.demo.dataStruct;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Random;

/**
 * @Author: zhuwei
 * @Date: 2019/2/1 17:24
 * @Description: BitSet的应用
 * https://blog.csdn.net/kongmin_123/article/details/82257209
 *
 *
 */
public class BitSetDemo2 {

    public static void main(String[] args) {
//        demo1();
//        demo2();
//        demo3();
//        demo4();
//        demo5();
        demo6();
    }

    /**
     * 使用BitSet查找电话号码
     * 从一堆数量大概在千万级的电话号码列表中找出所有重复的电话号码，需要时间复杂度尽可能小
     * @return
     */
    private static void demo1() {
        //创建一个具有10000000位的bitset,初始化所有位的值为false
        BitSet bitSet = new BitSet(10000000);
        //将指定位的值设为true
        bitSet.set(9999);
        //或者bitSet.set(9999,true);
        //输出指定位的值
        System.out.println("9999:"+bitSet.get(9999));
        System.out.println("9998:"+bitSet.get(9998));
    }

    /**
     * 使用BitSet统计随机数的个数
     * 有1千万个随机数，随机数的范围在1到1亿之间。现在要求写出一种算法，将1到1亿之间没有在随机数中的数求出来？
     * 这里以100代替1亿
     */
    private static void demo2() {
       Random random = new Random();

       List<Integer> list = new ArrayList<>();
       for(int i=0;i<100;i++) {
           int randomResult = random.nextInt(100);
           list.add(randomResult);
       }

        System.out.print("0~100之间产生的随机数有:");
        for(int i=0;i<list.size();i++){
            System.out.print(list.get(i)+" ");
        }
        System.out.println();
        System.out.println("0~100之间的随机数产生了"+list.size()+"个");
        BitSet bitSet=new BitSet(100);
        for(int i=0;i<100;i++)
        {
            bitSet.set(list.get(i));
        }
        //public int cardinality()方法返回此BitSet中比特设置为true的数目
        //就是BitSet中存放的有效位的个数，如果有重复运算会进行自动去重
        System.out.println("0~100存在BitSet的随机数有"+bitSet.cardinality()+"个");
        System.out.print("0~100不在上述随机数中有:");
        int count = 0;
        for (int i = 0; i < 100; i++) {
            if(!bitSet.get(i))
            {
                System.out.print(i+" ");
                count++;
            }
        }
        System.out.println();
        //0~100不在产生的随机数中的个数就是100减去存在BitSet的随机数个数
        System.out.print("0~100不在产生的随机数中的个数为:"+count+"个");

    }

    /**
     * 使用BitSet查找某个范围内的所有素数的个数
     *
     *
     */
    private static void demo3() {
        int n = 2000000;
        long start = System.currentTimeMillis();
        BitSet sieve = new BitSet(n+1);
        int count=0;
        for (int i = 2; i <= n; i++){
            sieve.set(i);
        }
        for (int i = 2; i < n; i++){
            if (sieve.get(i)){
                for (int j = 2 ; j < Math.sqrt(i); j++){
                    if(i%j==0){
                        sieve.clear(i);
                        break;
                    }
                }
            }
        }
        int counter = 0;
        for (int i = 1; i < n; i++) {
            if (sieve.get(i)) {
                count++;
            }
        }
        long end = System.currentTimeMillis();
        System.out.println(count + " primes");
        System.out.println((end - start) + " ms");

    }

    /**
     * 使用BitSet进行排序
     * 如统计40亿个数据中没有出现的数据，将40亿个不同数据进行排序等。
     *
     * 统计个数和上面两个类似，就不说了。使用BitSet对数据进行排序的代码如下：
     */
    private static void demo4() {

        int[] array = new int[]{423,700,9999,2323,356,6400,1,2,3,2,2,2,2};
        BitSet bitSet = new BitSet();
        //BitSet默认初始大小为64
        System.out.println("BitSet size:"+bitSet.size());

        for(int i=0;i<array.length;i++) {
            bitSet.set(array[i]);
        }
        //public int cardinality()方法返回此BitSet中比特设置为true的数目
        //就是BitSet中存放的有效位的个数，如果有重复运算会进行自动去重
        int bitLen = bitSet.cardinality();
        System.out.println("bitLen="+bitLen);
        System.out.println("Before ordering:"+bitSet);

        //进行排序，即把bit为true的元素复制到另一个数组
        int[] orderedArray = new int[bitLen];
        int k = 0;
        //nextSetBit(int fromIndex)方法返回fromIndex之后的下一个值为true的索引
        //此循环就是按照顺序循环取出在bitSet存在的数，以此达到排序的目的
        for(int i=bitSet.nextSetBit(0);i>=0;i=bitSet.nextSetBit(i+1)) {
            orderedArray[k++]=i;
        }

        System.out.println("After ordering:");
        for(int i=0;i<bitLen;i++) {
            System.out.print(orderedArray[i]+"\t");
        }
        System.out.println();
        //或者顺序访问的数据不放入数组而进行直接读取
        System.out.println("iterate over the true bits in a BitSet");
        //或者直接迭代BitSet中bit为ture的元素
        for(int i=bitSet.nextSetBit(0);i>=0;i=bitSet.nextSetBit(i+1)) {
            System.out.print(i+"\t");
        }
    }

    /**
     * 使用BitSet求并、交、补集
     */
    public static void demo5() {
        BitSet bitSet = new BitSet(100);
        bitSet.set(1);
        bitSet.set(2);
        bitSet.set(3);

        BitSet bitSet2 = new BitSet(100);
        bitSet2.set(2);
        bitSet2.set(3);
        bitSet2.set(5);

        System.out.println("刚开始的bitSet:"+bitSet);
        System.out.println("刚开始的bitSet2:"+bitSet2);
        System.out.println("-----------------------------");
        //求并集
        bitSet.or(bitSet2);
        System.out.println("求完并集之后的bitSet:"+bitSet);
        System.out.println("求完并集之后的bitSet2:"+bitSet2);
        System.out.println("-------------------------------");
        //使bitSet回到刚开始的状态
        bitSet.clear(5);

        //求交集
        bitSet.and(bitSet2);
        System.out.println("求完交集之后的bitSet:"+bitSet);
        System.out.println("求完交集之后的bitSet2:"+bitSet2);
        System.out.println("-------------------------------------");
        //使bitSet回到刚开始的状态
        bitSet.set(1);
        //此方法返回在bitSet中不在bitSet2中的值，求得是bitSet相对于bitSet2的补集
        bitSet.andNot(bitSet2);
        System.out.println("求完补集之后的bitSet:"+bitSet);
        System.out.println("求完补集之后的butSet2:"+bitSet2);

    }

    /**
     * 垃圾邮件的识别
     * 这里使用到了布隆过滤器。布隆过滤是一个很长的二进制向量和一系列随机映射函数。
     * 这里的布隆过滤器使用Google的Guava
     * fpp:false positive probability
     */
    public static void demo6() {
        BloomFilter bloomFilter = BloomFilter.create(Funnels.integerFunnel(),
                500,0.01);
        bloomFilter.put(1);
        bloomFilter.put(2);
        bloomFilter.put(3);

        System.out.println(bloomFilter.mightContain(1));
        System.out.println(bloomFilter.mightContain(2));
        System.out.println(bloomFilter.mightContain(3));

        System.out.println(bloomFilter.mightContain(100));
    }


}
