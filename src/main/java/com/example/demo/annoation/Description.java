package com.example.demo.annoation;

import java.lang.annotation.*;

/**
 * @Author: zhuwei
 * @Date:2019/6/20 10:35
 * @Description:
 */
/*****************   元注解    **********************************/
/*@Target 表示这个注解能放在什么位置上，是只能放在类上？还是即可以放在方法上，又可以放在属性上
 *
 *  ElementType.TYPE：能修饰类、接口或枚举类型
	ElementType.FIELD：能修饰成员变量
	ElementType.METHOD：能修饰方法
	ElementType.PARAMETER：能修饰参数
	ElementType.CONSTRUCTOR：能修饰构造器
	ElementType.LOCAL_VARIABLE：能修饰局部变量
	ElementType.ANNOTATION_TYPE：能修饰注解
	ElementType.PACKAGE：能修饰包
 * */
@Target({ElementType.METHOD ,  ElementType.TYPE})

/*@Retention
 * 表示生命周期，
 *
 * RetentionPolicy.SOURCE： 注解只在源代码中存在，编译成class之后，就没了。@Override 就是这种注解
 *
 * RetentionPolicy.CLASS： 注解在java文件编程成.class文件后，依然存在，但是运行起来后就没了
 * 						  @Retention的默认值，即当没有显式指定@Retention的时候，就会是这种类型。
 *
 * RetentionPolicy.RUNTIME： 注解在运行起来之后依然存在，程序可以通过反射获取这些信息
 *
 * */
@Retention( RetentionPolicy.RUNTIME)

/*@Inherited 表示该注解具有继承性
如果一个超类被 @Inherited 注解过的注解进行注解的话，那么如果它的子类没有被任何注解应用的话，那么这个子类就继承了超类的注解。*/
@Inherited

//元注解肯定是和文档有关。它的作用是能够将注解中的元素包含到 Javadoc 中去
@Documented
//使用 @interface 关键字定义注解
public @interface Description {

    /* 成员变量
     * 1. 注解的定义中以“无形参的方法”形式来声明，其方法名定义了该成员变量的名字，其返回值定义了该成员变量的类型
     *
     * 2. 注解中定义属性时它的类型必须是 8 种基本数据类型外加 类、接口、注解及它们的数组
     *
     * 3. 如果注解只有一个成员，则成员名必须取名为 value() ,在使用时可以忽略成员名和赋值号（=）
     *
     * 4. 注解类可以没有成员，没有成员的注解称为标识注解
     * */

    String desc();

    String author();

    //可以用 default 为成员指定一个默认的值
    int age() default 18;

}
