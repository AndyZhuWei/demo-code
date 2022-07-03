package com.example.demo.excel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description TODO
 * @Author zhuwei
 * @Date 2022/5/25 14:05
 */
@Data
@NoArgsConstructor
public class Province {
    private String value;
    private List<String> cityList;
}
