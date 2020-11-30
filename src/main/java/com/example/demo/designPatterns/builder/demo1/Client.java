package com.example.demo.designPatterns.builder.demo1;

/**
 * @Author: zhuwei
 * @Date:2020/3/11 9:50
 * @Description:
 */
public class Client {

    public static void main(String[] args) {
        Computer computer = new Computer.Builder("因特儿","三星")
                .setDisplay("三星24寸")
                .setKeyboard("罗技")
                .setUsbCount(2)
                .build();
        System.out.println(computer);
    }
}
