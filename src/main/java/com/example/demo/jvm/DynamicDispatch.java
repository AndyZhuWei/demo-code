package com.example.demo.jvm;

public class DynamicDispatch {
    static abstract class Human {
        abstract void sayHello();
    }
    
    static class Man extends Human {
        @Override
        void sayHello() {
            System.out.println("hello,man");
        }
    }
    
    static class Woman extends Human {
        @Override
        void sayHello() {
            System.out.println("hello,woman");
        }
    }
    
    public static void main(String[] args){
      Human man = new Man();
      Human woman = new Woman();
      man.sayHello();
      woman.sayHello();
      man = new Woman();
      man.sayHello();
    }
}