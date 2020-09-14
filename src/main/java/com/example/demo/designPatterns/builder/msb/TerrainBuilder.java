package com.example.demo.designPatterns.builder.msb;

/**
 * @author HP
 * @Description TODO
 * @date 2020/9/14-9:15
 */
public interface TerrainBuilder {
    TerrainBuilder buildWall();
    TerrainBuilder buildFort();
    TerrainBuilder buildMine();
    Terrain build();
}
