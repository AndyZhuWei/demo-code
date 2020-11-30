package com.example.demo.designPatterns.strategy;

/**
 * @author HP
 * @Description 如果比较猫的大小呢，我们可以定义比较猫大小的方法
 * @date 2020/9/9-9:02
 */
public class Dog implements Comparable {
    int food;

    public Dog(int food) {
        this.food = food;
    }

    @Override
    public int compareTo(Object o) {
        Dog d = (Dog)o;
        if (this.food < d.food) return -1;
        else if (this.food > d.food) return 1;
        else return 0;
    }

    @Override
    public String toString() {
        return "Dog{" +
                "food=" + food +
                '}';
    }
}
