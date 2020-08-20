package com.example.demo.designPatterns.builder.demo2;

/**
 * @Author: zhuwei
 * @Date:2020/3/11 9:58
 * @Description: 抽象构建者类
 */
public abstract class ComputerBuilder  {
    public abstract void setUsbCount();
    public abstract void setKeyborad();
    public abstract void setDisplay();

    public abstract Computer getComputer();
}
