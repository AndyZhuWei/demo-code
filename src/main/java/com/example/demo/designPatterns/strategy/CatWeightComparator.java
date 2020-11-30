package com.example.demo.designPatterns.strategy;

/**
 * @author HP
 * @Description 定义猫的体重比较器
 * @date 2020/9/9-9:44
 */
public class CatWeightComparator implements Comparator<Cat> {

    @Override
    public int compare(Cat o1, Cat o2) {
        if (o1.weight < o2.weight) return -1;
        else if (o1.weight > o2.weight) return 1;
        else return 0;
    }
}
