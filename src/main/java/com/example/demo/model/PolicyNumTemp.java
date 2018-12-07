package com.example.demo.model;

public class PolicyNumTemp {

    public PolicyNumTemp() {

    }
    public PolicyNumTemp(String policyNum) {
        this.policyNum = policyNum;
    }
    private String policyNum;

    public String getPolicyNum() {
        return policyNum;
    }

    public void setPolicyNum(String policyNum) {
        this.policyNum = policyNum == null ? null : policyNum.trim();
    }
}