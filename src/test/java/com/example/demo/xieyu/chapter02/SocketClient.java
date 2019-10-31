package com.example.demo.xieyu.chapter02;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * @Author: zhuwei
 * @Date:2019/10/25 9:15
 * @Description:
 *
 * 常见异常：
 * 情况1: 如果服务方（这里指提供数据的一方）已经将对应的Socket关闭，而这里是正常关闭Socket操作，
 *       请求方再发起多次writeLine()操作时，通常会抛出异常：java.net.SocketException: Software caused connection abort: socket write error
 *       当客户端关闭时，服务器端尝试读取客户端的一行数据也会报出这样的错误。此时发起readLine()操作，则通常读取到的是null
 * 情况2：如果服务方的进程直接退出，请求方使用了BufferedReader或BufferedWriter,请求方发起readLine()\WriteLine()操作时则会出现java.net.SocketException异常，
 *      异常的message通常有两种：请求方发起writeLine()操作得到的结果是：“Connection reset by peer:socket write error”
 *      而发起readLine()操作得到的结果是“Connection reset”
 *  情况3：当通过read()方法读取一个字节时，乳沟对方已经关闭，则会返回-1，如果通过一些java提供的流包装处理类中类似与readByte()的方法时（如DataInputStream），则会
 *      抛出Java.io.EOFException异常
 * 情况4：如果BufferedReader/BufferedWriter自己本身已经关闭了，但还在做read\write操作，就会从内部的一个ensureOpen()方法中抛出java.io.IOException异常，异常
 * 的message信息为“Stream closed”
 */
public class SocketClient {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        SocketWrapper socketWrapper =
                new SocketWrapper(new Socket("localhost",8888));
        try {
            System.out.println("已经连接上服务器端，现在可以输入数据开始通信了");
            String sendMsg = scanner.nextLine();
            socketWrapper.writeLine(sendMsg);//发送消息
            String recivedMsg = socketWrapper.readLine();
            while(!"close".equals(recivedMsg)) {
                System.out.println("===【服务器返回】===>"+recivedMsg);
                sendMsg = scanner.nextLine();
                socketWrapper.writeLine(sendMsg);//发送消息
                recivedMsg = socketWrapper.readLine();
            }
           // 这部分代码如果放开注释会报错
         /*   socketWrapper.writeLine("aaaa");
            socketWrapper.writeLine("aaaa");
            socketWrapper.writeLine("aaaa");*/
            System.out.println("我是客户端，结束了！");
        } finally {
            if(socketWrapper!=null){
                socketWrapper.close();
            }
        }
    }
}
