package com.example.demo.thread;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;

/**
 * @Author: zhuwei
 * @Date:2018/12/18 9:19
 * @Description: 管道输入/输出流
 * 管道输入/输出流主要用于线程之间的数据传输，而传输的媒介为内存
 */
public class Piped {

    public static void main(String[] args) throws Exception {
        PipedWriter out = new PipedWriter();
        PipedReader in = new PipedReader();
        //将输出流和输入流进行连接，否则在使用时会抛出IOException
        out.connect(in);
        Thread printThread = new Thread(new Print(in),"PrintThread");
        printThread.start();
        int receive = 0;
        try {
            while((receive=System.in.read())!=-1) {
                out.write(receive);
            }
        } finally {
            out.close();
        }

    }

    static class Print implements Runnable {
        private PipedReader in;
        public Print(PipedReader in) {
            this.in = in;
        }

        @Override
        public void run() {
            int receive = 0;
            try {
                while((receive=in.read())!=-1) {
                    System.out.println((char)receive);
                }
            } catch (IOException e) {

            }
        }
    }
}
