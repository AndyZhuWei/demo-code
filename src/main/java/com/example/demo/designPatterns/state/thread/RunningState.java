package com.example.demo.designPatterns.state.thread;

/**
 * @author HP
 * @Description TODO
 * @date 2020/9/16-14:26
 */
public class RunningState extends ThreadState_ {
    private Thread_ t;

    public RunningState(Thread_ t) {
        this.t = t;
    }

    void move(Action input) {
    }

    void run() {

    }
}
