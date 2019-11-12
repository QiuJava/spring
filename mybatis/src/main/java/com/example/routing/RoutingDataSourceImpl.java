package com.example.routing;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.example.util.DataSourceUtil;

/**
 * 路由数据源
 * 
 * @author Qiu Jian
 * 
 */
public class RoutingDataSourceImpl extends AbstractRoutingDataSource {

	@Override
	protected Object determineCurrentLookupKey() {
		return DataSourceUtil.getKey();
	}

}
