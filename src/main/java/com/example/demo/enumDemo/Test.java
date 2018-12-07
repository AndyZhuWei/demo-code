package com.example.demo.enumDemo;

/**
 * @Author: zhuwei
 * @Date:2018/12/7 17:04
 * @Description:
 */
public class Test {
    public static void main(String[] args) {
        Weekday sun = Weekday.SUN;
        System.out.println(sun.getValue());


//        System.out.println("nowday ====>" + Weekday.SAT);
//        System.out.println("nowday int =====> " + Weekday.SAT.ordinal());
//        System.out.println("nextday ====> " + Weekday.getNextDay(Weekday.SAT));


//        System.out.println(Weekday.valueOf("mon".toUpperCase()));
//
//        for (Weekday w : Weekday.values()){
//            System.out.println(w + ".ordinal()  ====>" +w.ordinal());
//        }
//
//
//        System.out.println("Weekday.MON.compareTo(Weekday.FRI) ===> " + Weekday.MON.compareTo(Weekday.FRI));
//        System.out.println("Weekday.MON.compareTo(Weekday.MON) ===> " + Weekday.MON.compareTo(Weekday.MON));
//        System.out.println("Weekday.MON.compareTo(Weekday.SUM) ===> " + Weekday.MON.compareTo(Weekday.SUN));
//        //Weekday.MON.compareTo(Weekday.FRI) ===> -4
//        //Weekday.MON.compareTo(Weekday.MON) ===> 0
//        //Weekday.MON.compareTo(Weekday.SUM) ===> 1
//
//        System.out.println("Weekday.MON.name() ====> " + Weekday.MON.name());
        //Weekday.MON.name() ====> MON


    }
}
