package com.example.demo.designPatterns.builder.demo2;

/**
 * @Author: zhuwei
 * @Date:2020/3/11 10:07
 * @Description: 首先生成一个director (1)，然后生成一个目标builder (2)
 * 接着使用director组装builder (3),组装完毕后使用builder创建产品实例 (4)。
 */
public class Clent {

    public static void main(String[] args) {
        ComputerDirector director = new ComputerDirector(); //1
        ComputerBuilder builder = new MacComputerBuilder("I5处理器","三星125");//2
        director.makeComputer(builder);//3
        Computer macComputer = builder.getComputer();//4
        System.out.println("mac computer:"+macComputer.toString());

        ComputerBuilder lenvoBuilder = new LenovoComputerBuilder("I7处理器","海力士222");
        director.makeComputer(lenvoBuilder);
        Computer lenovoComputer = lenvoBuilder.getComputer();
        System.out.println("lenovo computer:"+lenovoComputer.toString());

    }
}
