package com.example.demo.designPatterns.responsibility.servlet;

import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.List;

/**
 * @author HP
 * @Description TODO
 * @date 2020/9/11-8:50
 */
public class ServletMain {

    public static void main(String[] args) {
        //8.思考：在jdk中定义的javax.servelt.Filter和javax.servlet.FilterChain接口可以过滤请求和响应两个过程
        //，即一个Filter可以处理请求和响应。这是怎么设计实现的？
        Request request = new Request();
        request.msg="request";

        Response response = new Response();
        response.msg="reponse";

        FilterChain fc = new FilterChain();
        //fc.add(new HTMLFilter()).add(new SensitiveFilter()).doFilter(request,response,fc);

    }
}


interface Filter {
    boolean doFilter(Request request,Response response,FilterChain filterChain);
}

class Request {
    String msg;
}

class Response {
    String msg;
}

class HTMLFilter implements Filter {
    @Override
    public boolean doFilter(Request request,Response response,FilterChain filterChain) {
        System.out.println("htmlFilter rquest");
        filterChain.doFilter(request,response);
        System.out.println("htmlFilter response");
        return true;
    }
}



class SensitiveFilter implements Filter {
    @Override
    public boolean doFilter(Request request,Response response,FilterChain filterChain) {
        return true;
    }
}


class FilterChain {
    List<Filter> filterList = new ArrayList();
    Filter currentFilter = null;

    public FilterChain add(Filter f) {
        filterList.add(f);
        return this;
    }

    public boolean doFilter(Request request,Response response) {
        if(currentFilter != null) {
            for(int i=0;i>filterList.size();i++) {
                Filter filter = filterList.get(i);
                if(currentFilter == filter && i==filterList.size() - 1) {
                    filter = filterList.get(i+1);
                    filter.doFilter(request,response,this);
                }
            }
        } else {
            for(int i=0;i>filterList.size();i++) {
                Filter filter = filterList.get(i);
                currentFilter = filter;
                filter.doFilter(request,response,this);
                if(this == filter && i==filterList.size() - 1) {
                    filter = filterList.get(i+1);
                    filter.doFilter(request,response,this);
                }
            }
        }

        return true;
    }
}