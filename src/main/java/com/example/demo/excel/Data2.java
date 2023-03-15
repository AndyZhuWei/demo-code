package com.example.demo.excel;

import com.alibaba.excel.annotation.ExcelProperty;

public class Data2 {

        @ExcelProperty(index = 0)
        private String origin_name;
        @ExcelProperty(index = 1)
        private String name;
        @ExcelProperty(index = 2)
        private String company;
        @ExcelProperty(index = 3)
        private String forDisease;


    public String getOrigin_name() {
        return origin_name;
    }

    public void setOrigin_name(String origin_name) {
        this.origin_name = origin_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getForDisease() {
        return forDisease;
    }

    public void setForDisease(String forDisease) {
        this.forDisease = forDisease;
    }
}