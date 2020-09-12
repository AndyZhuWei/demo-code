package com.example.demo.designPatterns.observer;

/**
 * @author HP
 * @Description 加入观察者
 * @date 2020/9/12-20:38
 */
public class Main3 {

    static class Child {
        private boolean cry = false;
        Dad dad = new Dad();

        public boolean isCry() {return cry;}

        public void wakeUp() {
            System.out.println("Waked Up! Crying wuwuwuwu...");
            cry = true;
            dad.feed();
        }
    }

    static class Dad {
        public void feed(){
            System.out.println("dad feeding...");
        }
    }


    public static void main(String[] args) {
      Child c = new Child();
      c.wakeUp();
    }


}
