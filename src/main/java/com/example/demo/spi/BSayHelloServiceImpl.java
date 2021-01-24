package com.example.demo.spi;


/**
 * @author HP
 * @Description TODO
 * @date 2021/1/13-14:23
 */
public class BSayHelloServiceImpl implements ISayHelloService {

    @Override
    public String say() {
        return "B say ";
    }
}
