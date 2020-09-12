package com.example.demo.designPatterns.strategy;


import java.util.Arrays;

/**
 * @author HP
 * @Description TODO
 * @date 2020/9/9-8:55
 */
public class Main {

    public static void main(String[] args) {
        int[] a = {9,2,3,5,7,1,4};
        Sorter sorter = new Sorter();
        Sorter2 sorter2 = new Sorter2();
        sorter.sort(a);
        System.out.println(Arrays.toString(a));

        Cat[] cat = {new Cat(3,3),new Cat(1,1),new Cat(2,2)};
        sorter2.sort(cat);
        System.out.println(Arrays.toString(cat));


        Dog[] dog = {new Dog(3),new Dog(1),new Dog(0)};
        sorter2.sort(dog);
        System.out.println(Arrays.toString(dog));

        //使用比较器
        Dog[] dog3 = {new Dog(2),new Dog(1),new Dog(0)};
        Sorter3<Dog> sorter3 = new Sorter3<>();
        //sorter3.sort(dog3,new DogComparator());
        //当接口中只有一个方法时，可以用lamba表达式
        sorter3.sort(dog3,(o1,o2)->{
            if (o1.food < o2.food) return -1;
            else if (o1.food > o2.food) return 1;
            else return 0;
        });
        System.out.println(Arrays.toString(dog3));


    }
}
