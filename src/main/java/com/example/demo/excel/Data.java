package com.example.demo.excel;

import com.alibaba.excel.annotation.ExcelProperty;

public class Data {

        @ExcelProperty(index = 0)
        private String no;
        @ExcelProperty(index = 1)
        private String area;
        @ExcelProperty(index = 2)
        private String province;
        @ExcelProperty(index = 3)
        private String city;
        @ExcelProperty(index = 4)
        private String level;
        @ExcelProperty(index = 5)
        private String aidCenterName;



        public String getNo() {
            return no;
        }

        public void setNo(String no) {
            this.no = no;
        }

    public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getAidCenterName() {
            return aidCenterName;
        }

        public void setAidCenterName(String aidCenterName) {
            this.aidCenterName = aidCenterName;
        }
    }