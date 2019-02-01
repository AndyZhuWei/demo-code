package com.example.demo.bit;

/**
 * @Author: zhuwei
 * @Date:2019/1/23 16:55
 * @Description: 移位操作
 *
 *java中共有3种移位操作符，分别为"<<"(左移位操作符)、">>"（右移位操作符）和">>>"(无差别右移位操作符)。
 *
 * 对于java语言中的移位操作需要注意以下3点：
 *
 * 1.移位操作的对象为整形（int）或者长整形（long），对于char、byte和short类型自动执行向上转型为int。
 *
 * 2.符号位参与到移位过程，对于"<<"和">>"，移位是带符号的，在高位补与原符号位相同的数字，即正数高位补0，负
 *
 *    数低位补1；对于">>>"，移位操作是无差别的，在高位补0。
 *
 * 3.第三个问题是关于移位操作的宽度问题。移位宽度为截取移位操作的右操作数低五位得到的正整数，因此取值范围为0
 *
 *    ~31(对于长整形为低6位，因此取值范围为0~63)。
 *
 *
 *
 *
 *
 *    正整数的原码、反码和补码都一样；
 *
 * 负数部分：
 *
 * 原码和反码的相互转换：符号位不变，数值位按位取反
 *
 * 原码和补码的相互转换：符号位不变，数值位按位取反,末位再加1
 *
 * 已知补码，求原码的负数的补码：符号位和数值位都取反，末位再加1
 *
 *
 */
public class BitDemo {

    public static void main(String[] args) {
//        shift();
//        shift2();
        shift3();
    }


     public static void shift() {
        int i = -1;
        System.out.println(i+":");//-1:
        System.out.println(Integer.toBinaryString(i));//11111111111111111111111111111111
        i = i >>> 31;
        System.out.println(i+":");//1:
        System.out.println(Integer.toBinaryString(i));//1

        int j = -1;
        j = j >> 1;
        System.out.println(j+":");//-1:
        System.out.println(Integer.toBinaryString(j));//11111111111111111111111111111111

     }

    /**
     * 在java中无论左移右移，会遵循下面的规则
     *
     * 对于int类型的:
     * value<<n（其中value为int，n>=0） 等价于 value<<(n%32)
     * value>>n （其中value为int，n>=0） 等价于 value>>(n%32)
     * value>>>n （其中value为int，n>=0） 等价于 value>>>(n%32)
     *
     * 对于long类型的:
     * value<<n（其中value为long，n>=0） 等价于 value<<(n%64)
     * value>>n （其中value为long，n>=0） 等价于 value>>(n%64)
     * value>>>n （其中value为long，n>=0） 等价于 value>>>(n%64)
     *
     * 而对于byte、short、char遵循int的规则
     */
    public static void shift2() {
        int value = Integer.valueOf("00001111101001011111000010101100",2);
        System.out.println(value+":");//262533292:
        value = value << 32;
        System.out.println(value+":");//262533292:

        long value2 = Long.valueOf("0000111110100101111100001010110000001111101001011111000010101100",2);
        System.out.println(value2+":");//1127571903513751724:
        value2 = value2 >> 64;
        System.out.println(value2+":");//1127571903513751724:

    }


    /**
     *若是对int的值移负数位，java会截取那个负数的低5位。
     * -38的二进制表示是：11111111111111111111111111011010，截取低5位得：11010，这个数是26，也就是说：
     * value <<= -38 等价于 value <<= 26,得到的二进制是10110000000000000000000000000000，首位为1，
     * 所以该二进制是补码，换成其绝对值的源码为01010000000000000000000000000000,对应的十进制为1342177280,
     * 加上负号就是-1342177280。
     * 对于int是取低5位，对于long是取低6位
     * 也就是说：
     * value << -n(value为int,n>=0) 等价于 value << (-n & 31)
     * value >> -n(value为int,n>=0) 等价于 value >> (-n & 31)
     * value >>> -n(value为int,n>=0) 等价于 value >>> (-n & 31)
     *
     * 而对于long
     * value << -n(value为long,n>=0) 等价于 value << (-n & 63)
     * value >> -n(value为long,n>=0) 等价于 value >> (-n & 63)
     * value >>> -n(value为long,n>=0) 等价于 value >>> (-n & 63)
     *
     * 而对于byte、short、char遵循int的规则
     *
     *
     * 若一个数m满足： m=2的n次方
     *
     * 那么k % m = k & (m-1)
     */
    public static void shift3() {
        int value = Integer.valueOf("00001111101001011111000010101100",2);
        System.out.println(value+":");//262533292:
        value <<= -38;
        System.out.println(value+":");//-1342177280:

        long value2 = Long.valueOf("0000111110100101111100001010110000001111101001011111000010101100",2);
        System.out.println(value2+":");//1127571903513751724:
        value2 = value2 >> -74;
        System.out.println(value2+":");//62:

    }
}
