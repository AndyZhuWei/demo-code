package com.example.demo.designPatterns.proxy.v01;

import java.util.Random;

/**
 * @author HP
 * @Description 问题：我想记录坦克的移动时间
 * @date 2020/9/13-9:44
 */
public class Tank implements Movable{

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
}

interface Movable {
    void move();
}
