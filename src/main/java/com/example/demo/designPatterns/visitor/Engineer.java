package com.example.demo.designPatterns.visitor;

import java.util.Random;

/**
 * @Author: zhuwei
 * @Date:2019/10/29 15:39
 * @Description: 工程师
 */
public class Engineer extends Staff{

    public Engineer(String name) {
        super(name);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    //工程师一年的代码数量
    public int getCodeLines() {
        return new Random().nextInt(10*10000);
    }
}
