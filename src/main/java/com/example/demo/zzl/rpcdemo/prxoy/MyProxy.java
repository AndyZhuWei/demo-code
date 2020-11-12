package com.example.demo.zzl.rpcdemo.prxoy;


import com.example.demo.zzl.rpcdemo.rpc.Dispatcher;
import com.example.demo.zzl.rpcdemo.rpc.protocol.MyContent;
import com.example.demo.zzl.rpcdemo.rpc.transport.ClientFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.CompletableFuture;

/**
 * @author HP
 * @Description TODO
 * @date 2020/11/9-10:41
 */
public class MyProxy {

    public static <T> T proxyGet(Class<T> interfaceInfo) {
        //实现各个版本的动态代理

        ClassLoader classLoader = interfaceInfo.getClassLoader();
        Class<?>[] interfaces = {interfaceInfo};

        //TODO  LOCAL REMOTE  实现：  用到dispatcher  直接给你返回，还是本地调用的时候也代理一下

        Dispatcher dis = Dispatcher.getDis();
        return (T) Proxy.newProxyInstance(classLoader, interfaces, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                Object res = null;
                Object o = dis.get(interfaceInfo.getName());
                if(o== null) {
                    //就要走rpc
                    //1.调用方法、参数、服务 ==》 封装成message
                    String name = interfaceInfo.getName();
                    String methodName = method.getName();
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    //TODO  rpc  就像小火车拉货  content是service的具体数据，但是还需要header层完成IO传输的控制
                    MyContent myContent = new MyContent(name, methodName, parameterTypes, args);
                    //TODO 未来的小火车可能会变
                    /**
                     * 1,缺失了注册发现，zk
                     * 2,第一层负载面向的provider
                     * 3，consumer  线程池  面向 service；并发就有木桶，倾斜
                     * serviceA
                     *      ipA:port
                     *          socket1
                     *          socket2
                     *      ipB:port
                     */
                    CompletableFuture redF = ClientFactory.transport(myContent);
                    //5.?如果从IO未来回来了，怎么讲代码执行到这里
                    //（睡眠/回调，如何让线程停下来，你还能让他继续）


                    res =  redF.get(); //阻塞的

                } else {
                    //就是local
                    //插入一些插件的机会，做一些扩展
                    System.out.println("local FC....");
                    Class<?> clazz = o.getClass();
                    try {
                        Method m = clazz.getMethod(method.getName(), method.getParameterTypes());
                        res = m.invoke(o, args);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
                return res;
            }
        });
    }
}
