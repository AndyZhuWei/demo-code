package com.example.demo.zzl.rpcdemo.rpc.transport;

import com.example.demo.zzl.rpcdemo.rpc.protocol.MyContent;
import com.example.demo.zzl.rpcdemo.rpc.protocol.MyHeader;
import com.example.demo.zzl.rpcdemo.util.PackageMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.List;

/**
 * @author HP
 * @Description TODO
 * @date 2020/11/9-10:59
 */
public class ServerDecode extends ByteToMessageDecoder {

    //父类里一定有channelRead{ 前老的拼buf decode(); 剩余留存；对out遍历}->bytebuf
    //因为你偷懒，自己能不能实现！
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf buf, List<Object> out) throws Exception {

        //可能有多个数据包，所以用while循环
        //netty channel的read不能保证数据的完整性
        //而且，不是一次read处理一个message，前后read能保证数据完整性
        //if(buf.readableBytes() >= 120) { //不能使用if，因为读取的msg可能是多个数据包的
        while (buf.readableBytes() >= 120) {
            byte[] bytes = new byte[120];
            //readBytes会移动读指针
            // buf.readBytes(bytes);
            //从哪里读取，读多少，但是readindex不变
            buf.getBytes(buf.readerIndex(), bytes);
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            MyHeader myHeader = (MyHeader) ois.readObject();
          //  System.out.println("server response @ id:" + myHeader.getRequestID());

            //DECODE在2个方向都使用
            //通信的协议
            if (buf.readableBytes() >= myHeader.getDataLen()+120) {
                //如果剩余的
                buf.readBytes(120);//移动指针到body开始的为止
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
