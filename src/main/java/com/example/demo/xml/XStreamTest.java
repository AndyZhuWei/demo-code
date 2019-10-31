package com.example.demo.xml;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * @Author: zhuwei
 * @Date:2019/9/19 9:26
 * @Description: XStream的使用示例
 */
public class XStreamTest {
    /**
     * Java对象转换为XML
     */
    public static String javaObject2Xml(Person person) {
        XStream xs = new XStream(new DomDriver());
        //给person类定义别名
        xs.alias("person", person.getClass());
        return xs.toXML(person);
    }

    /**
     * XML转换为Java对象
     */
    public static Object xml2JavaObject(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.alias("person", Person.class);
        Person person = (Person) xs.fromXML(xml);
        return person;
    }


    public static void main(String[] args) {
        //构建Person对象
        Person p1 = new Person();
        p1.setName("朱伟");
        p1.setSex("男");
        p1.setAddress("天府三街");
        //将p1对象转换成XML字符串
        System.out.println(javaObject2Xml(p1));

        //构造XML字符串
        StringBuffer buffer = new StringBuffer();
        buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        buffer.append("<person>");
        buffer.append("<name>朱伟</name>");
        buffer.append("<sex>男</sex>");
        buffer.append("<address>四川成都</address>");
        buffer.append("</person>");
        //将XML字符串转换成Person对象
        Person p2 = (Person) xml2JavaObject(buffer.toString());
        //输出
        System.out.println(p2.getName() + " " + p2.getName() + " " + p2.getAddress());

    }
}
