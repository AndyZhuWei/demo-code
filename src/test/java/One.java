public class One {
    //静态属性和静态方法是否可以被继承？是否可以被重写？以及原因？
    public static String one_1 = "one";

    public String oneStr = "oneStr";

    public Integer i;

    public static void oneFn() {
        System.out.println("oneFn");
    }

    public Integer getI() {
        return i;
    }

    public void setI(Integer i) {
        if(this.i == null) {
            this.i = 0;
        }
        this.i += i;
    }
}