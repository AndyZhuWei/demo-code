package com.example.demo.rmi;


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author HP
 * @Description TODO
 * @date 2021/1/7-17:25
 */
public class MainServer {

    public static void main(String[] args) {
        try {

            //使用这种方式的话必须在接口实现类中继承UnicastRemoteObject
            way01();
            //不需要接口实现类继承UnicastRemoteObject
           // way02();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private  static void way01() throws RemoteException {
        //本地主机上的远程对象注册表Registry的实例，默认端口1099
        Registry registry = LocateRegistry.createRegistry(1099);
        //创建一个远程对象
        SayHelloServiceImpl sayHello = new SayHelloServiceImpl();
        //把远程对象注册到RMI注册服务器上，并命名为HelloRegistry
        registry.rebind("HelloRegistry",sayHello);
        System.out.println("======= 启动RMI服务成功! =======");
    }


    private  static void way02() throws RemoteException {
        //本地主机上的远程对象注册表Registry的实例，默认端口1099
        Registry registry = LocateRegistry.createRegistry(1099);
        //创建一个远程对象
        SayHelloServiceImpl sayHello = new SayHelloServiceImpl();

        Remote remote = UnicastRemoteObject.exportObject(sayHello, 6666);
        //把远程对象注册到RMI注册服务器上，并命名为HelloRegistry
        registry.rebind("HelloRegistry",remote);
        System.out.println("======= 启动RMI服务成功! =======");
    }
}
