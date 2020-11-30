package com.example.demo.designPatterns.state.v2;

/**
 * @author HP
 * @Description 当增加新的状态时非常不方便
 * @date 2020/9/16-9:38
 */
public class MM {
    String name;
    MMState state;


    public void smile() {
        state.smile();
    }

    public void cry() {
        state.cry();
    }

    public void say() {
        state.say();
    }
}
