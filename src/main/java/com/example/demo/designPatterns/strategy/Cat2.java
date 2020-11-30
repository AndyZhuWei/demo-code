package com.example.demo.designPatterns.strategy;

/**
 * @author HP
 * @Description 如果比较猫的大小呢，我们可以定义比较猫大小的方法
 * @date 2020/9/9-9:02
 */
public class Cat2 implements Comparable2<Cat> {
    int weight, height;

    public Cat2(int weight, int height) {
        this.weight = weight;
        this.height = height;
    }

    @Override
    public int compareTo(Cat c) {
        if (this.weight < c.weight) return -1;
        else if (this.weight > c.weight) return 1;
        else return 0;
    }

    @Override
    public String toString() {
        return "Cat2{" +
                "weight=" + weight +
                ", height=" + height +
                '}';
    }
}
