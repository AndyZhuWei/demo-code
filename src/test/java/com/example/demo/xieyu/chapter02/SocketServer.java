package com.example.demo.xieyu.chapter02;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * @Author: zhuwei
 * @Date:2019/10/25 9:11
 * @Description:
 */
public class SocketServer {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8888);
        System.out.println("端口已经打开为8888，开始准备接收数据");
        SocketWrapper socketWrapper = null;
        try {
            socketWrapper = new SocketWrapper(serverSocket.accept());
            String line = socketWrapper.readLine();
            while(!"bye".equals(line)) {
                System.out.println("客户端传来数据："+line);
                socketWrapper.writeLine("我接收到你的数据："+line);
                line = socketWrapper.readLine();
            }
            socketWrapper.writeLine("close");
        } finally {
            if(socketWrapper != null) {
                socketWrapper.close();
            }
        }
    }
}
