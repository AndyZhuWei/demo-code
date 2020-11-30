package com.example.demo.xieyu.chapter03;

import java.lang.instrument.Instrumentation;

/**
 * @Author: zhuwei
 * @Date:2019/11/7 9:46
 * @Description: 将Instrumentation载入内存
 */
public class InstForTransformer {

    /*编译为Agent后，系统启动执行main方法前会调用它*/
    public static void premain(String agentArgs,
                               Instrumentation isntP) {
        isntP.addTransformer(new TestTransformer());
    }
























}
