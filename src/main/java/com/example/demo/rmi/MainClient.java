package com.example.demo.rmi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * @author HP
 * @Description TODO
 * @date 2021/1/7-21:40
 */
public class MainClient {

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry(1099);
            //此处强转的类型是接口类型
            ISayServiceHello hello = (ISayServiceHello) registry.lookup("HelloRegistry");
            String andy = hello.say("andy");
            System.out.println("-----------"+andy+"----------------");
        } catch (NotBoundException | RemoteException e) {
            e.printStackTrace();
        }
    }
}
