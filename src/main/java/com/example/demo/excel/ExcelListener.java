package com.example.demo.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExcelListener extends AnalysisEventListener<StockModel> {

    /**
     * @description 用于保存数据
     * @author yangyun
     * @date 2019/3/27 0027
     * @param null
     * @return
     */
    private List<StockModel> datas = Collections.synchronizedList(new ArrayList<>());

    /**
     * @description
     * @author yangyun
     * @date 2019/3/19 0019
     * @param o 读取的每行数据
     * @param analysisContext
     * @return void
     */
    @Override
    public void invoke(StockModel o, AnalysisContext analysisContext) {
        datas.add(o);
    }

    /**
     * @description excel 读取完之后的操作
     * @author yangyun
     * @date 2019/3/27 0027
     * @param analysisContext
     * @return void
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
//        analysisContext.
    }

    public List<StockModel> getDatas() {
        return datas;
    }

    public void setDatas(List<StockModel> datas) {
        this.datas = datas;
    }
}