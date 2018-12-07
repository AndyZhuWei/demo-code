package com.example.demo.sort;

import java.util.Arrays;

/**
 * @Author: zhuwei
 * @Date:2018/9/30 9:59
 * @Description: 原理：首先使用分割法将这个序列分割成一个一个已经排好了序的子序列，
 * 然后利用归并的方法将一个一个的子序列合并成排好的序列
 */
public class MergeSort {
    public static void main(String[] args) {
        int[] arrayA = {23, 47, 81, 95};
        int[] arrayB = {7, 14, 39, 55, 62, 74};
        int[] arrayC = new int[10];

        merge(arrayA,4,arrayB,6,arrayC);
        System.out.println(Arrays.toString(arrayC));
    }

    /**
     * @Author: zhuwei
     * @Date: 2018/9/30 10:15
     * @Description: 通过循环
     */
    public static void merge(int[] arrayA, int sizeA, int[] arrayB, int sizeB, int[] arrayC) {
        int aIndex=0,bIndex=0,cIndex=0;
        while (aIndex < sizeA && bIndex < sizeB) {
            if(arrayA[aIndex]<arrayB[bIndex]) {
                arrayC[cIndex++]=arrayA[aIndex++];
            } else {
                arrayC[cIndex++]=arrayB[bIndex++];
            }
        }

        while(aIndex<sizeA) {
            arrayC[cIndex++]=arrayA[aIndex++];
        }

        while(bIndex<sizeB) {
            arrayC[cIndex++]=arrayB[bIndex++];
        }
    }
}
