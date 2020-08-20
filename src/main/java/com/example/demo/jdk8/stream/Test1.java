package com.example.demo.jdk8.stream;

import java.util.ArrayList;
import java.util.List;

public class Test1 {

    public static void main(String[] args) {
        List<String> words = new ArrayList<>();
        words.add("apple");
        words.add("apple111");
        words.add("apple222");
        words.add("apple3");

       // long count = words.stream().filter(w -> w.length() > 6).count();
        //并行执行过滤和统计操作
        long count = words.parallelStream().filter(w -> w.length() > 6).count();
        System.out.println(count);
    }
}
