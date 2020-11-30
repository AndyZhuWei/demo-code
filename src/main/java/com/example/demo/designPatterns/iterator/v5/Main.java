package com.example.demo.designPatterns.iterator.v5;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author HP
 * @Description JDK自带的迭代器
 * @date 2020/9/13-17:49
 */
public class Main {

    public static void main(String[] args) {
        Collection list = new LinkedList<String>();
        for(int i=0;i<20;i++) {
            list.add(new String("s"+i));
        }

        Iterator iterator = list.iterator();
        while(iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}
