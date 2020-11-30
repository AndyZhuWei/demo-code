package com.example.demo.jdk8.time;


import java.time.LocalDate;

//如果是JDK8的应用，可以使用Instant代替Date，LocalDateTime代替Calendar
//DateTimeFormatter代替SimpleDateFormat
public class TimeTest {
    public static void main(String[] args) {
        //获取今年的天数
        int daysOfThisYear = LocalDate.now().lengthOfYear();
        System.out.println(daysOfThisYear);

        //获取指定某年的天数
        daysOfThisYear = LocalDate.of(2011,1,1).lengthOfYear();
        System.out.println(daysOfThisYear);
    }
}
