package com.example.demo.annoation;

import java.lang.annotation.*;

/**
 * @Author: zhuwei
 * @Date:2019/6/20 13:47
 * @Description:
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface DesSimple {
    String value();
}
