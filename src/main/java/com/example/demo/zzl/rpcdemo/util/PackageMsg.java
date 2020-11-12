package com.example.demo.zzl.rpcdemo.util;


import com.example.demo.zzl.rpcdemo.rpc.protocol.MyContent;
import com.example.demo.zzl.rpcdemo.rpc.protocol.MyHeader;

/**
 * @author HP
 * @Description TODO
 * @date 2020/11/6-23:51
 */
public class PackageMsg {


    MyHeader myHeader;
    MyContent content;
    public PackageMsg(MyHeader myHeader, MyContent content) {
        this.myHeader=myHeader;
        this.content=content;
    }

    public MyHeader getMyHeader() {
        return myHeader;
    }

    public void setMyHeader(MyHeader myHeader) {
        this.myHeader = myHeader;
    }

    public MyContent getContent() {
        return content;
    }

    public void setContent(MyContent content) {
        this.content = content;
    }



}
