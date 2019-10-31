package com.example.demo.nio;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author: zhuwei
 * @Date:2018/9/28 15:30
 * @Description:
 */
public class NIOServer implements Runnable {

    private ByteBuffer byteBuffer;

    private Selector selector;

    private ServerSocketChannel serverSocketChannel;

    public NIOServer() throws IOException {
        this.byteBuffer = ByteBuffer.allocate(1024);
        this.selector = Selector.open();
        this.serverSocketChannel = ServerSocketChannel.open();
        this.serverSocketChannel.bind(new InetSocketAddress(8888));
        this.serverSocketChannel.configureBlocking(false);
        this.serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("服务器已启动");
    }

    @Override
    public void run() {
        System.out.println("run");
        while(true) {
            try {
                this.selector.select();

                Set<SelectionKey> selectionKeys =  this.selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while(iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();
                    if(selectionKey.isValid()) {
                        if(selectionKey.isAcceptable()) {
                            System.out.println("isAcceptable");
                            serverSocketChannel = (ServerSocketChannel)selectionKey.channel();
                            SocketChannel socketChannel = serverSocketChannel.accept();
                            socketChannel.configureBlocking(false);
                            System.out.println("socketChannel");
                            socketChannel.register(selector,SelectionKey.OP_READ);
                        }

                        if(selectionKey.isConnectable()) {
                            System.out.println("isConnectable");
                        }

                        if(selectionKey.isReadable()) {
                            System.out.println("isReadable");
                            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                            socketChannel.configureBlocking(false);
                            socketChannel.read(byteBuffer);
                            this.byteBuffer.flip();

                            byte[] bytes = new byte[byteBuffer.remaining()];
                            byteBuffer.get(bytes);
                            System.out.println(new String(bytes));
                        }

                        if(selectionKey.isWritable()) {
                            System.out.println("isWritable");
                        }
                    }


                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) throws IOException {
        new Thread(new NIOServer()).start();
    }
}
