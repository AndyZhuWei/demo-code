import java.util.Arrays;

public class Test {
    public static void main(String[] args){
        //System.out.println(Math.min(Double.MIN_VALUE, 0.0d));
//        System.out.println(Double.MIN_VALUE);
//        System.out.println(Integer.MIN_VALUE);
//        System.out.println(Float.MIN_VALUE);
//        System.out.println((int)Character.MIN_VALUE);
//        System.out.println(1.0/0.0);
//        System.out.println(3/0.0);
//        System.out.println(Double.POSITIVE_INFINITY);
//        System.out.println(Double.NEGATIVE_INFINITY);
//        System.out.println(Double.NaN);



        char[] chars = new char[] {'\u0097'};
        String str = new String(chars);
        byte[] bytes = str.getBytes();
        System.out.println(Arrays.toString(bytes));
    }
}
