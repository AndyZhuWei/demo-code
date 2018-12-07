package com.example.demo.sort;

/**
 * @Author: zhuwei
 * @Date:2018/9/29 9:50
 * @Description: 希尔排序(Shell's Sort)是插入排序的一种又称“缩小增量排序”（Diminishing Increment Sort），
 *  是直接插入排序算法的一种更高效的改进版本。希尔排序是非稳定排序算法。
 *
 *
 *  根据一定的步长，将数据进行分组，然后对每组进行排序，在每一组数据都有序之后，缩小分组的步长，
 *  继续进行组内排序，直到步长缩短为0，相应的排序也就结束了。他可以显著的减少数据交换次数，加快排序速度。
 */
public class ShellSort {

    public static void shellSort(int [] source){
        int gap = source.length/2;
        while (gap>=1){
            for (int i = gap; i < source.length; i++) {
                if (source[i] < source[i-gap]){
                    //缓存当前值
                    int temp = source[i];
                    int j;
                    //直接插入算法，找到当前值的排序位置，将数组后移到分组的点，最后将缓存值差插入合适的位置
                    for (j = i - gap; (j >= 0) && (temp < source[j]); j -= gap){
                        source[j + gap] = source[j];
                    }
                    source[j + gap] = temp;
                }
            }
            System.out.format("gpa = %d: ", gap);
            printAll(source);
            gap = gap/2;
        }
    }

    // 打印完整序列
    public static void printAll(int[] list) {
        for (int value : list) {
            System.out.print(value + " ");
        }
        System.out.println();
    }
}
