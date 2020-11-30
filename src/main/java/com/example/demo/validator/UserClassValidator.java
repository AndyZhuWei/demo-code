package com.example.demo.validator;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Author zhuwei
 * @Date 2020/7/29 10:43
 * @Description: 类级别校验器
 */
public class UserClassValidator implements ConstraintValidator<UserClassCheck, UserVO> {
    @Override
    public boolean isValid(UserVO userVO, ConstraintValidatorContext constraintValidatorContext) {

        if (userVO == null) {
            return true;
        }

        String messageTemplate;

        if (userVO.getUserName().equals("关注我") && userVO.getId().equals("你最帅")) {
            return true;
        }

        messageTemplate = String.format("用户名:%1$s和ID:%2$s不匹配", userVO.getUserName(), userVO.getId());

        constraintValidatorContext.buildConstraintViolationWithTemplate(messageTemplate).addConstraintViolation();
        return false;
    }

    @Override
    public void initialize(UserClassCheck constraintAnnotation) {

    }
}
