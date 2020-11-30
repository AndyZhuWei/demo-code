package com.example.demo.designPatterns.state.thread;

/**
 * @author HP
 * @Description TODO
 * @date 2020/9/16-14:26
 */
public class TerminatedState extends ThreadState_{
    private Thread_ t;

    public TerminatedState(Thread_ t) {
        this.t=t;
    }

    void move(Action input) {
        if(input.msg == "start") {
            t.state= new TerminatedState(t);
        }
    }

    void run() {
        
    }
}
