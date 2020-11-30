package com.example.demo.designPatterns.factory.abstractFactory;

/**
 * @author HP
 * @Description TODO
 * @date 2020/9/9-22:49
 */
public class MagicFactory extends AbstractFactory{
    @Override
    Food createFood() {
        return new MushRoom();
    }

    @Override
    Vehicle createVehicle() {
        return new Broom();
    }

    @Override
    Weapon createWeapon() {
        return new MagicStick();
    }
}
