package com.example.demo.designPatterns.prototype.v2;

/**
 * @author HP
 * @Description 要想把loc也克隆一份，需要在克隆person的时候把其中
 * 的熟悉loc也克隆一下，这个就叫深克隆
 * @date 2020/9/15-23:17
 */
public class Test {

    public static void main(String[] args) throws CloneNotSupportedException {
        Person p1 = new Person();
        //要调用clone方法必须实现cloneable接口和重写object的clone()方法
        Person p2 = (Person)p1.clone();
        System.out.println(p2.age+" "+p2.score);
        System.out.println(p2.loc);

        System.out.println(p1.loc == p2.loc);
        p1.loc.street="sh";
        System.out.println(p2.loc);
    }
}

class Person implements Cloneable {
    int age = 8;
    int score = 100;

    Location loc = new Location("bj",22);

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Person p =(Person)super.clone();
        p.loc = (Location)loc.clone();
        return p;
    }
}

class Location implements Cloneable{
    String street;
    int roomNo;

    @Override
    public String toString() {
        return "Location{" +
                "street='" + street + '\'' +
                ", roomNo=" + roomNo +
                '}';
    }

    public Location(String street, int roomNo) {
        this.street = street;
        this.roomNo = roomNo;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
