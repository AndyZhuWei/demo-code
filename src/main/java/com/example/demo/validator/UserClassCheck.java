package com.example.demo.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @Author zhuwei
 * @Date 2020/7/29 10:41
 * @Description: 对整个类进行校验
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Constraint(
        validatedBy = {UserClassValidator.class}
)
public @interface UserClassCheck {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
