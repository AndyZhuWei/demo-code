package com.example.demo.thread;

import java.io.IOException;

/**
 * @author HP
 * @Description 普通引用
 * @date 2020/9/26-23:10
 */
public class NormalReference {
    public static void main(String[] args) throws IOException {
        M m = new M();
        m = null;
        System.gc();

        System.in.read();
    }
}
