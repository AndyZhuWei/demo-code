package com.example.demo.zzl.rpcdemo;

import com.example.demo.zzl.rpcdemo.prxoy.MyProxy;
import com.example.demo.zzl.rpcdemo.rpc.Dispatcher;
import com.example.demo.zzl.rpcdemo.rpc.transport.ServerDecode;
import com.example.demo.zzl.rpcdemo.rpc.transport.ServerRequestHandler;
import com.example.demo.zzl.rpcdemo.service.Car;
import com.example.demo.zzl.rpcdemo.service.Fly;
import com.example.demo.zzl.rpcdemo.service.MyCar;
import com.example.demo.zzl.rpcdemo.service.MyFly;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.junit.Test;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author HP
 * @Description TODO
 * @date 2020/11/6-9:45
 */
/*
 * 1.先假设一个需求，写一个RPC
 * 2.来回通信，连接数量 拆包？
 * 3.动态代理，序列化、反序列化 、协议封装
 * 4.连接池
 * 5.就像调用本地方法一样去调用远程方法，面向java中所谓的 面向interface开发
 *
 */
public class MyRPCTest {

    //单独启动服务端的时候，服务端就有一个JVM
    @Test
    public void startServer() {
        MyCar car = new MyCar();
        MyFly fly = new MyFly();
        Dispatcher dis = Dispatcher.getDis();
        dis.register(Car.class.getName(),car);
        dis.register(Fly.class.getName(),fly);




        NioEventLoopGroup boss = new NioEventLoopGroup(50);
        NioEventLoopGroup worker = boss;

        ServerBootstrap sbs = new ServerBootstrap();
        ChannelFuture bind = sbs.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        System.out.println("server accept client port:" + socketChannel.remoteAddress().getPort());
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new ServerDecode());
                        pipeline.addLast(new ServerRequestHandler(dis));
                    }
                }).bind(new InetSocketAddress("localhost", 9090));
        try {
            bind.sync().channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


    //模拟consumer端
    @Test
    public void get() {

        //在这里启动服务器的时候，服务端和客户端时在一个JVM中
      /*  new Thread(() -> {
            startServer();
        }).start();
        System.out.println("server started......");*/

        AtomicInteger num = new AtomicInteger(0);
        int size = 20;
        Thread[] threads = new Thread[size];
        for (int i = 0; i < size; i++) {
            threads[i] = new Thread(() -> {
                Car car = MyProxy.proxyGet(Car.class);//动态代理实现
                String arg = "hello" + num.incrementAndGet();
                String res = car.ooxx(arg);
                System.out.println("client over msg:" + res+" src arg:"+arg);
            });
        }

        for (Thread thread : threads) {
            thread.start();
        }

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }


//        Fly fly = proxyGet(Fly.class);//动态代理实现
//        fly.xxoo("hello");
    }




}










