package com.example.demo.designPatterns.factory.abstractFactory;

/**
 * @author HP
 * @Description TODO
 * @date 2020/9/9-22:37
 */
public abstract class AbstractFactory {
    abstract Food createFood();
    abstract Vehicle createVehicle();
    abstract Weapon createWeapon();
}
