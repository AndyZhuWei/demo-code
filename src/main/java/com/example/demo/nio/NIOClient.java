package com.example.demo.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @Author: zhuwei
 * @Date:2018/9/28 15:39
 * @Description:
 */
public class NIOClient {

    /*private NIOClient() throws IOException {
        socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress("127.0.0.1",8888));
    }*/

    /*@Override
    public void run() {
        while(true) {
           byte[] bytes = new byte[1024];
            try {
                System.in.read(bytes);
                byteBuffer.put(bytes);
                byteBuffer.flip();
                socketChannel.write(byteBuffer);
                byteBuffer.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/

    public static void main(String[] args) {

        //创建连接的地址
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8888);
        //new Thread(new NIOClient()).start();
        SocketChannel socketChannel = null;
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);


        try {
            socketChannel = SocketChannel.open();
           // socketChannel.configureBlocking(false);
            socketChannel.connect(address);
            while (true) {
                byte[] bytes = new byte[1024];
                System.in.read(bytes);
                byteBuffer.put(bytes);
                byteBuffer.flip();
                socketChannel.write(byteBuffer);
                byteBuffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socketChannel != null) {
                try {
                    socketChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }
}
