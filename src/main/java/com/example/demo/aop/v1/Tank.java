package com.example.demo.aop.v1;

import java.util.Random;

/**
 * @author HP
 * @Description TODO
 * @date 2020/9/13-14:22
 */
public class Tank {

    public void move() {
        System.out.println("Tank moving claclacla...");
        try {
            Thread.sleep(new Random().nextInt(1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
