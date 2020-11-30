package com.example.demo.netty;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

/**
 * @author HP
 * @Description TODO
 * @date 2020/9/17-15:32
 */
public class ServerBO {
    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket();
            ss.bind(new InetSocketAddress(8888));
            ss.accept();
            System.out.println("a client connected");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
