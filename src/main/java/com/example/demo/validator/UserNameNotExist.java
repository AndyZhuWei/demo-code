package com.example.demo.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @Author zhuwei
 * @Date 2020/7/29 9:57
 * @Description: 验证用户是否在数据库的注解
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.FIELD,ElementType.ANNOTATION_TYPE,ElementType.CONSTRUCTOR,ElementType.PARAMETER,ElementType.TYPE_USE})
@Constraint(
        validatedBy = {UserNameValidator.class}
)
public @interface UserNameNotExist {
    String message() default "用户${validatedValue}不存在";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
