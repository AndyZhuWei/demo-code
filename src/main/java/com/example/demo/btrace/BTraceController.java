package com.example.demo.btrace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: zhuwei
 * @Date:2019/9/10 6:31
 * @Description:
 */
@RestController
@RequestMapping("/btrace")
public class BTraceController {

    @Autowired
    private BTraceService btraceService;


    @GetMapping("/add/{a}/{b}")
    public int testAdd(@PathVariable int a,@PathVariable int b) {
        return btraceService.plus(a,b);
    }
}
