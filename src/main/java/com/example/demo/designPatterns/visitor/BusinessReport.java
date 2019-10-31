package com.example.demo.designPatterns.visitor;

import java.util.LinkedList;
import java.util.List;

/**
 * @Author: zhuwei
 * @Date:2019/10/29 15:43
 * @Description: 员工业务报表类
 */
public class BusinessReport {

    private List<Staff> mStaffs = new LinkedList<>();

    public BusinessReport() {
        mStaffs.add(new Manager("经理-A"));
        mStaffs.add(new Engineer("工程师-A"));
        mStaffs.add(new Engineer("工程师-B"));
        mStaffs.add(new Engineer("工程师-C"));
        mStaffs.add(new Manager("经理-B"));
        mStaffs.add(new Engineer("工程师-D"));
    }

    /**
     * 访问者展示报表
     * @param visitor
     */
    public void showReport(Visitor visitor) {
        for(Staff staff : mStaffs) {
            staff.accept(visitor);
        }
    }
}
