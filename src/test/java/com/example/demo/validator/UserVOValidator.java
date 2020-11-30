package com.example.demo.validator;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author: zhuwei
 * @Date:2020/3/3 16:45
 * @Description:
 */
public class UserVOValidator {

    private static Logger logger = LoggerFactory.getLogger(UserVOValidator.class);

    private static Validator validator;

    @BeforeClass
    public static void setUpValidator() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void userIsNull() {
        UserVO userVO = new UserVO();
        userVO.setId("你最帅1");
        userVO.setUserName("关 注 我");
        userVO.setBigDecimal(new BigDecimal(88));
        userVO.setIntValue(32432);
        userVO.setDoubleValue(22.3);
        userVO.setEmail("23444dfsdfdsf");
        userVO.setFlagFalse(true);
        userVO.setFlagTrue(false);
        userVO.setPhoneNo("2343243");
        Set<ConstraintViolation<UserVO>> constraintViolationSet = validator.validate(userVO);
        for(Iterator<ConstraintViolation<UserVO>> iterator = constraintViolationSet.iterator();iterator.hasNext();){
            ConstraintViolation<UserVO> constraintViolation = iterator.next();
            logger.info("验证结果，属性:{},结果:{}",constraintViolation.getPropertyPath(),constraintViolation.getMessage());
        }

    }
}
