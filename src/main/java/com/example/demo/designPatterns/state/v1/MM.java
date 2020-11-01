package com.example.demo.designPatterns.state.v1;

/**
 * @author HP
 * @Description 当增加新的状态时非常不方便
 * @date 2020/9/16-9:38
 */
public class MM {
    String name;
    private enum MMState {HAPPY,SAD};
    MMState state;


    public void smile() {

    }

    public void cry() {

    }

    public void say() {

    }
}
