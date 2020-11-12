package com.example.demo.zzl.rpcdemo.service;


/**
 * @author HP
 * @Description TODO
 * @date 2020/11/9-11:27
 */
public class MyFly implements Fly {
    @Override
    public void xxoo(String msg) {
        System.out.println("server,get client arg:"+msg);
    }
}
