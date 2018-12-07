package com.example.demo.dataStruct;

import java.util.BitSet;

/**
 * @Author: zhuwei
 * @Date:2018/9/11 14:47
 * @Description: 一个Bitset类创建一种特殊类型的数组来保存位值。BitSet中数组大小会随需要增加
 */
public class BitSetDemo {
    public static void main(String[] args) {
        BitSet bits1 = new BitSet(16);
        BitSet bits2 = new BitSet(16);

        //set some bits
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
}
