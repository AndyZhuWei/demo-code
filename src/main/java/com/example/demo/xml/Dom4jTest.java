package com.example.demo.xml;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.List;

/**
 * @Author: zhuwei
 * @Date:2019/9/16 9:39
 * @Description:
 */
public class Dom4jTest {
    public static void main(String[] args) throws DocumentException {
        //构造XML字符串
        StringBuffer buffer = new StringBuffer();
        buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        buffer.append("<person>");
        buffer.append("<name>朱伟</name>");
        buffer.append("<sex>男</sex>");
        buffer.append("<address>四川成都</address>");
        buffer.append("</person>");

        //通过解析XML字符串创建Document对象
        Document document = DocumentHelper.parseText(buffer.toString());
        //得到XML的根元素（本例中是person）
        Element root = document.getRootElement();
        //得到根元素person所有子节点
        List<Element> elementList = root.elements();
        //遍历所有子节点
        for(Element e : elementList) {
            //输出子节点名称和值
            System.out.println(e.getName()+" => "+e.getText());
        }


    }
}
