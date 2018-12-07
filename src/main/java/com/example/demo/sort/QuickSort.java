package com.example.demo.sort;

/**
 * @Author: zhuwei
 * @Date:2018/9/30 11:48
 * @Description: 原理：选择一个数组值作为基准，从数组两边开始遍历整个数组，
 * 我们选取一个基准值，从右侧开始遍历，找到小于基准的数，就替换掉基准位的值，
 * 接下来从左开始遍历，找到大于基准的值，就填补右侧之前替换掉基准位的值的位置空缺，
 * 左右的遍历依次交换进行，直到左右两个遍历相遇，将基准值放入相遇的位置。
 * 此时左侧全部为小于基准的值，右侧全为大于基准的值。
 * 之后对于前后段的排序也递归采用前面的方式，直到排序结束。
 */
public class QuickSort {
    /**
     * 快速排序
     * @param source 排序数组
     * @param a 起始点
     * @param b 结束点
     */
    public static void quickSort(int [] source, int a, int b){
        if (!(a < b)) return;
        int i = a;
        int j = b;
        int x = source[i];
        while (i < j){
            //循环递减，直到i、j相遇或者出现小于基准数的值
            while (i < j && source[j] > x){
                j--;
            }
            if (i < j){
                source[i] = source[j];
                i++;
            }
            //循环递增，直到出现大于基准数的值
            while (i < j && source[i] < x){
                i++;
            }
            if (i < j){
                source[j] = source[i];
                j--;
            }
        }
        //将基准点位移到中间位置
        source[i] = x;
        System.out.println(source);
        //递归调用，基准点前的排序；之后进行基准点之后的排序
        quickSort(source, a, i-1);
        quickSort(source, i+1, b);
    }
}
