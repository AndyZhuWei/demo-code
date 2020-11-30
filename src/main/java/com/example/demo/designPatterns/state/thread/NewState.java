package com.example.demo.designPatterns.state.thread;

/**
 * @author HP
 * @Description TODO
 * @date 2020/9/16-14:26
 */
public class NewState extends ThreadState_{
    private Thread_ t;


    public NewState(Thread_ t) {
        this.t=t;
    }

    @Override
    void move(Action input) {
        if(input.msg == "start") {
            t.state= new RunningState(t);
        }
    }

    @Override
    void run() {

    }
}
