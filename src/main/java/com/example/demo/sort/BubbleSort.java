package com.example.demo.sort;

import java.util.Arrays;

/**
 * @Author: zhuwei
 * @Date:2018/9/29 9:19
 * @Description: 冒泡排序算法是最简单的排序算法，是一种典型的交换排序算法。其原理是，实现了双层循环，
 * 内层循环将相邻两个数进行两两比较，将最大的一个数以冒泡（两两交换）的形式传送最队尾，
 * 一次只能将一个最大值传送到队尾；而外层循环实现了依次将当前最大值传送，最终实现排序；
 *
 */
public class BubbleSort {

    public static void sort(int[] source) {
        int length = source.length;
        for(int i=0;i<length-1;i++) {
            for(int j=0;j<length-1-i;j++) {
                if (source[j] > source[j+1]) {
                    swap(source,j,j+1);
                }
            }
        }
    }
    /**
     * 执行交换
     * @param source
     * @param x
     * @param y
     */
    public static void swap(int[] source, int x, int y){
        int temp = source[x];
        source[x] = source[y];
        source[y] = temp;
    }

    public static void main(String[] args) {
        int[] sources = new int[]{2,5,1,8,3,7,243,-123,-123,23};
        //BubbleSort.sort(sources);
        Arrays.sort(sources);
        System.out.println(Arrays.toString(sources));
    }
}
