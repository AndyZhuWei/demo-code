package com.example.demo.nio;

import java.io.*;
import java.nio.channels.FileChannel;

/**
 * @Author: zhuwei
 * @Date:2019/8/15 12:56
 * @Description:
 */
public class FileTest {
    public static void main(String[] args) throws IOException {
        InputStream in = new FileInputStream(new File("E:\\江苏国寿重疾卡.xls"));
        OutputStream out = new FileOutputStream(new File("E:\\江苏国寿重疾卡.copy2.xls"));
        byte[] bytes = new byte[10];
        int i= -1;
        while((i = in.read(bytes))>-1) {
            out.write(bytes,0,i);
        }

    }

}
