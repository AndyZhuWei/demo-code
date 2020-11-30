package com.example.demo.designPatterns.builder.msb;

/**
 * @author HP
 * @Description TODO
 * @date 2020/9/14-9:18
 */
public class Main {
    public static void main(String[] args) {
        TerrainBuilder terrainBuilder = new ComplexTerrainBuilder();
        Terrain t = terrainBuilder.buildFort().buildWall().buildMine().build();


        Person p = new Person.PersonBuilder()
                 .basicInfo(1,"zhangsan",18)
                .score(20)
                .weight(200)
                .loc("bj","23")
                .build();



    }
}
