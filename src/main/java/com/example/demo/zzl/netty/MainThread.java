package com.example.demo.zzl.netty;

/**
 * @author HP
 * @Description TODO
 * @date 2020/11/4-10:29
 */
public class MainThread {

    public static void main(String[] args) {

        //这里不做关于IO 和  业务的事情
        //1,创建 IO Thread  （一个或者多个）
        SelectorThreadGroup boss = new SelectorThreadGroup(3);  //混杂模式：boss中的线程组即处理接收的事件又处理读写io事件

        SelectorThreadGroup worker = new SelectorThreadGroup(3);  //工作者线程

        boss.setWorkers(worker);





        boss.bind(9999);
        boss.bind(8888);
        boss.bind(7777);
    }
}
