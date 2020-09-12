package com.example.demo.designPatterns.strategy;

/**
 * @author HP
 * @Description Dog比较器
 * @date 2020/9/9-9:44
 */
public class DogComparator implements Comparator<Dog> {

    @Override
    public int compare(Dog o1, Dog o2) {
        if (o1.food < o2.food) return -1;
        else if (o1.food > o2.food) return 1;
        else return 0;
    }
}
