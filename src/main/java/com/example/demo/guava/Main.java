package com.example.demo.guava;

import org.assertj.core.util.Preconditions;
import org.junit.Test;

/**
 * @Description TODO
 * @Author zhuwei
 * @Date 2021/3/15 14:03
 */
public class Main {

    @Test
    public void test01() {
        String params = "";
        String s = Preconditions.checkNotNull(params);
        System.out.println(s);
    }
}
