package com.example.demo.designPatterns.builder.msb;

/**
 * @author HP
 * @Description TODO
 * @date 2020/9/14-9:17
 */
public class ComplexTerrainBuilder implements TerrainBuilder{
    Terrain terrain = new Terrain();

    @Override
    public TerrainBuilder buildWall() {
        terrain.w = new Wall(10,10,50,50);
        return this;
    }

    @Override
    public TerrainBuilder buildFort() {
        terrain.f = new Fort(10,10,50,50);
        return this;
    }

    @Override
    public TerrainBuilder buildMine() {
        terrain.m = new Mine(10,10,50,50);
        return this;
    }

    @Override
    public Terrain build() {
        return terrain;
    }
}
