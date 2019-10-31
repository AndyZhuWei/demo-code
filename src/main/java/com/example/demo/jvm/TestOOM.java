package com.example.demo.jvm;

import java.util.ArrayList;

/**
 * @Author: zhuwei
 * @Date:2019/9/11 5:31
 * @Description:
 */
public class TestOOM {
    static class Obj {
        public byte[] bytes = "hello everyone".getBytes();
    }


    public static void main(String[] args) {
        ArrayList<Obj> list = new ArrayList<Obj>();
        while(true) {
            list.add(new Obj());
        }
    }


}
