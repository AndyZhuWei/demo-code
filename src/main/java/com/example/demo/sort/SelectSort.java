package com.example.demo.sort;

/**
 * @Author: zhuwei
 * @Date:2018/9/29 17:15
 * @Description: 原理：首先在未排序序列中找到最小的元素，存放到排序序列的起始位置，
 * 然后，再从剩余未排序的元素中寻找最小的元素，放到排序序列起始位之后。依次类推，直到所有的元素均排序完毕。
 */
public class SelectSort {
    public static void selectSort(int[] source){
        int length = source.length;
        for (int i = 0; i < length; i++) {
            for (int j = i + 1; j < length; j++){
                if (source[i] > source[j]){
                    swap(source, i, j);
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
}
