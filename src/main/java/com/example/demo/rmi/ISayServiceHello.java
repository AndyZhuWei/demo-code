package com.example.demo.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author HP
 * @Description 待发布的服务接口需要集成Remote
 * @date 2021/1/7-17:24
 * 接口需要继承Remote
 */
public interface ISayServiceHello extends Remote {

    //必须抛出RemoteException异常
    String say(String key) throws RemoteException;
}
