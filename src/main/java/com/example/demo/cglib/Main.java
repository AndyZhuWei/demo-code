package com.example.demo.cglib;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Random;

/**
 * @author HP
 * @Description TODO
 * @date 2020/9/13-13:58
 */
public class Main {
    public static void main(String[] args) {
        Enhancer enhancer = new Enhancer();
        //如果Tank是final的则cglib就不能通过继承的方式生成子类的动态代理类，
        //所以cglib也有局限性。但是asm就可以，他可以直接修改相应的字节码文件
        //cglib的底层也用的是asm
        enhancer.setSuperclass(Tank.class);
        enhancer.setCallback(new TimeMethodInterceptor());
        Tank tank = (Tank)enhancer.create();
        tank.move();
    }

}


class TimeMethodInterceptor implements MethodInterceptor {

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println(o.getClass().getSimpleName());
        System.out.println(o.getClass().getSuperclass().getSimpleName());
        System.out.println(method.getName());
        System.out.println("before");
        Object result = null;
        result = methodProxy.invokeSuper(o,objects);
        //result = method.invoke(o.getClass().getSuperclass().newInstance(),objects);
        System.out.println("after");
        return result;
    }
}



class Tank {
    public void move() {
        System.out.println("Tank moving claclacia...");
        try {
            Thread.sleep(new Random().nextInt(10000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

