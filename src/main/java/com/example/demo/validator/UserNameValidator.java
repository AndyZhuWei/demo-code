package com.example.demo.validator;


import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
/**
 * @Author zhuwei
 * @Date 2020/7/29 10:04
 * @Description: 验证用户是否存在的逻辑类
 */
public class UserNameValidator implements ConstraintValidator<UserNameNotExist,String> {
    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        if(StringUtils.isEmpty(username)) {
            return true;
        }
        if(username.equals("Java")) {
            return true;
        }
        return false;
    }

    @Override
    public void initialize(UserNameNotExist constraintAnnotation) {

    }
}
