package com.example.demo.designPatterns.visitor;

/**
 * @Author: zhuwei
 * @Date:2019/10/29 15:56
 * @Description:
 */
public interface Visitor {

    //访问工程师类型
    void visit(Engineer engineer);

    //访问经理类型
    void visit(Manager manager);
}
