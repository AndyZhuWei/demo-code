package com.example.demo.sort;

/**
 * @Author: zhuwei
 * @Date:2018/9/29 9:29
 * @Description: 插入排序算法有种递归的思想在里面，它由N-1趟排序组成。初始时，只考虑数组下标0处的元素，只有一个元素，显然是有序的。

然后第一趟 对下标 1 处的元素进行排序，保证数组[0,1]上的元素有序；

第二趟 对下标 2 处的元素进行排序，保证数组[0,2]上的元素有序；

.....

.....

第N-1趟对下标 N-1 处的元素进行排序，保证数组[0,N-1]上的元素有序，也就是整个数组有序了。

它的递归思想就体现在：当对位置 i 处的元素进行排序时，[0,i-1]上的元素一定是已经有序的了
 */
public class InsertSort {

    public static void insertSourt(int[] source) {
        for(int i=0;i<source.length;i++) {
            for(int j=i;(j>0)&&(source[j]<source[j-1]);j--) {
                swap(source,j,j-1);
            }
        }
    }

    public static void swap(int[] source, int x, int y){
        int temp = source[x];
        source[x] = source[y];
        source[y] = temp;
    }
}
