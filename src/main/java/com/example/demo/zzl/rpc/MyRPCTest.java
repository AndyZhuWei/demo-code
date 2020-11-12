package com.example.demo.zzl.rpc;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.junit.Test;

import java.io.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
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

    @Test
    public void startServer() {
        MyCar car = new MyCar();
        MyFly fly = new MyFly();
        Dispatcher dis = new Dispatcher();
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

        new Thread(() -> {
            startServer();
        }).start();
        System.out.println("server started......");

        AtomicInteger num = new AtomicInteger(0);
        int size = 20;
        Thread[] threads = new Thread[size];
        for (int i = 0; i < size; i++) {
            threads[i] = new Thread(() -> {
                Car car = proxyGet(Car.class);//动态代理实现
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


    public static <T> T proxyGet(Class<T> interfaceInfo) {
        //实现各个版本的动态代理

        ClassLoader classLoader = interfaceInfo.getClassLoader();
        Class<?>[] interfaces = {interfaceInfo};


        return (T) Proxy.newProxyInstance(classLoader, interfaces, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //如何设计我们的consumer的对于provider的调用过程

                //1.调用方法、参数、服务 ==》 封装成message
                String name = interfaceInfo.getName();
                String methodName = method.getName();
                Class<?>[] parameterTypes = method.getParameterTypes();


                MyContent myContent = new MyContent(name, methodName, parameterTypes, args);
                ByteArrayOutputStream bas = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bas);
                oos.writeObject(myContent);
                byte[] msgBody = bas.toByteArray();
                //2.在通信的过程中有可能是一个连接或2个连接并发访问（不是绑定在io上），所以发过去再发过来就不知道是谁的请求了
                //所以还需要设计一个RequestID+msg  本地要缓存RequestID
                //协议：【header<>】【msgBody】
                MyHeader header = createHeader(msgBody);

                bas.reset();
                oos = new ObjectOutputStream(bas);
                oos.writeObject(header);
                byte[] msgHeader = bas.toByteArray();

                //System.out.println("msgHeader.length:"+msgHeader.length);

                //3.连接池：取得连接
                ClientFactory factory = ClientFactory.getFactory();
                NioSocketChannel clientChannel = factory.getClient(new InetSocketAddress("localhost", 9090));
                //获取连接过程中： 开始-创建 ，过程-直接取

                //4.发送 --> 走IO out 走netty(netty是event事件驱动的)

                ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.directBuffer(msgHeader.length + msgBody.length);

                CountDownLatch countDownLatch = new CountDownLatch(1);
                long id = header.getRequestID();
                CompletableFuture<String> res = new CompletableFuture<>();

                //用CompletableFuture替代
//                ResponseMappingCallBack.addCallBack(id, new Runnable() {
//                    @Override
//                    public void run() {
//                        countDownLatch.countDown();
//                    }
//                });


                ResponseMappingCallBack.addCallBack(id,res);



                byteBuf.writeBytes(msgHeader);
                byteBuf.writeBytes(msgBody);
                ChannelFuture channelFuture = clientChannel.writeAndFlush(byteBuf);
                channelFuture.sync();//IO是双向的，你看似有个sync，她仅代表out


                //用CompletableFuture替代
                //countDownLatch.await();

                //5.?如果从IO未来回来了，怎么讲代码执行到这里
                //（睡眠/回调，如何让线程停下来，你还能让他继续）


                return res.get();
            }
        });
    }


    public static MyHeader createHeader(byte[] msg) {
        MyHeader myHeader = new MyHeader();
        int size = msg.length;
        int f = 0x14141414;
        long requestID = Math.abs(UUID.randomUUID().getLeastSignificantBits());
        //0x14 0001 0100
        myHeader.setFlag(f);
        myHeader.setRequestID(requestID);
        myHeader.setDataLen(size);
        return myHeader;
    }
}

class ServerDecode extends ByteToMessageDecoder {

