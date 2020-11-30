package com.example.demo.string;

import java.util.StringTokenizer;

public class SplitAndStringTokenizer {

    public static void main(String[] args) {
        String orgStr = null;
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<100000;i++) {
            sb.append(i);
            sb.append(",");
        }
        orgStr = sb.toString();
        long start = System.currentTimeMillis();
        for(int i=0;i<100000;i++) {
            orgStr.split(",");//调用split方法切割字符串
        }
        long end = System.currentTimeMillis();
        System.out.println(end-start);

        start = System.currentTimeMillis();
        String orgStr1 = sb.toString();
        StringTokenizer st = new StringTokenizer(orgStr1,",");
        for(int i=0;i<100000;i++){
            st.nextToken();
        }
        st = new StringTokenizer(orgStr1,",");//StringTokenizer类切割字符串
        end = System.currentTimeMillis();
        System.out.println(end-start);

        start = System.currentTimeMillis();
        String orgStr2 = sb.toString();
        String temp = orgStr2;
        while(true) {
            String splitStr = null;
            int j= temp.indexOf(",");
            if(j<0)break;
            splitStr = temp.substring(0,j);
            temp = temp.substring(j+1);//采用String对象的subString方法切割字符串
        }
        temp = orgStr2;
        end = System.currentTimeMillis();
        System.out.println(end-start);


    }
}
