package com.example.demo.designPatterns.factory;

/**
 * @author HP
 * @Description TODO
 * @date 2020/9/9-21:59
 */
public class Main {
    public static void main(String[] args) {
//        Car c = new Car();
//        c.go();
//        Plane p = new Plane();
//        p.go();
        //交通工具很多，老是变动，很不灵活，所以我们建立一个父接口
        //Movable，让car和plane都实现它,则客户调用如下：
        Movable movable = new Plane();
        movable.go();



    }
}
