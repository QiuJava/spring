package cn.loan.core.config.datasource;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * 自定义数据源
 * 
 * @author qiujian
 *
 */
@Configuration
public class DataSourceCustom {

	@Autowired
	private WriteDataSource writeDs;
	@Autowired
	private ReadDataSource readDs;

	public DataSource writeDataSource() {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUrl(writeDs.getUrl());
		dataSource.setDriverClassName(writeDs.getDriverClassName());
		dataSource.setUsername(writeDs.getUsername());
		dataSource.setPassword(writeDs.getPassword());
		return dataSource;
	}

	public DataSource readDataSource() {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUrl(readDs.getUrl());
		dataSource.setDriverClassName(readDs.getDriverClassName());
		dataSource.setUsername(readDs.getUsername());
		dataSource.setPassword(readDs.getPassword());
		return dataSource;
	}

	@Bean
	public DataSource routingDataSource() {
		RoutingDataSource routingDataSource = new RoutingDataSource();
		Map<Object, Object> targetDataSources = new HashMap<>(2);
		DataSource writeDataSource = writeDataSource();
		targetDataSources.put(DataSourceUtil.WRITE_KEY, writeDataSource);
		targetDataSources.put(DataSourceUtil.READ_ONE_KEY, readDataSource());
		routingDataSource.setTargetDataSources(targetDataSources);
		routingDataSource.setDefaultTargetDataSource(writeDataSource);
		return routingDataSource;
	}
}
