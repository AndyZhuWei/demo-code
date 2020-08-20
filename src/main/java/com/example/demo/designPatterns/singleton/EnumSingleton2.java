package com.example.demo.designPatterns.singleton;

public class EnumSingleton2 {

    private EnumSingleton2() {
    }

    public static EnumSingleton2 getIntance() {
        return EnumInstance.INSTANCE.getInstance();
    }

    //枚举类就是单例的
    enum EnumInstance {
        INSTANCE;
        private EnumSingleton2 enumSingleton;

        EnumInstance() {
            enumSingleton = new EnumSingleton2();
        }

        public EnumSingleton2 getInstance() {
            return this.enumSingleton;
        }
    }

    public static void main(String[] args) {
        System.out.println(EnumSingleton2.getIntance() == EnumSingleton2.getIntance());
    }

}