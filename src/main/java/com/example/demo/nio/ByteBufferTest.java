package com.example.demo.nio;

import java.nio.ByteBuffer;

/**
 * @Description TODO
 * @Author zhuwei
 * @Date 2021/3/11 11:13
 */
public class ByteBufferTest {

    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        ByteBuffer byteBuffer1 = ByteBuffer.allocateDirect(1024);
    }
}
