package com.example.demo.zzl.rpcdemo.service;


/**
 * @author HP
 * @Description TODO
 * @date 2020/11/9-11:26
 */
public class MyCar implements Car {
    @Override
    public String ooxx(String msg) {
        System.out.println("server,get client arg:"+msg);
        return "server res "+ msg;
    }
}
