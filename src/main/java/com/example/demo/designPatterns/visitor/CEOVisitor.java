package com.example.demo.designPatterns.visitor;

/**
 * @Author: zhuwei
 * @Date:2019/10/29 21:46
 * @Description: CEO访问者
 */
public class CEOVisitor implements Visitor {
    @Override
    public void visit(Engineer engineer) {
        System.out.println("工程师:"+engineer.name+",KPI:"+engineer.kpi);
    }

    @Override
    public void visit(Manager manager) {
        System.out.println("经理："+manager.name+",KPI:"+manager.kpi+",新产品数量："+manager.getProducts());
    }


    //在CEO的访问者中，CEO关注工程师的 KPI，经理的 KPI 和新产品数量，通过两个 visitor 方法分别进行处理。
    // 如果不使用 Visitor 模式，只通过一个 visit 方法进行处理，那么就需要在这个 visit 方法中进行判断，
    // 然后分别处理，代码大致如下：
//    public class ReportUtil {
//        public void visit(Staff staff) {
//            if (staff instanceof Manager) {
//                Manager manager = (Manager) staff;
//                System.out.println("经理: " + manager.name + ", KPI: " + manager.kpi +
//                        ", 新产品数量: " + manager.getProducts());
//            } else if (staff instanceof Engineer) {
//                Engineer engineer = (Engineer) staff;
//                System.out.println("工程师: " + engineer.name + ", KPI: " + engineer.kpi);
//            }
//        }
//    }

    //这就导致了 if-else 逻辑的嵌套以及类型的强制转换，难以扩展和维护，当类型较多时，
    // 这个 ReportUtil 就会很复杂。而使用 Visitor 模式，通过同一个函数对
    // 不同对元素类型进行相应对处理，使结构更加清晰、灵活性更高。

}
