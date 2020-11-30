package com.example.demo.designPatterns.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author HP
 * @Description 加入多个观察者
 * @date 2020/9/12-20:38
 */
public class Main6 {

    static class Child {
        private boolean cry = false;
        private List<Observer> observerList = new ArrayList<>();

        {
            observerList.add(new Dad());
            observerList.add(new Mum());
            observerList.add(new Dog());
        }

        public boolean isCry() {return cry;}

        public void wakeUp() {
            System.out.println("Waked Up! Crying wuwuwuwu...");
            cry = true;
            WakeupEvent event = new WakeupEvent(System.currentTimeMillis(),"bed");
            for(Observer observer : observerList) {
                observer.actionOnWakeUp(event);
            }

        }
    }

    //事件类
    static class WakeupEvent {
        long timestamp;
        String loc;

        public WakeupEvent(long timestamp, String loc) {
            this.timestamp = timestamp;
            this.loc = loc;
        }
    }

    static class Dad implements Observer{

        @Override
        public void actionOnWakeUp(WakeupEvent event) {
            feed();
        }

        public void feed(){
            System.out.println("dad feeding...");
        }
    }
    static class Mum implements Observer{
        @Override
        public void actionOnWakeUp(WakeupEvent event) {
            hug();
        }

        public void hug(){
            System.out.println("mum huging...");
        }
    }
    static class Dog implements Observer{
        @Override
        public void actionOnWakeUp(WakeupEvent event) {
            wang();
        }

        public void wang(){
            System.out.println("dog wanging...");
        }
    }


    interface Observer {
        void actionOnWakeUp(WakeupEvent event);
    }


    public static void main(String[] args) {
      Child c = new Child();
      c.wakeUp();
    }


}
