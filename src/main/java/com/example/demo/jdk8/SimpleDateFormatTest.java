package com.example.demo.jdk8;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @Author: zhuwei
 * @Date:2019/7/2 16:04
 * @Description: 对SimpleDateFormat线程不安全进行测试
 * 解决方案：
 * 1.仅在需要用到的地方创建一个新的实例，就没有线程安全问题，不过也加重了创建对象的负担，会频繁地创建和销毁对象，效率较低。
 * 2.简单粗暴，synchronized往上一套也可以解决线程安全问题，缺点自然就是并发量大的时候会对性能有影响，线程阻塞。
 * 3.ThreadLocal
 * 4.基于JDK1.8的DateTimeFormatter
 */
public class SimpleDateFormatTest {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String formatDate(Date date) throws ParseException {
        return sdf.format(date);
    }

    public static Date parse(String strDate) throws ParseException {
        return sdf.parse(strDate);
    }


    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String formatDate2(LocalDateTime date) {
        return formatter.format(date);
    }

    public static LocalDateTime parse2(String dateNow) {
        return LocalDateTime.parse(dateNow,formatter);
    }




    public static void main(String[] args) throws InterruptedException, ParseException {
        //单线程下没有问题
        //System.out.println(sdf.format(new Date()));
        //多线程测试有现成安全的问题
        ExecutorService service = Executors.newFixedThreadPool(100);

        for (int i = 0; i < 20; i++) {
            service.execute(() -> {
                for (int j = 0; j < 10; j++) {

                   try {
                        System.out.println(parse(
                                "2018-01-02 09:45:59"));

                       // System.out.println(ThreadLocalSimpleDateFormat.threadLocal.get().parse("2018-01-02 09:45:59"));*/

                       // System.out.println(parse2("2018-01-02 09:45:59"));

                   } catch (ParseException e) {

                        e.printStackTrace();

                    }
                }
            });
        }

        // 等待上述的线程执行完
        service.shutdown();
        service.awaitTermination(1, TimeUnit.DAYS);
    }
}
