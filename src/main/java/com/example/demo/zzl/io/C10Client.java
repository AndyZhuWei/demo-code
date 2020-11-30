//package com.example.demo.zzl.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;

/**
 * @author HP
 * @Description TODO
 * @date 2020/10/28-23:00
 */
public class C10Client {

    public static void main(String[] args) {
        LinkedList<SocketChannel> clients = new LinkedList<>();
        InetSocketAddress serverAddr = new InetSocketAddress("192.168.80.100",9090);

        for(int i=10000;i<65000;i++) {
            try {
                SocketChannel client1 = SocketChannel.open();

                SocketChannel client2 = SocketChannel.open();

              /*  client1.bind(new InetSocketAddress("192.168.80.10",i));
                client1.connect(serverAddr);
                boolean c1 = client1.isOpen();
                clients.add(client1);*/
                client2.bind(new InetSocketAddress("172.16.212.213",i));
                client2.connect(serverAddr);
                boolean c2 = client2.isOpen();
                clients.add(client2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("clients:"+clients.size());
    }
}
