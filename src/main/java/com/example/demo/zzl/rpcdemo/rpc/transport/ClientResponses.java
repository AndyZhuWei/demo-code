package com.example.demo.zzl.rpcdemo.rpc.transport;

import com.example.demo.zzl.rpcdemo.rpc.ResponseMappingCallBack;
import com.example.demo.zzl.rpcdemo.util.PackageMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author HP
 * @Description TODO
 * @date 2020/11/9-11:02
 */
public class ClientResponses extends ChannelInboundHandlerAdapter {

    //consumer......
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        PackageMsg responsePkg = (PackageMsg) msg;
        //曾经没有考虑返回的事情
        ResponseMappingCallBack.runCallback(responsePkg);
    }
}
