package com.example.demo.designPatterns.proxy.v04;

import java.util.Random;

/**
 * @author HP
 * @Description 问题：我想记录坦克的移动时间
 * 最简单的办法，改变代码，记录时间
 * 问题2：如果无法改变方法源码
 * <p>
 * 用聚合的方式比继承的方式来做会比较灵活，设计模式的原则之一就是慎用继承。
 * 我们可以把多个代理类组合在一起。
 * 这个静态代理模式看上去是不是和装饰器模式 一样了，其实设计模式到最后都是多态的应用。他们只是在语义上有区分而已
 *
 * 问题：目前可以代理Movalbe了，但是如果我想要代理任何我想要代理的对象？怎么办？动态代理
 * 动态生成代理类
 * @date 2020/9/13-9:44
 */
public class Tank implements Movable {

    //模拟坦克移动了一段时间
    @Override
    public void move() {
        System.out.println("Tank moving claclacla...");
        try {
            Thread.sleep(new Random().nextInt(10000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new TankTimeProxy(new TankLogProxy(new Tank())).move();
    }
}

class TankTimeProxy implements Movable {

    Movable movable;

    public TankTimeProxy(Movable movable) {
        this.movable = movable;
    }

    @Override
    public void move() {
        long start = System.currentTimeMillis();
        movable.move();
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}

class TankLogProxy implements Movable {

    Movable movable;

    public TankLogProxy(Movable movable) {
        this.movable = movable;
    }

    @Override
    public void move() {
        System.out.println("start log....");
        movable.move();
        System.out.println("end log....");
    }
}

interface Movable {
    void move();
}
