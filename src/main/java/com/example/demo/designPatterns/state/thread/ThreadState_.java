package com.example.demo.designPatterns.state.thread;

/**
 * @author HP
 * @Description TODO
 * @date 2020/9/16-14:32
 */
public abstract class ThreadState_ {
    abstract void move(Action input);
    abstract void run();
}
