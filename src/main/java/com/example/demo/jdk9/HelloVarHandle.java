package com.example.demo.jdk9;

import java.lang.invoke.MethodHandles;
//import java.lang.invoke.VarHandle;

/**
 * @author HP
 * @Description TODO
 * @date 2020/9/26-22:11
 */
public class HelloVarHandle {

    int x = 8;

  //  private static VarHandle handle;

    static {
      //  try {
           // handle = MethodHandles.lookup().findVarHandle(HelloVarHandle.class,"x",int.class);
       // } catch (NoSuchFieldException e) {
       //     e.printStackTrace();
       // } catch (IllegalAccessException e) {
        //    e.printStackTrace();
      //  }
    }

    public static void main(String[] args) {
        HelloVarHandle t = new HelloVarHandle();

      //  System.out.println((int)handle.get(t));
       // handle.set(t,9);
       // System.out.println(t.x);

        //通过handle可以原子性的修改值，
        //所以有了varhanle普通的属性也可以进行原子性的操作
       // handle.compareAndSet(t,9,10);
        System.out.println(t.x);

      // handle.getAndAdd(t,10);
        System.out.println(t.x);
    }
}
