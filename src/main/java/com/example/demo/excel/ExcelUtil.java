package com.example.demo.excel;


import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ExcelUtil {

    /**
     * @description
     * @author yangyun
     * @date 2019/3/19 0019
     * @param excel 上传excel 文件
     * @param rowModel 对应实体对象
     * @param sheetNo 解析开始行, 有表头的情况从第一行开始
     * @return java.util.List<java.lang.Object>
     */
    public static List<Object> readExcel(InputStream file, BaseRowModel rowModel, int sheetNo) throws IOException {
        return readExcel(file, rowModel, sheetNo, 1);
    }

    public static List<Object> readExcel(InputStream file, BaseRowModel rowModel, int sheetNo, int headLineNum) throws IOException {
        ExcelListener excelListener = new ExcelListener();
        ExcelReader reader = getReader(file, excelListener);
        if (reader == null) {
            return null;
        }
        reader.read(new Sheet(sheetNo, headLineNum, rowModel.getClass()));
       // return excelListener.getDatas();
        return null;
    }

    public static ExcelReader getReader(InputStream file, ExcelListener excelListener) throws IOException{
        //String fileName = excel.getOriginalFilename();
//        if (fileName == null || !(fileName.toLowerCase().endsWith(".xls") || fileName.toLowerCase().endsWith(".xlsx") || fileName.toLowerCase().endsWith(".xlsm"))){
//            throw new ErpException(CommonConstant.FAIL_CODE, "文件格式错误");
//        }

        InputStream is = new BufferedInputStream(file);

        return new ExcelReader(is, null, excelListener, false);
    }
}