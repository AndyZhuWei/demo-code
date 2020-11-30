package com.example.demo.designPatterns.builder.demo2;

/**
 * @Author: zhuwei
 * @Date:2020/3/11 10:05
 * @Description: 指导者类（ComputerDirector）
 */
public class ComputerDirector {
    public void makeComputer(ComputerBuilder builder) {
        builder.setUsbCount();
        builder.setDisplay();
        builder.setKeyborad();
    }
}
