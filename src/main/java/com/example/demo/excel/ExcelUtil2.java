package com.example.demo.excel;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import org.assertj.core.util.Lists;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理特药清单
 */
public class ExcelUtil2 {

    public static void main(String[] args) throws FileNotFoundException {


        Map<String,Data2> mapArea = new HashMap<>();



        List<Data2> listData2 = Lists.newArrayList();
        String fileName = "D:\\Desktop\\data.xlsx";
        EasyExcel.read(fileName,Data2.class, new PageReadListener<Data2>(dataList -> {
            for (Data2 data : dataList) {
                String originName = data.getOrigin_name();
                String name = data.getName();
                String company = data.getCompany();
                String forDisease = data.getForDisease();
                Data2 data2 = null;
                if(StringUtils.isBlank(originName)) {
                    data2 = listData2.get(listData2.size()-1);
                    String forDisease1 = data2.getForDisease();
                    String[] split = forDisease.split("、");
                    for (String s : split) {
                        if(s.indexOf(forDisease1) == -1 ) {
                            forDisease1 += "、" + s;
                        }
                    }
                    data2.setForDisease(forDisease1);
                } else {
                    data2 = new Data2();
                    data2.setOrigin_name(originName);
                    data2.setName(name);
                    data2.setCompany(company);
                    data2.setForDisease(forDisease);
                    listData2.add(data2);

                }

            }
        })).sheet().doRead();

        //得到listArea数据
        String s = JSONObject.toJSONString(listData2);
        System.out.println(s);
    }
}