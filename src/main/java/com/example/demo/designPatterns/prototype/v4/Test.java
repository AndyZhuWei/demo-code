package com.example.demo.designPatterns.prototype.v4;

/**
 * @author HP
 * @Description 思考为什么loc中的string类型是引用，为什么不需要克隆
 * 因为这个例子中常量是在常量池中，改变其中的常量，只是把指针指向了常量池中
 * 其的常量而已。如果Sring是new出来而不是字面量，则还是需要克隆String引用
 * 需要注意的String不能clone，克隆是需要new出一个新的对象
 * @date 2020/9/15-23:17
 */
public class Test {

    public static void main(String[] args) throws CloneNotSupportedException {
        Person p1 = new Person();
        //要调用clone方法必须实现cloneable接口和重写object的clone()方法
        Person p2 = (Person)p1.clone();
        System.out.println("p1.loc == p2.loc?"+(p1.loc == p2.loc));

        p1.loc.street.reverse();
        System.out.println(p2.loc.street);
    }
}

class Person implements Cloneable {
    int age = 8;
    int score = 100;

    Location loc = new Location(new StringBuilder("bj"),22);

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Person p =(Person)super.clone();
        p.loc = (Location)loc.clone();
        return p;
    }
}

class Location implements Cloneable{
    StringBuilder street;
    int roomNo;

    @Override
    public String toString() {
        return "Location{" +
                "street='" + street + '\'' +
                ", roomNo=" + roomNo +
                '}';
    }

    public Location(StringBuilder street, int roomNo) {
        this.street = street;
        this.roomNo = roomNo;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
