package com.example.demo.designPatterns.bridge.v4;

/**
 * @author HP
 * @Description TODO
 * @date 2020/9/15-9:38
 */
public class WildGift extends Gift {
    public WildGift(GiftImpl impl) {
        this.impl=impl;
    }
}
