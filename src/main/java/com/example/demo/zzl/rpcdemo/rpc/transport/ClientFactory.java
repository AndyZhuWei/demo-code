package com.example.demo.zzl.rpcdemo.rpc.transport;


import com.example.demo.zzl.rpcdemo.rpc.ResponseMappingCallBack;
import com.example.demo.zzl.rpcdemo.rpc.protocol.MyContent;
import com.example.demo.zzl.rpcdemo.rpc.protocol.MyHeader;
import com.example.demo.zzl.rpcdemo.util.SerDerUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * @author HP
 * @Description TODO
 * @date 2020/11/9-10:53
 */
public class ClientFactory {

    int poolSize = 10;
    Random rand = new Random();
    NioEventLoopGroup clientWorkers;


    private static final ClientFactory factory;

    private ClientFactory(){}

    static {
        factory = new ClientFactory();
    }

    public static ClientFactory getFactory() {
        return factory;
    }


    public static CompletableFuture<Object> transport(MyContent content) {
        //content  就是货物  现在可以用自定义的rpc传输协议（有状态），也可以用http协议作为载体传输
        //我们先手工用了http协议作为载体，那这样是不是代表我们未来可以让provider是一个tomcat  jetty 基于http协议的一个容器
        //有无状态来自于你使用的什么协议，那么http协议肯定是无状态，每请求对应一个连接
        //dubbo 是一个rpc框架  netty 是一个io框架
        //dubbo中传输协议上，可以是自定义的rpc传输协议，http协议

        String type = "rpc";
       // String type = "http";
        CompletableFuture<Object> res = new CompletableFuture<>();

        if(type.equals("rpc")) {
            byte[] msgBody = SerDerUtil.ser(content);
            MyHeader header = MyHeader.createHeader(msgBody);
            byte[] msgHeader = SerDerUtil.ser(header);
            System.out.println("main:::"+ msgHeader.length);

            NioSocketChannel clientChannel = factory.getClient(new InetSocketAddress("localhost", 9090));


            ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.directBuffer(msgHeader.length + msgBody.length);
            long id = header.getRequestID();

            ResponseMappingCallBack.addCallBack(id,res);
            byteBuf.writeBytes(msgHeader);
            byteBuf.writeBytes(msgBody);
            clientChannel.writeAndFlush(byteBuf);
        }

        return res;
    }




    //一个consumer 可以连接很多的provider，每一个provider都有自己的pool K,V
    ConcurrentHashMap<InetSocketAddress, ClientPool> outboxs = new ConcurrentHashMap<>();

    public  NioSocketChannel getClient(InetSocketAddress address) {

        //TODO 在并发情况下一定要谨慎
        ClientPool clientPool = outboxs.get(address);
        if(clientPool == null) {
            synchronized (outboxs) {
                if(clientPool == null) {
                    outboxs.putIfAbsent(address,new ClientPool(poolSize));
                    clientPool = outboxs.get(address);
                }
            }
        }

        int i = rand.nextInt(poolSize);

        if(clientPool.clients[i] != null && clientPool.clients[i].isActive()) {
            return clientPool.clients[i];
        } else {
            synchronized (clientPool.lock[i]) {
                if(clientPool.clients[i] == null || !clientPool.clients[i].isActive()) {
                    return clientPool.clients[i] = create(address);
                }

            }
        }
        return  clientPool.clients[i] ;
    }

    private NioSocketChannel create(InetSocketAddress address) {
        //基于Netty的客户端
        clientWorkers = new NioEventLoopGroup(1);
        Bootstrap bootstrap = new Bootstrap();
        ChannelFuture connect = bootstrap.group(clientWorkers)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()
                                .addLast(new ServerDecode())
                                .addLast(new ClientResponses());//解决给谁的
                    }
                })
                .connect(address);
        try {
            NioSocketChannel client = (NioSocketChannel)connect.sync().channel();
            return client;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }




}
