package com.example.demo.zzl.rpcdemo.rpc;


import com.example.demo.zzl.rpcdemo.util.PackageMsg;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author HP
 * @Description TODO
 * @date 2020/11/9-11:03
 */
public class ResponseMappingCallBack {
    static ConcurrentHashMap<Long, CompletableFuture> mapping = new ConcurrentHashMap<>();


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
