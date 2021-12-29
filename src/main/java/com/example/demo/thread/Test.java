package com.example.demo.thread;

public class Test {
static Thread thread1 = new Thread(()-> {
System.out.println("thread1");
});
static Thread thread2 = new Thread(()-> {
System.out.println("thread2");
});
static Thread thread3 = new Thread(()-> {
System.out.println("thread3");
});
public static void main(String[] args) throws InterruptedException {
thread1.start();
thread1.join();
thread2.start();
thread2.join();
thread3.start();
}
}