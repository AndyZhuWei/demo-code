package com.example.demo.designPatterns.bridge.v1;

/**
 * @author HP
 * @Description TODO
 * @date 2020/9/15-9:34
 */
public class GG {
    public void chase(MM mm) {
        Gift g = new Book();
        give(mm,g);
    }

    public void give(MM mm,Gift g) {
        
    }
}
