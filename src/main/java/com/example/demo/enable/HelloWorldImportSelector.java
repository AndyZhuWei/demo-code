package com.example.demo.enable;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @Description 基于ImportSelector
 * @Author zhuwei
 * @Date 2021/4/19 20:01
 */
public class HelloWorldImportSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        return new String[]{"com.example.demo.enable.HelloWorldConfiguration"};
    }
}
