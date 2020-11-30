package com.example.demo.designPatterns.builder.msb;

/**
 * @author HP
 * @Description TODO
 * @date 2020/9/14-9:20
 */
public class Person {
    int id;
    String name;
    int age;
    double weight;
    int score;
    Location loc;

    private Person(){}

    public static class PersonBuilder {
        Person p = new Person();

        public PersonBuilder basicInfo(int id,String name,int age) {
            p.id=id;
            p.name=name;
            p.age=age;
            return this;
        }

        public PersonBuilder weight(double weight) {
            p.weight = weight;
            return this;
        }

        public PersonBuilder score(int score) {
            p.score=score;
            return this;
        }

        public PersonBuilder loc(String street,String roomNo) {
            p.loc = new Location(street,roomNo);
            return this;
        }

        public Person build() {
            return p;
        }

    }

}

class Location {
    String street;
    String roomNO;

    public Location(String street, String roomNO) {
        this.street = street;
        this.roomNO = roomNO;
    }
}
