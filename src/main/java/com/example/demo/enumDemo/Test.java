package com.example.demo.enumDemo;

/**
 * @Author: zhuwei
 * @Date:2018/12/7 17:04
 * @Description:
 */
public class Test {
    public static void main(String[] args) {
//        Weekday sun = Weekday.SUN;
//        System.out.println(sun);


        System.out.println("nowday ====>" + Weekday.SAT);
        System.out.println("nowday int =====> " + Weekday.SAT.ordinal());
        System.out.println("nextday ====> " + Weekday.getNextDay(Weekday.SAT));
    }
}
