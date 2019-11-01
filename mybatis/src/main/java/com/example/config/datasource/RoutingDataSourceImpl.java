package com.example.config.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.example.util.DataSourceUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 路由数据源
 * 
 * @author Qiu Jian
 * 
 */
@Slf4j
public class RoutingDataSourceImpl extends AbstractRoutingDataSource {

	@Override
	protected Object determineCurrentLookupKey() {
		String key = DataSourceUtil.getKey();
		log.info("执行sql的数据源key-->[{}]", key == null ? DataSourceUtil.MASTER_DATASOURCE_KEY : key);
		return key;
	}

}
