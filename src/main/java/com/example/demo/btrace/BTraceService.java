package com.example.demo.btrace;

import org.springframework.stereotype.Service;

/**
 * @Author: zhuwei
 * @Date:2019/9/10 6:34
 * @Description:
 */
@Service
public class BTraceService {


    public int plus(int a,int b) {
        return a+b;
    }
}
