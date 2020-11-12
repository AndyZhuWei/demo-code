package com.example.demo.zzl.rpcdemo.rpc.transport;

import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author HP
 * @Description TODO
 * @date 2020/11/9-10:55
 */
public class ClientPool {
    NioSocketChannel[] clients;
    Object[] lock;

    ClientPool(int size) {
        clients = new NioSocketChannel[size];
        lock = new Object[size];
        for(int i=0;i<size;i++) {//锁可以先初始化
            lock[i]=new Object();
        }
    }
}
