package com.example.demo.designPatterns.factory;

/**
 * @author HP
 * @Description 工厂方法模式，一个工厂对应一个产品，对于产品扩展很方便。这个具体的工厂类也可以继承一个抽象的工厂，但是
 * 其创建的产品就一个。
 * @date 2020/9/9-22:12
 */
public class CarFactory {

    public Movable create() {
        System.out.println("a car created!");
        return new Car();

    }
}
