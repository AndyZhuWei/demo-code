package com.example.demo.designPatterns.factory.abstractFactory;

/**
 * @author HP
 * @Description TODO
 * @date 2020/9/9-22:49
 */
public class ModernFactory extends AbstractFactory{
    @Override
    Food createFood() {
        return new Bread();
    }

    @Override
    Vehicle createVehicle() {
        return new Car();
    }

    @Override
    Weapon createWeapon() {
        return new AK47();
    }
}
