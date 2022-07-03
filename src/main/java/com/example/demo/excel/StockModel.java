package com.example.demo.excel;

import com.alibaba.excel.annotation.ExcelProperty;

import java.util.Date;

public class StockModel  {


    /**
     * 邀约码
     */
    @ExcelProperty("CODE") //
    private String code;

    /**
     * 营销员工号
     */
    @ExcelProperty("SALES_MAN_NO") //
    private String salesmanNum;

    /**
     * 营销员姓名
     */
    @ExcelProperty("SALES_MAN") //
    private String salesman;

    /**
     * 总公司ID
     */
    @ExcelProperty("OWNED_COMPANY_ID")
    private String ownerCompanyId;

    /**
     * 总公司名称
     */
    @ExcelProperty("OWNED_COMPANY")
    private String ownerCompanyName;

    /**
     * 支公司ID
     */
    @ExcelProperty("BRANCH_COMPANY_ID")
    private String companyId;

    /**
     * 支公司名称
     */
    @ExcelProperty("BRANCH_COMPANY")
    private String companyName;

    /**
     * 项目类别
     */
    @ExcelProperty("PROJECT_TYPE")
    private String projectType;

    /**
     * 项目编码
     */
    @ExcelProperty("PROJECT_CODE")
    private String projectCode;



    /**
     * 姓名
     */
    @ExcelProperty("NAME")
    private String userName;

    /**
     * 性别
     */
    @ExcelProperty("SEX")
    private String gender;

    /**
     * 身份证号
     */
    @ExcelProperty("IDNO")
    private String idcardNum;

    /**
     * 手机号
     */
    @ExcelProperty("PHONE")
    private String phoneNumber;

    /**
     * 出生日期
     */
    @ExcelProperty("BIRTHDAY")
    private String birthday;

    /**
     * 身高
     */
    @ExcelProperty("HEIGHT")
    private String height;

    /**
     * 体重
     */
    @ExcelProperty("WEIGHT")
    private String weight;


    /**
     * 创建时间
     */
    @ExcelProperty("CREATE_TIME")
    private Date createTime;

    /**
     * 血型
     */
    @ExcelProperty("BLOOD_TYPE")
    private String bloodType;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSalesmanNum() {
        return salesmanNum;
    }

    public void setSalesmanNum(String salesmanNum) {
        this.salesmanNum = salesmanNum;
    }

    public String getSalesman() {
        return salesman;
    }

    public void setSalesman(String salesman) {
        this.salesman = salesman;
    }

    public String getOwnerCompanyId() {
        return ownerCompanyId;
    }

    public void setOwnerCompanyId(String ownerCompanyId) {
        this.ownerCompanyId = ownerCompanyId;
    }

    public String getOwnerCompanyName() {
        return ownerCompanyName;
    }

    public void setOwnerCompanyName(String ownerCompanyName) {
        this.ownerCompanyName = ownerCompanyName;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIdcardNum() {
        return idcardNum;
    }

    public void setIdcardNum(String idcardNum) {
        this.idcardNum = idcardNum;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }
}