package com.example.demo.zzl.rpcdemo.rpc.protocol;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author HP
 * @Description TODO
 * @date 2020/11/9-10:50
 */
public class MyHeader implements Serializable {
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
