public class MyTest {
    //静态属性和静态方法是否可以被继承？是否可以被重写？以及原因？
    public static void main(String[] args) {
       /* One one = new Two();
        one.oneFn();
        String one_1 = One.one_1;
        System.out.println("One.one_1>>>>>>>"+one_1);
        String one_12 = one.one_1;
        System.out.println("one.one_1>>>>>>>"+one_12);

        System.out.print(one.oneStr);*/


        One one = new One();
        one.setI(1);
        System.out.print(one.getI());
    }
}