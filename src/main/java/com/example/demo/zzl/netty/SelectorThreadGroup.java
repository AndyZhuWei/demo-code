package com.example.demo.zzl.netty;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Channel;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author HP
 * @Description TODO
 * @date 2020/11/4-11:00
 */
public class SelectorThreadGroup {

    ServerSocketChannel server=null;

    SelectorThread[] sts;

    AtomicInteger xid = new AtomicInteger(0);

    SelectorThreadGroup workers = null;

    public void setWorkers(SelectorThreadGroup workers) {
        this.workers = workers;
    }

    SelectorThreadGroup(int num) {
        //num 线程数
        sts = new SelectorThread[num];
        for(int i=0;i<num;i++) {
            sts[i]=new SelectorThread(this);

            new Thread(sts[i]).start();
        }
    }


    public void bind(int port) {

        try {
            server = ServerSocketChannel.open();
            server.configureBlocking(false);
            server.bind(new InetSocketAddress(port));

            //注册到哪个selector上呢？
            //混杂模式
            //nextSelector(server);

            //0号线程处理接收事件，其余的处理读写io
            //nextSelectorV2(server);

            nextSelectorV3(server);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void nextSelector(Channel c) {
        SelectorThread st = next();

        //1.通过队列传递数据 消息
        st.lbq.add(c);
        //2.通过打断阻塞，让对应的线程去完成注册
        st.selector.wakeup();





        //重点：c有可能是server 有可能是client
       /* ServerSocketChannel s = (ServerSocketChannel)c;
        //呼应上， int nums = selector.select(); //阻塞 wakeup()
        try {
           // System.out.println(Thread.currentThread().getName()+" before wakeup");
            st.selector.wakeup();//功能是让selector的select()方法，立刻返回，不阻塞
           // System.out.println(Thread.currentThread().getName()+" after wakeup");
          //  System.out.println(Thread.currentThread().getName()+" before register");
            s.register(st.selector, SelectionKey.OP_ACCEPT);//会被阻塞的！！！！
           // System.out.println(Thread.currentThread().getName()+" after register");
            //以上代码中wakeup()和register()哪个放前面都有问题。
        } catch (ClosedChannelException e) {
            e.printStackTrace();
        }*/




    }

    //将0号线程用作接收事件，其他线程处理读写io
    public void nextSelectorV2(Channel c) {

        if(c instanceof ServerSocketChannel) {
            SelectorThread st = sts[0];
            //1.通过队列传递数据 消息
            st.lbq.add(c);
            //2.通过打断阻塞，让对应的线程去完成注册
            st.selector.wakeup();
        } else {
            SelectorThread st = nextV2();

            //1.通过队列传递数据 消息
            st.lbq.add(c);
            //2.通过打断阻塞，让对应的线程去完成注册
            st.selector.wakeup();
        }


    }

    //boss线程组处理accept,worker处理读写io事件
    public void nextSelectorV3(Channel c) {
        if(c instanceof ServerSocketChannel) {
            SelectorThread st = next();
            //1.通过队列传递数据 消息
            st.lbq.add(c);
            //2.通过打断阻塞，让对应的线程去完成注册
            st.selector.wakeup();
        } else {
            SelectorThread st = this.workers.next();

            //1.通过队列传递数据 消息
            st.lbq.add(c);
            //2.通过打断阻塞，让对应的线程去完成注册
            st.selector.wakeup();
        }


    }

    //无论serversocket socket都复用这个方法
    private SelectorThread next() {
        int index = xid.incrementAndGet() % sts.length;
        return sts[index];
    }

    //将0号线程用作接收事件，其他线程处理读写io
    private SelectorThread nextV2() {
        int index = xid.incrementAndGet() % (sts.length-1);
        return sts[index+1];
    }
}
