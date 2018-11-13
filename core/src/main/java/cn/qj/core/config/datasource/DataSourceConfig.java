package cn.qj.core.config.datasource;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;

import cn.qj.core.common.RoutingDataSource;
import cn.qj.core.util.DataSourceUtil;

/**
 * 数据源配置
 * 
 * @author Qiujian
 * @date 2018/09/25
 */
@Configuration
public class DataSourceConfig {

	@Autowired
	private WriteDataSourceConfig writeDataSourceConfig;
	@Autowired
	private ReadDataSourceConfig readDataSourceConfig;

	public DataSource writeDataSource() {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUrl(writeDataSourceConfig.getUrl());
		dataSource.setDriverClassName(writeDataSourceConfig.getDriverClassName());
		dataSource.setUsername(writeDataSourceConfig.getUsername());
		dataSource.setPassword(writeDataSourceConfig.getPassword());
		return dataSource;
	}

	public DataSource readDataSource() {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUrl(readDataSourceConfig.getUrl());
		dataSource.setDriverClassName(readDataSourceConfig.getDriverClassName());
		dataSource.setUsername(readDataSourceConfig.getUsername());
		dataSource.setPassword(readDataSourceConfig.getPassword());
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
