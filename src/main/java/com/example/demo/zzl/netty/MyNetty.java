package com.example.demo.zzl.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.*;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @author HP
 * @Description TODO
 * @date 2020/11/5-17:33
 */
public class MyNetty {


    @Test
    public void testByteBuf() {
       // ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer(8,20); 模式是非堆上分配  //buf.isDirect()  :true

        //Unpooled 非池化 再堆上分配
       // ByteBuf byteBuf = UnpooledByteBufAllocator.DEFAULT.heapBuffer(8, 20); //buf.isDirect()  :false

        //pooled 池化
        ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.heapBuffer(8, 20); //buf.isDirect()  :false

        print(byteBuf);

        byteBuf.writeBytes(new byte[]{1,2,3,4});
        print(byteBuf);

        byteBuf.writeBytes(new byte[]{1,2,3,4});
        print(byteBuf);

        byteBuf.writeBytes(new byte[]{1,2,3,4});
        print(byteBuf);

        byteBuf.writeBytes(new byte[]{1,2,3,4});
        print(byteBuf);

        byteBuf.writeBytes(new byte[]{1,2,3,4});
        print(byteBuf);

        byteBuf.writeBytes(new byte[]{1,2,3,4});
        print(byteBuf);


    }

    public static void print(ByteBuf buf){
        System.out.println("buf.isReadable()    :"+buf.isReadable());
        System.out.println("buf.readerIndex()   :"+buf.readerIndex());
        System.out.println("buf.readableBytes() "+buf.readableBytes());
        System.out.println("buf.isWritable()    :"+buf.isWritable());
        System.out.println("buf.writerIndex()   :"+buf.writerIndex());
        System.out.println("buf.writableBytes() :"+buf.writableBytes());
        System.out.println("buf.capacity()  :"+buf.capacity());
        System.out.println("buf.maxCapacity()   :"+buf.maxCapacity());
        System.out.println("buf.isDirect()  :"+buf.isDirect());
        System.out.println("--------------");
    }


    @Test
    public void loopExecutor() throws IOException {
        //group 线程池
        NioEventLoopGroup selector = new NioEventLoopGroup(2);//这个东西不是selector，它里边的线程会创建一个selector
        selector.execute(()->{
            try {
                for(;;) {
                    System.out.println("hello world001");
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        selector.execute(()->{
            try {
                for(;;) {
                    System.out.println("hello world002");
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        System.in.read();
    }

    @Test
    public void clientMode() throws InterruptedException {
        NioEventLoopGroup thread = new NioEventLoopGroup(1);

        //客户端模式：
        NioSocketChannel client = new NioSocketChannel();

        thread.register(client);//epoll_ctl(5,ADD,3)

        //响应式编程
        ChannelPipeline p = client.pipeline();
        p.addLast(new MyInHandler());


        //reactor 异步的特征
        ChannelFuture connect = client.connect(new InetSocketAddress("192.168.80.100",9090));
        //等待建立连接成功
        ChannelFuture sync = connect.sync();



        ByteBuf buf = Unpooled.copiedBuffer("hello server".getBytes());
        ChannelFuture send = client.writeAndFlush(buf);
        //等待发送成功
        send.sync();

        //等待关闭事件
        sync.channel().closeFuture().sync();

        System.out.println("client over");

    }


    // 再MyInHandler类上不加@ChannelHandler.Sharable时，多个客户端连接会报以下的错误。因为MyInHandler会再多个客户端中注册，而MyInHandler是单例对象，存在风险，故提示。
    // io.netty.channel.ChannelPipelineException: com.example.demo.zzl.netty.MyInHandler is not a @Sharable handler, so can't be added or removed multiple times.
    //我们可以显式再MyInHandler中加入这个注解来解决，但是
    //一般Handler都是用户自己实现的，我们不能保证用户再这个Handler中加入属性的操作，故加入注解的方式不能解决根本问题
    //所以@ChannelHandler.Sharable不应该强压给coder
    //那怎么解决？怎么可以让每次注册时都是new一个新的Handler？
    //可以设计一个没有业务功能的Handler，再找个handler的read中

    @Test
    public void serverMode() throws InterruptedException {

        NioEventLoopGroup thread = new NioEventLoopGroup(1);
        NioServerSocketChannel server = new NioServerSocketChannel();

        thread.register(server);


        //指不定什么时候家里来人，响应式
        ChannelPipeline p = server.pipeline();
        //p.addLast(new MyAcceptHandler(thread,new MyInHandler()));//accept接收客户端，并且注册到selector
        p.addLast(new MyAcceptHandler(thread,new ChannelInit()));//accept接收客户端，并且注册到selector
        ChannelFuture bind = server.bind(new InetSocketAddress("192.168.80.10", 9090));
        bind.sync().channel().closeFuture().sync();
        System.out.println("server close...");

    }

    @Test
    public void nettyClient() throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup(1);
        Bootstrap bs = new Bootstrap();
        ChannelFuture connect = bs.group(group)
                .channel(NioSocketChannel.class)
               // .handler(new ChannelInit())
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new MyInHandler());
                    }
                })
                .connect(new InetSocketAddress("192.168.80.100", 9090));
        Channel client = connect.sync().channel();

        ByteBuf buf = Unpooled.copiedBuffer("hello server".getBytes());
        ChannelFuture send = client.writeAndFlush(buf);
        //等待发送成功
        send.sync();

        client.closeFuture().sync();



    }


    @Test
    public void nettyServer() throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup(1);
        ServerBootstrap bs = new ServerBootstrap();
        ChannelFuture bind = bs.group(group, group)
                .channel(NioServerSocketChannel.class)
                //.childHandler(new ChannelInit())
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new MyInHandler());
                    }
                })
                .bind(new InetSocketAddress("192.168.80.10", 9090));
        bind.sync().channel().closeFuture().sync();
    }

}

//找个代码后边都是netty框架给我们实现，MyInHandler是coder自己实现的，所以需要以参数的形式传入进来
class MyAcceptHandler extends ChannelInboundHandlerAdapter {

    EventLoopGroup selector;
    ChannelHandler channelInitHandler;

    public MyAcceptHandler(EventLoopGroup thread,ChannelHandler channelInitHandler) {
        this.selector = thread;
        this.channelInitHandler=channelInitHandler;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("server registed...");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //listen socket accept client
        //socket        R/W
        SocketChannel client = (SocketChannel) msg;//accept 我怎么没调用额？

        //2.响应式的 handler
        client.pipeline().addLast(channelInitHandler);//1.client::pipeline[ChannelInitHandler,]

        //1.注册
        selector.register(client);
    }
}


//目前找个写法不优美，我们可以把ChannelInit找个写成抽象类，
//再抽象方法中让用户自己new MyInHandler对象
@ChannelHandler.Sharable
class ChannelInit extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        Channel client = ctx.channel();
        client.pipeline().addLast(new MyInHandler());//2.client::pipline[ChannelInitHandler,MyInHandler]
        client.pipeline().remove(this);//删除ChannelInitHandler，过河拆桥
    }
}




//@ChannelHandler.Sharable 用ChannelInit包装
class MyInHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client registed...");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client active...");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf =(ByteBuf)msg;
        //CharSequence str = buf.readCharSequence(buf.readableBytes(), CharsetUtil.UTF_8);
        CharSequence str = buf.getCharSequence(0,buf.readableBytes(), CharsetUtil.UTF_8);
        System.out.println(str);

        //问题代码，上边readCharSequence已经移动了readIndex了。
        //我们可以使用getCharSequence,这个不会移动readIndex
        ctx.writeAndFlush(buf);
    }
}
