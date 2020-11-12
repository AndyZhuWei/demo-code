package com.example.demo.zzl.netty;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author HP
 * @Description TODO
 * @date 2020/11/6-11:01
 */
public class MyTest {


    @Test
    public void test01() throws IOException, InterruptedException {
        ServerSocketChannel server = ServerSocketChannel.open();
        Selector selector = Selector.open();

        server.configureBlocking(false);
        server.bind(new InetSocketAddress("192.168.80.10",9090));
        server.register(selector, SelectionKey.OP_ACCEPT);

        while(true) {
            final Set<SelectionKey> keys = selector.keys();
            System.out.println(keys.size()+"  keys");
            int select = selector.select();
            System.out.println(select+"  select");
            Thread.sleep(1000);
        }
    }
}
