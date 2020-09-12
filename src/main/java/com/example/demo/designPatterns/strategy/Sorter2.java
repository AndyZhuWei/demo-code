package com.example.demo.designPatterns.strategy;

/**
 * @author HP
 * @Description TODO
 * @date 2020/9/9-8:57
 */
public class Sorter2 {

    /**
     * 如果想用Sorter2进行排序，则不管什么对象需要首先实现我们的Comparable方法，在其接口方法compareTo实现比较大小的逻辑
     * 这个Comparable是我们自定义的,但是目前这个Comparable接口不完美，需要在比较方法时做类型转换，我们可以用泛型，参考Comparable2
     * 相应的使用时参考Cat2
     *
     */
    public void sort(Comparable[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            int minPos = i;
            for(int j=i+1;j<arr.length;j++) {
                minPos = arr[j].compareTo(arr[minPos])==-1?j:minPos;
            }
            swap(arr,i,minPos);
        }
    }


    public static void swap(Comparable[] source, int x, int y) {
        Comparable temp = source[x];
        source[x] = source[y];
        source[y] = temp;
    }
}
