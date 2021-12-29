package com.example.demo.api.interceptor;

import com.example.demo.api.annoation.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @Description 拦截请求，是否此请求返回的值需要包装，其实就是运行的时候，解析@ResponseResult注解
 * @Author zhuwei
 * @Date 2021/3/1 19:51
 */
@Slf4j
@Component
public class ResponseResultInterceptor implements HandlerInterceptor {
    //标记名称
    public static final String RESPONSE_RESULT_ANN = "RESPONSE-RESULT-ANN";


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //请求的方法
        if(handler instanceof HandlerMethod) {
            final HandlerMethod handlerMethod = (HandlerMethod) handler;
            Class<?> clazz = handlerMethod.getBeanType();
            Method method = handlerMethod.getMethod();
            //判断是否在类对象上面加了注解
            if(clazz.isAnnotationPresent(ResponseResult.class)) {
                //设置此请求返回体，需要包装，往下传递，在ResponseBodyAdvice接口进行判断
                request.setAttribute(RESPONSE_RESULT_ANN,clazz.getAnnotation(ResponseResult.class));
            } else if(method.isAnnotationPresent(ResponseResult.class)) {//方法体上是否有注解
                //设置此请求返回体，需要包装，往下传递，在ResponseBodyAdvice接口进行判断
                request.setAttribute(RESPONSE_RESULT_ANN,clazz.getAnnotation(ResponseResult.class));
            }
        }
        return true;
    }
}
