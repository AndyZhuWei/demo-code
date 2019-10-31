package com.example.demo.designPatterns.visitor;

/**
 * @Author: zhuwei
 * @Date:2019/10/29 21:52
 * @Description: CTOVisitor
 */
public class CTOVisitor implements Visitor{

    @Override
    public void visit(Engineer engineer) {
        System.out.println("工程师："+engineer.name+",代码行数："+engineer.getCodeLines());
    }

    @Override
    public void visit(Manager manager) {
        System.out.println("经理："+manager.name+"，产品数量："+manager.getProducts());
    }

    //重载的 visit 方法会对元素进行不同的操作，而通过注入不同的 Visitor
    // 又可以替换掉访问者的具体实现，使得对元素的操作变得更灵活，可扩展性
    // 更高，同时也消除了类型转换、if-else 等“丑陋”的代码。

}
