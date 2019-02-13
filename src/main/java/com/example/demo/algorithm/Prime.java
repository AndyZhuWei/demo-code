package com.example.demo.algorithm;

/**
 *
 * 素数：一个大于1的自然数，如果除了1和它本身外，不能被其他自然数整除(除0以外）的数称为素数(质数） ，否则称为合数。
 */
public class Prime {

    public static void main(String[] args) {
        int j;
        boolean flag;
        int count=0;
        for(int i=2;i<2000000;i++) {
            flag = false;
            for(j=2;j<Math.sqrt(i);j++) {
                if(i%j==0) {
                    flag = true;
                    break;
                }
            }
            if(!flag) {
//                System.out.print(i+"      ");
                count++;
            }
        }
        System.out.print(count+"      ");
    }
}