    //父类里一定有channelRead{ 前老的拼buf decode(); 剩余留存；对out遍历}->bytebuf
    //因为你偷懒，自己能不能实现！
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf buf, List<Object> out) throws Exception {

        //可能有多个数据包，所以用while循环
        //netty channel的read不能保证数据的完整性
        //而且，不是一次read处理一个message，前后read能保证数据完整性
        //if(buf.readableBytes() >= 103) { //不能使用if，因为读取的msg可能是多个数据包的
        while (buf.readableBytes() >= 103) {
            byte[] bytes = new byte[103];
            //readBytes会移动读指针
            // buf.readBytes(bytes);
            //从哪里读取，读多少，但是readindex不变
            buf.getBytes(buf.readerIndex(), bytes);
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            MyHeader myHeader = (MyHeader) ois.readObject();
            System.out.println("server response @ id:" + myHeader.getRequestID());

            //DECODE在2个方向都使用
            //通信的协议
            if (buf.readableBytes() >= myHeader.getDataLen()+103) {
                //如果剩余的
                buf.readBytes(103);//移动指针到body开始的为止
                byte[] data = new byte[(int) myHeader.getDataLen()];
                buf.readBytes(data);
                ByteArrayInputStream din = new ByteArrayInputStream(data);
                ObjectInputStream doin = new ObjectInputStream(din);
                if(myHeader.getFlag() == 0x14141414) {
                    MyContent content = (MyContent) doin.readObject();
                    out.add(new PackageMsg(myHeader, content));
                } else if(myHeader.getFlag() == 0x14141424){
                    MyContent content = (MyContent) doin.readObject();
                    out.add(new PackageMsg(myHeader, content));
                }
            } else {
                break;
            }

        }
    }
}

class ServerRequestHandler extends ChannelInboundHandlerAdapter {

    Dispatcher dis;
    //provider:

    public ServerRequestHandler(Dispatcher dis) {
        this.dis=dis;
    }



    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //这个msg不一定是ByteBuf要看前边处理的解码器最后一个参数中封装的是什么对象
        //在没有写解密器前我们可以像下边这样处理，有了serverDecoder我们就不用下边这种写法
        //ByteBuf buf = (ByteBuf)msg;
        PackageMsg requestPkg = (PackageMsg) msg;

       // System.out.println("server handler:"+requestPkg.content.getArgs()[0]);


        //如果假设处理完了，要给客户端返回了
        //你需要注意哪些环节

        //bytebuf
        //因为是个RPC吗，你的有requestID
        //在client那一侧也要解决解码问题

        //关注rpc通信协议，来的时候flag 0x14141414

        //有新的header+content
        String ioThreadName = Thread.currentThread().getName();
        //1.直接在当前方法处理IO和业务和返回

        //2.自己创建线程池

        //3.使用netty自己的eventloop来处理业务及返回
        //ctx.executor().execute(new Runnable() {
        ctx.executor().parent().next().execute( new Runnable() {
            @Override
            public void run() {

                String serviceName = requestPkg.content.getName();
                String method = requestPkg.content.getMethod();
                Object c = dis.get(serviceName);
                Class<?> clazz = c.getClass();
                Object res = null;
                try {
                    Method m = clazz.getMethod(method, (requestPkg.content.getParameterTypes()));
                    res = m.invoke(c, requestPkg.content.getArgs());
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }


                String execThreadName = Thread.currentThread().getName();
                MyContent content = new MyContent();
                String s = "io thread:" + ioThreadName + " exec thread:" + execThreadName + " from args:" + requestPkg.getContent().getArgs()[0];
               // System.out.println(s);
                //content.setRes(s);
                content.setRes((String)res);
                byte[] contentByte = SerDerUtil.ser(content);



                MyHeader resHeader = new MyHeader();
                resHeader.setRequestID(requestPkg.getMyHeader().getRequestID());
                resHeader.setFlag(0x14141424);
                resHeader.setDataLen(contentByte.length);
                byte[] headerByte = SerDerUtil.ser(resHeader);

                ByteBuf  byteBuf = PooledByteBufAllocator.DEFAULT.directBuffer(headerByte.length+contentByte.length);

                byteBuf.writeBytes(headerByte);
                byteBuf.writeBytes(contentByte);
                ctx.writeAndFlush(byteBuf);

            }
        });











    }
}


