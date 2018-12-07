import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: zhuwei
 * @Date:2018/9/4 10:11
 * @Description:
 */
public class JVMInstructionTest implements Runnable {

    public JVMInstructionTest() {
        System.out.println("constructor method");
    }

    private void s() {
        System.out.println("private method");
    }

    static void print() {
        System.out.println("static method");
    }

    void p() {
        System.out.println("instance method");
    }

    public void d(String str) {
        System.out.println("for method handle"+str);
    }

    static void ddd(String str) {
        System.out.println("static method for method handle"+str);
    }

    public static void main(String[] args) throws Throwable {
        /**
         * invoke special
         * 指令用于调用一些需要特殊处理的实例方法，包括实例初始化方法、私有方法和父类方法
         */
        JVMInstructionTest test = new JVMInstructionTest();
        /**
         * invoke special
         */
        test.s();
        /**
         * invoke virtual
         * 指令用于调用对象的实例方法，根据对象的实际类型进行分派
         */
        test.p();
        /**
         * invoke static
         * 用以调用类方法
         */
        print();
        /**
         * invoke interface
         * 用以调用接口方法，在运行时搜索一个实现了这个接口方法的对象，找出适合的方法进行调用。
         */
        Runnable r = new JVMInstructionTest();
        r.run();
        /**
         * Java 8中，lambda表达式和默认方法时，底层会生成和使用invoke dynamic
         * invoke dynamic
         */
        List<Integer> list = Arrays.asList(1, 2, 3, 4);
        list.stream().forEach(System.out::println);

        /**
         * MethodHandle
         * invoke dynamic
         */
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandle mh = lookup.findVirtual(JVMInstructionTest.class,"d", MethodType.methodType(void.class,String.class));
        System.out.println(mh);
        mh.bindTo(test).invoke("a");

        mh = lookup.findStatic(JVMInstructionTest.class,"ddd",MethodType.methodType(void.class,String.class));
        mh.invoke("static");





    }

    @Override
    public void run() {
        System.out.println("interface method");
    }
}
