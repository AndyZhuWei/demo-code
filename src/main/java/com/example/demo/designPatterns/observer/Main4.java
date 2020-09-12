package com.example.demo.designPatterns.observer;

/**
 * @author HP
 * @Description 加入多个观察者
 * @date 2020/9/12-20:38
 */
public class Main4 {

    static class Child {
        private boolean cry = false;
        Dad dad = new Dad();
        Mum mum = new Mum();
        Dog dog = new Dog();

        public boolean isCry() {return cry;}

        public void wakeUp() {
            System.out.println("Waked Up! Crying wuwuwuwu...");
            cry = true;
            dad.feed();
            mum.hug();
            dog.wang();

        }
    }

    static class Dad {
        public void feed(){
            System.out.println("dad feeding...");
        }
    }
    static class Mum {
        public void hug(){
            System.out.println("mum huging...");
        }
    }
    static class Dog {
        public void wang(){
            System.out.println("dog wanging...");
        }
    }


    public static void main(String[] args) {
      Child c = new Child();
      c.wakeUp();
    }


}
