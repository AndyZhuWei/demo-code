package com.example.demo.jdk8;

import java.util.LinkedList;
import java.util.List;

public class ForEachTest {

    public static void main(String[] args) {
        List<String> names = new LinkedList<>();
        names.add("Apple");
        names.add("Orange");
        names.forEach(name -> System.out.println(name));
    }
}
