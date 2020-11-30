package com.example.demo.string;

import java.util.ArrayList;
import java.util.List;

public class StringDemo {
    public static void main(String[] args) {
        List<String> handler = new ArrayList<>();
        for(int i=0;i<1000;i++) {

        }
    }

    //定义一个静态类HugeStr
    static class HugeStr {
        private String str = new String(new char[800000]);
        public String getSubString(int begin,int end) {
            return str.substring(begin,end);//调用String的subString方法
        }

    }

    //定义一个静态类ImproveHugeStr
    static class ImproveHugeStr {
        private String str = new String(new char[10000000]);
        public String getSubString(int begin,int end) {
            return new String(str.substring(begin,end));//重新创建一个String对象
        }
    }
}


