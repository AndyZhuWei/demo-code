package com.example.demo.xieyu.chapter02;


class B extends A {
        private final static B instance = new B();
        public static B getInstance() {
            return instance;
        }

        public void test() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            instance.test2();
        }

        public void test2(){};
    }