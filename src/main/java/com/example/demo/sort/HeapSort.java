package com.example.demo.sort;

import java.util.Arrays;

/**
 * @Author: zhuwei
 * @Date:2019/6/18 9:55
 * @Description: 堆排序
 */
public class HeapSort {

    public static void main(String[] args) {
        int[] sources = new int[]{2,5,1,8,3,7,243,-123,-123,23};

        headSort(sources);
        System.out.println(Arrays.toString(sources));
    }


    public static void headSort(int[] datas) {
        for(int i=datas.length/2;i>=0;i--) {
            headAdajust(datas,i,datas.length);
        }

        for(int i=datas.length-1;i>0;i--) {
            swap(datas,0,i);
            headAdajust(datas,0,i-1);
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


    /**
     * 堆调整，其实就是从下往上、从右到左，将每个非终端结点当作根节点，将其
     * 和其子树调整成大顶堆。
     * 已知datas[i..length]记录的关键字，除datas[i]之外均满足堆的定义。
     * 本函数调整datas[i]记录使dats[i..length]成为一个大顶堆
     * @param datas 待调整的序列
     * @param i
     * @param length
     */
    private static void headAdajust(int[] datas,int i,int length) {
        int temp,j;
        temp = datas[i];
        for(j=2*i;j<length;j*=2) {

            if(j<length&&datas[j]<datas[j+1]) {
                j++;
            }
            if(temp>datas[j]) {
                break;
            }
            datas[i] = datas[j];
            i = j;
            if(i == 0) {
                break;
            }
        }
        datas[i] = temp;
    }



}
