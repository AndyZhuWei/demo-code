package com.example.demo.designPatterns.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author HP
 * @Description 有很多时候，观察者要根据事件的具体情况来进行处理
 * 大多数时候，我们处理事件的时候，需要事件源对象
 * @date 2020/9/12-20:38
 */
public class Main7 {

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
            WakeupEvent event = new WakeupEvent(System.currentTimeMillis(),"bed",this);
            for(Observer observer : observerList) {
                observer.actionOnWakeUp(event);
            }

        }
    }

    //事件类
    static class WakeupEvent {
        long timestamp;
        String loc;
        Child source;

        public WakeupEvent(long timestamp, String loc,Child source) {
            this.timestamp = timestamp;
            this.loc = loc;
            this.source=source;
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
