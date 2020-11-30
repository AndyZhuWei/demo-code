package com.example.demo;


import com.example.demo.sort.ShellSort;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

/**
 * @Author: zhuwei
 * @Date:2020/6/2 9:34
 * @Description:
 */
public class SortTests {


    /**
     * 选择排序
     * 不稳定 O(N2)
     * @param testArray
     */
    public void testSelectionSort(int[] testArray) {
        int[] array = {7,1,3,8,2,9,5,4,6};
        if(testArray != null) {
            array = testArray;
        }

        for(int i=0;i<array.length-1;i++) {
            int minPos = i;
            for(int j=i+1;j<array.length;j++) {
                if(array[minPos] > array[j]) {
                    minPos = j;
                }
            }
            if(i != minPos) {
                swap(array,i,minPos);
            }
        }

       // System.out.println(Arrays.toString(array));
    }


    /**
     * 冒泡排序
     */
   // @Test
    public void testBuddleSort(int[] testArray) {
        int[] array = {7,1,3,8,2,9,5,4,6};
        if(testArray != null) {
            array = testArray;
        }

        for(int i=0;i<array.length;i++){
            for(int j=0;j<array.length-i-1;j++){
                if(array[j]>array[j+1]) {
                    swap(array,j,j+1);
                }
            }
        }
       // System.out.println(Arrays.toString(array));

    }

    /**
     * 插入排序
     */
    // @Test
    public void testInsertSort(int[] testArray) {
        int[] array = {7,1,3,8,2,9,5,4,6};
         if(testArray != null) {
             array = testArray;
         }

        for(int i=0;i<array.length-1;i++){
            for(int j=i+1;j>0;j--) {
                if(array[j]<array[j-1]) {
                    swap(array,j,j-1);
                }
            }
        }
         //System.out.println(Arrays.toString(array));

    }

    /**
     * 优化的插入排序
     */
    public void testInsertSort2(int[] testArray) {
        int[] array = {7,1,3,8,2,9,5,4,6};
        if(testArray != null) {
            array = testArray;
        }
        int i,j;
        for(i=1;i<array.length;i++){
            if(array[i]<array[i-1]) {
                int min = array[i];
                for(j=i-1;j>0;j--) {
                    if(array[j] > min) {
                        array[j+1]=array[j];
                    }
                }
                array[j+1] = min;
            }
        }
        //System.out.println(Arrays.toString(array));
    }


    /**
     * 希尔排序
     */
    public void testShellSort(int[] testArray) {
       // int[] testArray = {7,1,3,8,2,9,5,4,6};
        int gap = testArray.length/2;
        while(gap >= 1) {
            for(int i=gap;i<testArray.length;i++){
                int min;
                if(testArray[i]<testArray[i-gap]) {
                    min = testArray[i];
                    int j;
                    /* 需要注意这种写法是不正确的
                    for(j=i-gap;j>=0;j-=gap) {
                        if(testArray[j]>min) {
                            testArray[j+gap] = testArray[j];
                        }
                    }*/
                    //这种写法是正确的
                    for(j=i-gap;(j>=0&&testArray[j]>min);j-=gap) {
                          testArray[j+gap] = testArray[j];
                    }
                    testArray[j+gap] = min;
                }
                //System.out.println(Arrays.toString(testArray));
            }
           // System.out.format("gpa = %d: ", gap);
           // System.out.println(Arrays.toString(testArray));
            gap = gap/2;
        }

    }


    /**
     * 希尔排序
     */
    public void testShellSort2(int[] testArray) {
        //int[] testArray = {7,1,3,8,2,9,5,4,6};
        int gap = testArray.length;
        do {
            gap = gap/3 + 1;
            for(int i=gap;i<testArray.length;i++){
                int min;
                if(testArray[i]<testArray[i-gap]) {
                    min = testArray[i];
                    int j;
                    //这种写法是正确的
                    for(j=i-gap;(j>=0&&testArray[j]>min);j-=gap) {
                        testArray[j+gap] = testArray[j];
                    }
                    testArray[j+gap] = min;
                }
                //System.out.println(Arrays.toString(testArray));
            }
           // System.out.format("gpa = %d: ", gap);
           // System.out.println(Arrays.toString(testArray));
        } while (gap>1);

    }


    /**
     * 归并排序
     */
    public void testMergeSort(int[] testArray) {
        //int[] testArray = {7,1,3,8,2,9,5,4,6};
        int gap = testArray.length;
            for(int i=gap;i<testArray.length;i++){
                int min;
                if(testArray[i]<testArray[i-gap]) {
                    min = testArray[i];
                    int j;
                    //这种写法是正确的
                    for(j=i-gap;(j>=0&&testArray[j]>min);j-=gap) {
                        testArray[j+gap] = testArray[j];
                    }
                    testArray[j+gap] = min;
                }
                //System.out.println(Arrays.toString(testArray));
            }
            // System.out.format("gpa = %d: ", gap);
            // System.out.println(Arrays.toString(testArray));

    }


    /**
     * 归并排序时用到的merge方法
     * @param arrayA 已经排序好的一个数组
     * @param arrayB 已经排序好的另一个数组
     * @param left 左指针
     * @param right 右指针
     */
    private void merge(int arrayA[],int arrayB[],int left,int right) {
        int[] result = new int[arrayA.length+arrayB.length];

        //1.遍历A数组
        for(int i=0;i<arrayA.length&&i<arrayB.length;i++){
           // if(arrayA[i])
        }
    }














    @Test
    public void check() {
        boolean result = true;
        int[] array = new int[100000];
        Random random = new Random();
        for(int i=0;i<array.length;i++) {
            array[i] = random.nextInt(100000);
        }
        //复制数组
        int[] arrayCopy = new int[100000];
        System.arraycopy(array,0,arrayCopy,0,array.length);

        long startTime = System.currentTimeMillis();
        Arrays.sort(array);
        long endTime = System.currentTimeMillis();
        System.out.println("Arrays.sort coust time:"+(endTime-startTime));
        //this.testSelectionSort(arrayCopy);
        //this.testBuddleSort(arrayCopy);

        long startTime1 = System.currentTimeMillis();
//        this.testInsertSort(arrayCopy);
       // this.testInsertSort2(arrayCopy);
       // this.testShellSort(arrayCopy);
        this.testShellSort2(arrayCopy);
        long endTime1 = System.currentTimeMillis();
        System.out.println("user coust time:"+(endTime1-startTime1));


        for(int i=0;i<array.length;i++) {
            if(array[i] != arrayCopy[i]) {
                result = false;
                break;
            }
        }

        if(result) {
            System.out.println("true");
        } else {
            System.out.println("false");
        }






    }








    public void swap(int[] array,int i,int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }





}
