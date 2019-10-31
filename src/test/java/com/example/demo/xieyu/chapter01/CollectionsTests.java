package com.example.demo.xieyu.chapter01;

import org.junit.Test;

import java.text.Collator;
import java.util.*;

/**
 * @Author: zhuwei
 * @Date:2019/10/23 18:14
 * @Description: 集合相关的示例
 */
public class CollectionsTests {



    //通过常量构造一个ArrayList返回
    @Test
    public void test01() {
        //常规写法
        List<String> list = new ArrayList<String>();
        list.add("a");
        list.add("b");
        list.add("c");

        //用工具就可以这样写：
        List<String> list1 = Arrays.asList("a","b","c");
    }


    //中文拼音排序
    private final static Comparator CHINA_COMPARE
            = Collator.getInstance(Locale.CHINA);

    //对list排序
    private static void sortList() {
        List<String> list = Arrays.asList("张三","李四","王五");
        Collections.sort(list,CHINA_COMPARE);
        for(String str : list) {
            System.out.println(str);
        }
    }

    //对数组排序
    private static void sortArray() {
        String[] arr = {"张三","李四","王五"};
        Arrays.sort(arr,CHINA_COMPARE);
        for(String str : arr) {
            System.out.println(str);
        }
    }
    @Test
    public void test02() {
        sortList();
        sortArray();
    }
}
