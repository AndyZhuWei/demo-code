package com.example.demo.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author HP
 * @Description Netty的客户端
 * @date 2020/9/17-15:22
 */
public class Client2 {

    public static void main(String[] args) {
        //事件处理的线程池，构造时可以传入线程数的个数，如果不传，默认就是核数乘以2的数量
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        //辅助启动类
        Bootstrap bootstrap = new Bootstrap();

        try {
            //group()把线程池放进来
            ChannelFuture channelFuture = bootstrap.group(eventLoopGroup)
                    //指定连接通道，如果指定NioSocketChannel则为非阻塞通道
                    //如果指定SocketChannel则为阻塞通道
                    .channel(NioSocketChannel.class)
                    //当channel上有事件了交给哪个handler处理
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            System.out.println(ch);
                        }
                    })
                    //connect是一个异步方法
                    .connect("localhost",8888);

            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if(!future.isSuccess()) {
                        System.out.println("not connected");
                    } else {
                        System.out.println("connected!");
                    }
                }
            }).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
