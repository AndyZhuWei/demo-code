package com.example.demo.zzl.rpcdemo.rpc.transport;

import com.example.demo.zzl.rpcdemo.rpc.Dispatcher;
import com.example.demo.zzl.rpcdemo.rpc.protocol.MyContent;
import com.example.demo.zzl.rpcdemo.rpc.protocol.MyHeader;
import com.example.demo.zzl.rpcdemo.util.PackageMsg;
import com.example.demo.zzl.rpcdemo.util.SerDerUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author HP
 * @Description TODO
 * @date 2020/11/9-11:32
 */
public class ServerRequestHandler extends ChannelInboundHandlerAdapter {

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

                String serviceName = requestPkg.getContent().getName();
                String method = requestPkg.getContent().getMethod();
                Object c = dis.get(serviceName);
                Class<?> clazz = c.getClass();
                Object res = null;
                try {
                    Method m = clazz.getMethod(method, (requestPkg.getContent().getParameterTypes()));
                    res = m.invoke(c, requestPkg.getContent().getArgs());
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }


                String execThreadName = Thread.currentThread().getName();
                MyContent content = new MyContent();
               // String s = "io thread:" + ioThreadName + " exec thread:" + execThreadName + " from args:" + requestPkg.getContent().getArgs()[0];
                // System.out.println(s);
                //content.setRes(s);
                content.setRes((String)res);
                byte[] contentByte = SerDerUtil.ser(content);



                MyHeader resHeader = new MyHeader();
                resHeader.setRequestID(requestPkg.getMyHeader().getRequestID());
                resHeader.setFlag(0x14141424);
                resHeader.setDataLen(contentByte.length);
                byte[] headerByte = SerDerUtil.ser(resHeader);

                ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.directBuffer(headerByte.length+contentByte.length);

                byteBuf.writeBytes(headerByte);
                byteBuf.writeBytes(contentByte);
                ctx.writeAndFlush(byteBuf);

            }
        });
    }
}
