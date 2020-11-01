package com.example.demo.thread;

/**
 * @Author: zhuwei
 * @Date:2018/12/18 10:34
 * @Description: 从这段代码的输出结果可以看出，在main线程中和thread1线程中，longLocal保存的副本值和stringLocal保存的副本值都不一样。
 * 最后一次在main线程再次打印副本值是为了证明在main线程中和thread1线程中的副本值确实是不同的。
 * 参考 https://www.cnblogs.com/dolphin0520/p/3920407.html
 */
public class ThreadLocalTest {
    ThreadLocal<Long> longLocal = new ThreadLocal<Long>();
    ThreadLocal<String> stringLocal = new ThreadLocal<String>();

    public void set() {
        //设置值的时候其实是设置在到了当前线程的map
        // （这个map是Thread对象中的ThreadLocal.ThreadLocalMap对象）中，这个map以相应的threadLocal位key，相应的值位value
        //Spring声明式事务就用到了ThreadLocal,保证同一个Connection
        //保存再map中的entry是一个继承自WeakReference的对象，为什么要继承这个弱引用呢，
        //是因为一般线程对象是长期存在的，所以threadLocalMap是长期存在，指向ThreadLocal的强引用对象结束后，
        // 如果指向Entry中的key指向ThreadLocal的是强引用就会有内存泄漏的风险。但是还有一个问题，如果是弱引用，则key被回收后，变为null。则对应的
        //value则一直会访问不到，所以还是有内存泄漏的风险。所以一般我们再用ThreadLocal的时候需要手动清除，即threadlocal.remove()
        //具体的示例图可以参见本目录中的weakReference.png图片
        longLocal.set(Thread.currentThread().getId());
        stringLocal.set(Thread.currentThread().getName());
    }

    public long getLong() {
        return longLocal.get();
    }

    public String getString() {
        return stringLocal.get();
    }


    public static void main(String[] args) throws InterruptedException {
        final ThreadLocalTest test = new ThreadLocalTest();

        test.set();
        System.out.println(test.getLong());
        System.out.println(test.getString());

        Thread thread1 = new Thread() {
            @Override
            public void run() {
                test.set();
                System.out.println(test.getLong());
                System.out.println(test.getString());
            }
        };

        thread1.start();
        thread1.join();

        System.out.println(test.getLong());
        System.out.println(test.getString());

    }



}
