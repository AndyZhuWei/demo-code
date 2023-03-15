package com.example.demo.excel;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import org.assertj.core.util.Lists;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtil {

    public static void main(String[] args) throws FileNotFoundException {


        Map<String,Area> mapArea = new HashMap<>();
        Map<String,Province> mapProvince = new HashMap<>();



        List<Area> listArea = Lists.newArrayList();
        String fileName = "D:\\Desktop\\data.xlsx";
        EasyExcel.read(fileName,Data.class, new PageReadListener<Data>(dataList -> {
            for (Data data : dataList) {
                String area = data.getArea();
                String city = data.getCity();
                String aidCenterName = data.getAidCenterName();
                String level = data.getLevel();

                //处理区域
                boolean newArea = true;
                Area areaC = null;
                if(StringUtils.isBlank(area)) {
                    areaC = listArea.get(listArea.size()-1);
                    newArea = false;
                } else {
                    areaC = new Area();
                    areaC.setAreaId(data.getNo());
                    areaC.setArea(area);
//                    mapArea.put(area,areaC);
                    listArea.add(areaC);

                }
                //处理省
                List<Province> listProvince = null;
                if(newArea) {
                    listProvince = Lists.newArrayList();
                    areaC.setProvince(listProvince);
                } else {
                    listProvince = areaC.getProvince();
                }


                Province provinceC = null;
                List<String> cityList = null;
                String province = data.getProvince();
                if(StringUtils.isBlank(province)) {
                    provinceC = listProvince.get(listProvince.size()-1);
                    cityList = provinceC.getCityList();
                } else {
                    provinceC = new Province();
                    provinceC.setValue(province);
                    cityList = new ArrayList<>();
                    provinceC.setCityList(cityList);
//                    mapProvince.put(province,provinceC);
                    listProvince.add(provinceC);


                }
                cityList.add(city);


                //处理急救中心
                List<Aid> aidCenter = areaC.getAidCenter();
                if(aidCenter == null) {
                    aidCenter = Lists.newArrayList();
                    areaC.setAidCenter(aidCenter);
                }
                Aid aid = new Aid();
                aid.setName(aidCenterName);
                aid.setLocal("");
                aid.setCity(city);
                aid.setCityLevel(level);
                aidCenter.add(aid);

            }
        })).sheet().doRead();

        //得到listArea数据
        String s = JSONObject.toJSONString(listArea);
        System.out.println(s);
    }
}