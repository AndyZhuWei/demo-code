package com.example.demo.designPatterns.factory.abstractFactory;

/**
 * @author HP
 * @Description 抽象工厂方法，此模式再产品一族上扩展很方便，但是对于扩展产品则不是很方便
 * @date 2020/9/9-22:51
 */
public class Main {
    public static void main(String[] args) {
        AbstractFactory af = new MagicFactory();
        Food food = af.createFood();
        Vehicle vehicle = af.createVehicle();
        Weapon weapon = af.createWeapon();
        food.printName();
        vehicle.go();
        weapon.shot();


    }
}
