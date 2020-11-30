package com.example.demo.designPatterns.strategy;

/**
 * @author HP
 * @Description 定义猫的身高比较器
 * @date 2020/9/9-9:44
 */
public class CatHeightComparator implements Comparator<Cat> {

    @Override
    public int compare(Cat o1, Cat o2) {
        if (o1.height < o2.height) return -1;
        else if (o1.height > o2.height) return 1;
        else return 0;
    }
}
