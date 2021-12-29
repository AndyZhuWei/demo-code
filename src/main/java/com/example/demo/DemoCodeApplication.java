package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;
import java.io.IOException;

@SpringBootApplication
//@MapperScan("com.example.demo.mapper")  test   fdasfd master分支修改
//@MapperScan("com.example.demo.mapper")  test test分支修改
@EnableScheduling
public class DemoCodeApplication {

	public static void main(String[] args) throws IOException {
		//SpringApplication.run(DemoCodeApplication.class, args);

		//System.out.println(System.getProperty("jdbc.drivers"));

		String fileName = "test.txt";
		File testFile = new File("filepath" + File.separator + "test" + File.separator + fileName);
		File fileParent = testFile.getParentFile();//返回的是File类型,可以调用exsit()等方法
		String fileParentPath = testFile.getParent();//返回的是String类型
		System.out.println("fileParent:" + fileParent);
		System.out.println("fileParentPath:" + fileParentPath);
		if (fileParent!=null) {
			fileParent.mkdirs();// 能创建多级目录
		}
		if (!testFile.exists())
			testFile.createNewFile();//有路径才能创建文件
		System.out.println(testFile);

		String path = testFile.getPath();
		String absolutePath = testFile.getAbsolutePath();//得到文件/文件夹的绝对路径
		String getFileName = testFile.getName();//得到文件/文件夹的名字
		System.out.println("path:"+path);
		System.out.println("absolutePath:"+absolutePath);
		System.out.println("getFileName:"+getFileName);

	}
}
