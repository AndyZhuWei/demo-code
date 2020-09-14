package com.example.demo.designPatterns.adapter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author HP
 * @Description TODO
 * @date 2020/9/14-9:37
 */
public class Main {
    public static void main(String[] args) throws IOException {
        FileInputStream is = new FileInputStream("D:\\gc.log");
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String s = br.readLine();
        while (s != null && !s.equals("")) {
            System.out.println(s);
            s = br.readLine();
        }
        br.close();
    }
}
