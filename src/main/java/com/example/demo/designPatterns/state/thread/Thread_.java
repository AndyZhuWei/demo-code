package com.example.demo.designPatterns.state.thread;

/**
 * @author HP
 * @Description TODO
 * @date 2020/9/16-14:22
 */
public class Thread_ {
    ThreadState_ state;

    void move(Action input) {
        state.move(input);
    }

    void run(){state.run();}
}
