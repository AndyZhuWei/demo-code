package com.example.demo.designPatterns.strategy;

/**
 * @author HP
 * @Description TODO
 * @date 2020/9/9-8:57
 */
public class Sorter {

    /**
     * 现在是对int进行排序，如果想用double排序怎么办，如果是对float呢或者对其他自定对象呢？
     *
     */
    public void sort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            int minPos = i;
            for(int j=i+1;j<arr.length;j++) {
                minPos = arr[j]<arr[minPos]?j:minPos;
            }
            swap(arr,i,minPos);
        }
    }

    /**
     * 自定义对象排序时比较大小我们可以定义一个方法，但是还是不通用，？看Sorter2
     */
    public void sort(Cat[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            int minPos = i;
            for(int j=i+1;j<arr.length;j++) {
                minPos = arr[j].compareTo(arr[minPos])==-1?j:minPos;
            }
            swap(arr,i,minPos);
        }
    }


    public static void swap(int[] source, int x, int y) {
        int temp = source[x];
        source[x] = source[y];
        source[y] = temp;
    }

    public static void swap(Cat[] source, int x, int y) {
        Cat temp = source[x];
        source[x] = source[y];
        source[y] = temp;
    }
}
