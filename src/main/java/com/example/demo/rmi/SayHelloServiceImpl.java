package com.example.demo.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author HP
 * @Description TODO
 * @date 2021/1/7-17:25
 * 需要继承UnicastRemoteObject或者不继承在MainService中使用UnicastRemoteObject.exportObject(sayHello,6666);
 */
public class SayHelloServiceImpl implements ISayServiceHello {

    protected SayHelloServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public String say(String key) {
        return "Hello "+key;
    }
}
