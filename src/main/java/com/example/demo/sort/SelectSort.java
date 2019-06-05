package com.example.demo.sort;

import java.util.Arrays;

/**
 * @Author: zhuwei
 * @Date:2018/9/29 17:15
 * @Description: 通过n-i次关键字间的比较，从n-i+1个记录中选出关键字最小的记录，并和第i个记录交换
 */
public class SelectSort {

    /**
     * 简单排序算法
     *
     * @param datas
     */
    public static void simpleSelectSort(int[] datas) {
        for (int i = 0; i < datas.length; i++) {
            int min = i;
            for (int j = i + 1; j < datas.length; j++) {
                if (datas[min] > datas[j]) {
                    min = j;
                }
            }
            if (min != i) {
                swap(datas, i, min);
            }
        }
    }

    public static void main(String[] args) {
        int[] sources = new int[]{2, 5, 1, 8, 3, 7, 243, -123, 23};
        SelectSort.simpleSelectSort(sources);
        System.out.println(Arrays.toString(sources));
    }


    /**
     * 执行交换
     *
     * @param source
     * @param x
     * @param y
     */
    public static void swap(int[] source, int x, int y) {
        int temp = source[x];
        source[x] = source[y];
        source[y] = temp;
    }
}
