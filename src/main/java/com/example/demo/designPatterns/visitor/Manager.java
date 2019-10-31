package com.example.demo.designPatterns.visitor;

import java.util.Random;

/**
 * @Author: zhuwei
 * @Date:2019/10/29 15:41
 * @Description:
 */
//经理
public class Manager extends Staff {

    public Manager(String name) {
        super(name);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    //一年做的产品数量
    public int getProducts() {
        return new Random().nextInt(10);
    }
}
