package com.example.demo.zzl.rpcdemo.rpc.protocol;

import java.io.Serializable;

/**
 * @author HP
 * @Description TODO
 * @date 2020/11/9-10:50
 */
public class MyContent implements Serializable {

    String name;
    String method;
    Class<?>[] parameterTypes;
    Object[] args;
    String res;

    public MyContent(){

    }


    public MyContent(String name, String method, Class<?>[] parameterTypes, Object[] args) {
        this.name = name;
        this.method = method;
        this.parameterTypes = parameterTypes;
        this.args = args;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public void setRes(String res) {
        this.res=res;
    }

    public String getRes() {
        return res;
    }
}
