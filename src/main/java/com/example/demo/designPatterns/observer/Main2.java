package com.example.demo.designPatterns.observer;

/**
 * @author HP
 * @Description 和第一种没有本质区别，是面向对象的傻等
 * @date 2020/9/12-20:31
 */
public class Main2 {

    static class Child {
        private boolean cry = false;

        public boolean isCry() {return cry;}

        public void wakeUp() {
            System.out.println("Waked Up! Crying wuwuwuwu...");
            cry = true;
        }
    }


    public static void main(String[] args) {
        Child child = new Child();
        while(!child.isCry()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("observing...");
        }
    }
}
