package com.example.demo.spi;



import java.rmi.RemoteException;

/**
 * @author HP
 * @Description TODO
 * @date 2021/1/13-14:23
 */
public class ASayHelloServiceImpl implements ISayHelloService {

    @Override
    public String say() {
        return "A say ";
    }
}
