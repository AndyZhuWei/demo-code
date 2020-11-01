package com.example.demo.designPatterns.state.car;

/**
 * @author HP
 * @Description TODO
 * @date 2020/9/16-14:40
 */
public class Car {
    private CarState carState;

    public Car(CarState carState) {
        this.carState=carState;
    }

}
