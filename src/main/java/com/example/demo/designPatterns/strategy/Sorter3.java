package com.example.demo.designPatterns.strategy;

/**
 * @author HP
 * @Description TODO
 * @date 2020/9/9-8:57
 */
public class Sorter3<T> {

        public void sort(T[] arr,Comparator<T> comparator) {
        for (int i = 0; i < arr.length - 1; i++) {
            int minPos = i;
            for(int j=i+1;j<arr.length;j++) {
                minPos = comparator.compare(arr[j],arr[minPos])==-1?j:minPos;
            }
            swap(arr,i,minPos);
        }
    }


    public void swap(T[] source, int x, int y) {
        T temp = source[x];
        source[x] = source[y];
        source[y] = temp;
    }
}
