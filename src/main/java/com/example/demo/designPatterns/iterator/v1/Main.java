package com.example.demo.designPatterns.iterator.v1;


/**
 * @author HP
 * @Description 构建一个容器，可以添加对象
 * @date 2020/9/13-16:51
 */
public class Main {

    public static void main(String[] args) {
        ArrayList list = new ArrayList();
        for (int i = 0; i < 15; i++) {
            list.add(new String("s"+i));
        }
        System.out.println(list.size());
    }
}

//相比数组，这个容器不用考虑边界问题，可以动态扩展
class ArrayList {
    Object[] objects = new Object[10];
    //objects中下一个空的位置在哪儿，或者说目前容器中有多少个元素
    private int index = 0;
    public void add(Object o) {
        if(index == objects.length) {
            Object[] newObjects = new Object[objects.length*2];
            System.arraycopy(objects,0,newObjects,0,objects.length);
            objects = newObjects;
        }

        objects[index]=o;
        index++;
    }

    public int size(){return index;}

}

