package com.example.demo.designPatterns.visitor;

public class Client {

    public static void main(String[] args) {
        // 构建报表
        BusinessReport report = new BusinessReport();
        System.out.println("=========== CEO看报表 ===========");
        report.showReport(new CEOVisitor());
        System.out.println("=========== CTO看报表 ===========");
        report.showReport(new CTOVisitor());


       // 在上述示例中，Staff 扮演了 Element 角色，而 Engineer 和 Manager 都是 ConcreteElement；
        // CEOVisitor 和 CTOVisitor 都是具体的 Visitor 对象；而 BusinessReport 就是
        // ObjectStructure；Client就是客户端代码。
        //访问者模式最大的优点就是增加访问者非常容易，我们从代码中可以看到，如果要增
        // 加一个访问者，只要新实现一个 Visitor 接口的类，从而达到数据对象与数据操
        // 作相分离的效果。如果不实用访问者模式，而又不想对不同的元素进行不同的操作，
        // 那么必定需要使用 if-else 和类型转换，这使得代码难以升级维护。

    }
}