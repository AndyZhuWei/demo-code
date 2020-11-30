package com.example.demo.validator;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.testng.annotations.BeforeClass;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author: zhuwei
 * @Date:2020/3/3 15:41
 * @Description:
 */
@UserClassCheck
public class UserVO {

    //@NotNull(message = "id不能为空")
    @NotEmpty(message = "id不能为空")
    private String id;

    //自定义注解
    //@UserNameNotExist
    @Length(min = 20,max = 30,message = "${validatedValue}字符串长度要求{min}到{max}之间")
    private String userName;

    @NotNull(message = "密码不能为空")
    private String userPswd;

    @AssertTrue
    private boolean flagTrue;

    @AssertFalse
    private boolean flagFalse;

    @DecimalMin(value = "10",message = "最小值{value}")
    @DecimalMax(value = "20",message = "最大值{value}")
    private BigDecimal bigDecimal;


    @NotNull
    @DecimalMin(value = "0.01",message = "最小值{value}")
    @DecimalMax(value = "999.00",message = "最大值{value}")
    private Double doubleValue = null;


    @NotNull
    @Min(value = 1,message = "最小值{value}")
    @Max(value = 88,message = "最大值{value}")
    private Integer intValue = null;

    @Range(min = 1,max = 888,message = "范围{mix}到{max}")
    private Long range;

    @Email(message = "邮箱格式错误")
    private String email;

    @Future(message = "需要一个将来的日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createDate;

    private String createBy;


    @Past(message = "需要一个过去的日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp updateDate;

    @Length(min = 2,max = 8,message = "这个值${validatedValue}不符合，至少{min}个字符,最多{max}个字符")
    private String updateBy;

    @Pattern(regexp = "^[0-9]*$",message = "只能为数字")
    private String phoneNo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPswd() {
        return userPswd;
    }

    public void setUserPswd(String userPswd) {
        this.userPswd = userPswd;
    }

    public boolean isFlagTrue() {
        return flagTrue;
    }

    public void setFlagTrue(boolean flagTrue) {
        this.flagTrue = flagTrue;
    }

    public boolean isFlagFalse() {
        return flagFalse;
    }

    public void setFlagFalse(boolean flagFalse) {
        this.flagFalse = flagFalse;
    }

    public BigDecimal getBigDecimal() {
        return bigDecimal;
    }

    public void setBigDecimal(BigDecimal bigDecimal) {
        this.bigDecimal = bigDecimal;
    }

    public Double getDoubleValue() {
        return doubleValue;
    }

    public void setDoubleValue(Double doubleValue) {
        this.doubleValue = doubleValue;
    }

    public Integer getIntValue() {
        return intValue;
    }

    public void setIntValue(Integer intValue) {
        this.intValue = intValue;
    }

    public Long getRange() {
        return range;
    }

    public void setRange(Long range) {
        this.range = range;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }


}

class UserVOValidator {

    private static Logger logger = LoggerFactory.getLogger(UserVOValidator.class);

    private static Validator validator;

    public  UserVOValidator() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public void userIsNull() {
        UserVO userVO = new UserVO();
        userVO.setId("你最帅");
        userVO.setUserName("关注我");
        userVO.setBigDecimal(new BigDecimal(88));
        userVO.setIntValue(32432);
        userVO.setDoubleValue(22.3);
        userVO.setEmail("23444dfsdfdsf");
        userVO.setFlagFalse(true);
        userVO.setFlagTrue(false);
        userVO.setPhoneNo("2343243");
        Set<ConstraintViolation<UserVO>> constraintViolationSet = validator.validate(userVO);
        for(Iterator<ConstraintViolation<UserVO>> iterator = constraintViolationSet.iterator(); iterator.hasNext();){
            ConstraintViolation<UserVO> constraintViolation = iterator.next();
            logger.info("验证结果，属性:{},结果:{}",constraintViolation.getPropertyPath(),constraintViolation.getMessage());
        }
    }

    public static void main(String[] args) {
        UserVOValidator userVOValidator = new UserVOValidator();
        userVOValidator.userIsNull();
    }
}
