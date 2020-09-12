package com.example.demo.designPatterns.factory;

/**
 * @author HP
 * @Description 简单工程，可扩展不好
 * 每增加一个交通工具都需要修改这个代码
 * @date 2020/9/9-22:10
 */
public class SimpleVehicleFactory {
    public Car createCar() {
        return new Car();
    }

    public Plane createPlane() {
        return new Plane();
    }
}