class ResponseMappingCallBack {
    static ConcurrentHashMap<Long,CompletableFuture> mapping = new ConcurrentHashMap<>();


    public static void addCallBack(long requestId,CompletableFuture cb) {
        mapping.putIfAbsent(requestId,cb);
    }

    public static void runCallback(PackageMsg packageMsg) {
        long requestid = packageMsg.getMyHeader().getRequestID();
        String res = packageMsg.getContent().getRes();
        CompletableFuture cf = mapping.get(requestid);
        cf.complete(res);
        removeCB(requestid);
    }

    public static void removeCB(long requestid) {
        mapping.remove(requestid);
    }
}

//源于 spark 源码
class ClientFactory {

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

    //一个consumer 可以连接很多的provider，每一个provider都有自己的pool K,V
    ConcurrentHashMap<InetSocketAddress,ClientPool> outboxs = new ConcurrentHashMap<>();

    public synchronized NioSocketChannel getClient(InetSocketAddress address) {
        ClientPool clientPool = outboxs.get(address);
        if(clientPool == null) {
            outboxs.putIfAbsent(address,new ClientPool(poolSize));
            clientPool = outboxs.get(address);
        }

        int i = rand.nextInt(poolSize);

        if(clientPool.clients[i] != null && clientPool.clients[i].isActive()) {
            return clientPool.clients[i];
        }

        synchronized (clientPool.lock[i]) {
            return clientPool.clients[i] = create(address);
        }





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
                                .addLast(new ClientResponse());//解决给谁的
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

class ClientResponse extends ChannelInboundHandlerAdapter {

    //consumer......
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        PackageMsg responsePkg = (PackageMsg) msg;
        //曾经没有考虑返回的事情
        ResponseMappingCallBack.runCallback(responsePkg);
    }
}

class ClientPool {
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

class MyHeader implements Serializable{
    /*
     * 通信上的协议
     * 1.ooxx值 在某个二进制位表示我这个是一个什么协议
     * 2.UUID:requestID
     * 3.DATA_LEN
     *
     */
    int flag;//32bit可以设置很多信息
    long requestID;
    long dataLen;


    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public long getRequestID() {
        return requestID;
    }

    public void setRequestID(long requestID) {
        this.requestID = requestID;
    }

    public long getDataLen() {
        return dataLen;
    }

    public void setDataLen(long dataLen) {
        this.dataLen = dataLen;
    }





}



class MyContent implements Serializable {

    String name;
    String method;
    Class<?>[] parameterTypes;
    Object[] args;
    String res;

    public MyContent(){

    }


    public MyContent(String name, String method, Class<?>[] parameterTypes, Object[] args) {
        this.name = name;
        this.method = method;
        this.parameterTypes = parameterTypes;
        this.args = args;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public void setRes(String res) {
        this.res=res;
    }

    public String getRes() {
        return res;
    }
}

interface Car {
    String ooxx(String msg);
}

class MyCar implements Car {
    @Override
    public String ooxx(String msg) {
        System.out.println("server,get client arg:"+msg);
        return "server res "+ msg;
    }
}

interface Fly {
    void xxoo(String msg);
}

class MyFly implements Fly {
    @Override
    public void xxoo(String msg) {
        System.out.println("server,get client arg:"+msg);
    }
}

class Dispatcher {
    public static ConcurrentHashMap<String,Object> invokeMap = new ConcurrentHashMap<>();

    public void register(String k,Object object) {
        invokeMap.put(k,object);
    }

    public Object get(String k) {
        return invokeMap.get(k);
    }
}