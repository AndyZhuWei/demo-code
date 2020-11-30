package com.example.demo.zzl.netty;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author HP
 * @Description TODO
 * @date 2020/11/4-10:30
 */
public class SelectorThread implements Runnable {

    Selector selector = null;

    SelectorThreadGroup stg = null;

    LinkedBlockingQueue<Channel> lbq = new LinkedBlockingQueue<>();

    public SelectorThread(SelectorThreadGroup stg) {
        try {
            selector = Selector.open();
            this.stg = stg;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void run() {
        //Loop
        while(true) {
            try {
                //1.select()
               // System.out.println(Thread.currentThread().getName()+" before "+ selector.keys().size());
                int nums = selector.select(); //阻塞 wakeup() 如果在其之后register，这个select()是不能监控，需要在下一次select()之前注册上，才会监控
                //Thread.sleep(1000);//这决对不是解决方案，我只是给你演示
               // System.out.println(Thread.currentThread().getName()+" after "+ selector.keys().size());

                //2.处理selectkeys
                if(nums > 0) {
                    Set<SelectionKey> keys = selector.selectedKeys();
                    Iterator<SelectionKey> iter = keys.iterator();
                    while(iter.hasNext()) { //线程处理的过程
                        SelectionKey key = iter.next();
                        iter.remove();
                        if(key.isAcceptable()) {
                            acceptHandler(key);
                        } else if(key.isReadable()) {
                            readHandler(key);
                        } else if(key.isWritable()) {

                        }
                    }
                }

                //3.处理一些task
                if(!lbq.isEmpty()) {
                    Channel c = lbq.take();
                    if(c instanceof ServerSocketChannel) {
                        System.out.println(Thread.currentThread().getName()+" accept");
                        ServerSocketChannel server = (ServerSocketChannel)c;
                        server.register(selector,SelectionKey.OP_ACCEPT);
                    } else if(c instanceof SocketChannel) {
                        System.out.println(Thread.currentThread().getName()+" read");
                        SocketChannel client = (SocketChannel)c;
                        ByteBuffer buffer = ByteBuffer.allocateDirect(4098);
                        client.register(selector,SelectionKey.OP_READ,buffer);
                    }
                }


            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private void readHandler(SelectionKey key) {
        System.out.println(Thread.currentThread().getName()+"  read......");
        ByteBuffer buffer = (ByteBuffer)key.attachment();
        SocketChannel client = (SocketChannel) key.channel();
        buffer.clear();
        while(true) {
            try {
                int num = client.read(buffer);
                if(num > 0) {
                    buffer.flip();//将读到的内容翻转，然后直接写出
                    while(buffer.hasRemaining()) {
                        client.write(buffer);
                    }
                    buffer.clear();
                } else if(num == 0) {
                    break;
                } else if(num < 0) {
                    //客户端断开了
                    System.out.println("client: "+client.getRemoteAddress()+" closed......");
                    key.cancel();
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void acceptHandler(SelectionKey key) {
        System.out.println(Thread.currentThread().getName()+" acceptHandler......");

        ServerSocketChannel server = (ServerSocketChannel) key.channel();
        try {
            SocketChannel client = server.accept();
            client.configureBlocking(false);

            //choose a selector and register!
            //stg.nextSelector(client);

            //stg.nextSelectorV2(client);

            stg.nextSelectorV3(client);


        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
