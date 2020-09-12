package com.example.demo.designPatterns.responsibility;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * @author HP
 * @Description TODO
 * @date 2020/9/11-8:50
 */
public class Main {

    public static void main(String[] args) {
        Msg msg = new Msg();
        msg.setMsg("大家好:).<script>t,欢迎访问 mashibing.com,大家都是996 ");

        //以下处理消息的代码可能会非常复杂，如果都这么写在这里会非常的乱，非常不容易扩展。
        //既然处理消息的这部分时变化的，我们就把变化的抽取处理进行封装，即封装变化
        //1.处理msg
       /* String r = msg.getMsg();
        r = r.replace("<","[");
        r = r.replace(">","]");
        msg.setMsg(r);

        r = r.replaceAll("996","995");
        msg.setMsg(r);*/


        //2.使用过滤器，想下边的处理一样，但是filter没有串在一起，我们可以用一个list
//        new HTMLFilter().doFilter(msg);
//        new SensitiveFilter().doFilter(msg);

        //3.用list串其各个filter,如果有其他filter进来了只需要往list中添加
        //其实这个还不是责任链，各个filter还没有串成一个链条，只是放入到了list中而已
//        List<Filter> filterList = new ArrayList();
//        filterList.add(new HTMLFilter());
//        filterList.add(new SensitiveFilter());
//        for(Filter f : filterList) {
//            f.doFilter(msg);
//        }

        //4.将各个filter串成一个链条 这个就是责任链模式，每个filter负责自己的责任
        //目前FilterChain和装入list中的方式还差不多，接着往下看我们编写FilterChain2
       /* FilterChain fc = new FilterChain();
        fc.add(new HTMLFilter());
        fc.add(new SensitiveFilter());
        fc.doFilter(msg);*/
        //改为FilterChain2后，我们添加filter时就可以使用了链式编程，如下
//        FilterChain2 fc = new FilterChain2();
//        fc.add(new HTMLFilter()).add(new SensitiveFilter()).doFilter(msg);

        //5.如果我们有两个链条，怎么将两个链条合在一起呢

//        FilterChain2 fc = new FilterChain2();
//        fc.add(new HTMLFilter()).add(new SensitiveFilter());
//
//        FilterChain2 fc2 = new FilterChain2();
//        fc2.add(new FaceFilter()).add(new URLFilter());
//
//        //需要连续调用两次doFilter，有点不爽，我们可以把FilterChain2改成成实现Filter接口
//        fc.doFilter(msg);
//        fc2.doFilter(msg);


//        //6.将FilterChain2改造成实现Filter接口，看FilterChain3
//        //这样合并链条的时候，需要add就可以了
//        //现在又有一个要求，又FilterChain中的某一个Filter决定链条是否继续，改怎么办？接着往下看
//
//        FilterChain3 fc = new FilterChain3();
//        fc.add(new HTMLFilter()).add(new SensitiveFilter());
//
//        FilterChain3 fc2 = new FilterChain3();
//        fc2.add(new FaceFilter()).add(new URLFilter());
//
//        fc.add(fc2);
//
//        fc.doFilter(msg);

        //7.将Filter接口中的方法改造成返回boolean类型，看Filter2
        //然后将各个Filter的实现进行改造
        /*FilterChain4 fc = new FilterChain4();
        fc.add(new HTMLFilter2()).add(new SensitiveFilter2());

        FilterChain4 fc2 = new FilterChain4();
        fc2.add(new FaceFilter2()).add(new URLFilter2());

        fc.add(fc2);

        fc.doFilter(msg);

        System.out.println(msg);*/


        //8.思考：在jdk中定义的javax.servelt.Filter和javax.servlet.FilterChain接口可以过滤请求和响应两个过程
        //，即一个Filter可以处理请求和响应。这是怎么设计实现的？参考ServletMain
    }
}

class Msg {
    String name;
    String msg;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Msg{" +
                "name='" + name + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}

interface Filter {
    void doFilter(Msg mss);
}

interface Filter2 {
    boolean doFilter(Msg mss);
}

class HTMLFilter implements Filter {
    @Override
    public void doFilter(Msg msg) {
        String r = msg.getMsg();
        r = r.replace("<","[");
        r = r.replace(">","]");
        msg.setMsg(r);
    }
}

class HTMLFilter2 implements Filter2 {
    @Override
    public boolean doFilter(Msg msg) {
        String r = msg.getMsg();
        r = r.replace("<","[");
        r = r.replace(">","]");
        msg.setMsg(r);
        return true;
    }
}

class SensitiveFilter implements Filter {
    @Override
    public void doFilter(Msg msg) {
        String r = msg.getMsg();
        r = r.replaceAll("996","995");
        msg.setMsg(r);
    }
}

class SensitiveFilter2 implements Filter2 {
    @Override
    public boolean doFilter(Msg msg) {
        String r = msg.getMsg();
        r = r.replaceAll("996","995");
        msg.setMsg(r);
        return false;
    }
}

class FaceFilter implements Filter {
    @Override
    public void doFilter(Msg msg) {
        String r = msg.getMsg();
        r = r.replace(":)","^V^");
        msg.setMsg(r);
    }
}

class FaceFilter2 implements Filter2 {
    @Override
    public boolean doFilter(Msg msg) {
        String r = msg.getMsg();
        r = r.replace(":)","^V^");
        msg.setMsg(r);
        return true;
    }
}

class URLFilter implements Filter {
    @Override
    public void doFilter(Msg msg) {
        String r = msg.getMsg();
        r = r.replace("mashibing.com","http://www.mashibing.com");
        msg.setMsg(r);
    }
}

class URLFilter2 implements Filter2 {
    @Override
    public boolean doFilter(Msg msg) {
        String r = msg.getMsg();
        r = r.replace("mashibing.com","http://www.mashibing.com");
        msg.setMsg(r);
        return true;
    }
}

class FilterChain {
    List<Filter> filterList = new ArrayList();

    public void add(Filter f) {
        filterList.add(f);
    }

    public void doFilter(Msg msg) {
        for(Filter f : filterList) {
            f.doFilter(msg);
        }
    }
}

class FilterChain2 {
    List<Filter> filterList = new ArrayList();

    public FilterChain2 add(Filter f) {
        filterList.add(f);
        return this;
    }

    public void doFilter(Msg msg) {
        for(Filter f : filterList) {
            f.doFilter(msg);
        }
    }
}

class FilterChain3 implements Filter{
    List<Filter> filterList = new ArrayList();

    public FilterChain3 add(Filter f) {
        filterList.add(f);
        return this;
    }

    @Override
    public void doFilter(Msg msg) {
        for(Filter f : filterList) {
            f.doFilter(msg);
        }
    }
}

class FilterChain4 implements Filter2{
    List<Filter2> filterList = new ArrayList();

    public FilterChain4 add(Filter2 f) {
        filterList.add(f);
        return this;
    }

    @Override
    public boolean doFilter(Msg msg) {
        for(Filter2 f : filterList) {
            if(!f.doFilter(msg)) {
                return false;
            }
        }
        return true;
    }
}