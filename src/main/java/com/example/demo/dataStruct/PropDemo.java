package com.example.demo.dataStruct;

import java.util.Collections;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

/**
 * @Author: zhuwei
 * @Date:2018/9/11 15:31
 * @Description: Properties 继承于 Hashtable.表示一个持久的属性集.属性列表中每个键及其对应值都是一个字符串。
 */
public class PropDemo {

    public static void main(String[] args) {
        Properties capitals = new Properties();
        Set states;
        String str;

        capitals.put("Illinois", "Springfield");
        capitals.put("Missouri", "Jefferson City");
        capitals.put("Washington", "Olympia");
        capitals.put("California", "Sacramento");
        capitals.put("Indiana", "Indianapolis");

        // Show all states and capitals in hashtable.
        states = capitals.keySet(); // get set-view of keys
        Iterator itr = states.iterator();
        while(itr.hasNext()) {
            str = (String) itr.next();
            System.out.println("The capital of " +
                    str + " is " + capitals.getProperty(str) + ".");
        }

        System.out.println();

        // look for state not in list -- specify default
        str = capitals.getProperty("Florida", "Not Found");
        System.out.println("The capital of Florida is "
                + str + ".");
    }
}
