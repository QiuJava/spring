package com.example.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.example.util.DataSourceUtil;

/**
 * 数据源KEY
 * 
 * @author Qiu Jian
 * 
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSourceKey {

	String value() default DataSourceUtil.MASTER_DATASOURCE_KEY;

}
