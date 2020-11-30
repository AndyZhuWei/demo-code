package com.example.demo.designPatterns.bridge.v4;

/**
 * @author HP
 * @Description TODO
 * @date 2020/9/15-9:34
 */
public class GG {
    public void chase(MM mm) {
        Gift g = new WarmGift(new Flower());
        give(mm,g);
    }

    public void give(MM mm, Gift g) {
        System.out.println(g+"gived!");
    }
}
