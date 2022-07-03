package com.example.demo;

import com.alibaba.excel.EasyExcel;
import com.example.demo.excel.ExcelListener;
import com.example.demo.excel.ExcelUtil;
import com.example.demo.excel.StockModel;
import com.example.demo.model.PolicyNumTemp;
import com.example.demo.service.PolicyNumTempService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoCodeApplicationTests {

	@Autowired
	private PolicyNumTempService policyNumTempService;

	@Test
	public void contextLoads() {
		List<PolicyNumTemp> policyNumTempList = new ArrayList();
		//1.加载本地文件
		File file=new File("C:\\Users\\pc\\Desktop\\policy_num.txt");
		BufferedReader reader=null;
		String temp=null;
		int line=1;
		try{
			reader=new BufferedReader(new FileReader(file));
			while((temp=reader.readLine())!=null){
				policyNumTempList.add(new PolicyNumTemp(temp));
				line++;
			}
			policyNumTempService.batchInsert(policyNumTempList);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			if(reader!=null){
				try{
					reader.close();
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		}



	}





	@Test
	public void indexOrNameRead() {
		String fileName = "D:\\Desktop\\data.xlsx";
		// 这里默认读取第一个sheet
		ExcelListener excelListener = new ExcelListener();
		EasyExcel.read(fileName, StockModel.class, excelListener).sheet().doRead();
		List<StockModel> datas = excelListener.getDatas();
		if(datas!=null && datas.size()!=0) {
			for(StockModel stockModel: datas) {

			}
		}
	}

}
