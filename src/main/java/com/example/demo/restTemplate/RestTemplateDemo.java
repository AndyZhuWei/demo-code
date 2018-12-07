package com.example.demo.restTemplate;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

/**
 * @Author: zhuwei
 * @Date:2018/11/15 13:33
 * @Description:
 */
@RestController
@RequestMapping("/restTemplate")
public class RestTemplateDemo {


    @GetMapping("/doHttpGet")
    public String doGetControllerOne(HttpServletRequest request) throws UnsupportedEncodingException {
        System.out.println(request.getCharacterEncoding());
        //GET方式传输中文，需要转码
        String justryDeng = new String(request.getHeader("JustryDeng").getBytes("ISO-8859-1"),"utf-8");
        System.out.println(justryDeng);
        System.out.println("flag的值为："+request.getParameter("flag"));
        return "我是一只小小小鸟~";
    }

    @PostMapping("/doHttpPost")
    public String doGetControllerTwo(HttpServletRequest request,@RequestBody String jsonString) throws UnsupportedEncodingException {
        System.out.println(request.getCharacterEncoding());
        //GET方式传输中文，需要转码
        String justryDeng = new String(request.getHeader("JustryDeng").getBytes("ISO-8859-1"),"utf-8");
        System.out.println(justryDeng);
        System.out.println("flag的值为："+request.getParameter("flag"));
        //获取请求体中的数据
        System.out.println("请求体的数据为:"+jsonString);
        return "我是post的响应数据";
    }


}
