package com.example.config.datasource;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.example.routing.RoutingDataSourceImpl;
import com.example.util.DataSourceUtil;

/**
 * 数据源配置
 *
 * @author Qiu Jian
 *
 */
@Configuration
public class DataSourceConfig {

	@Value("${datasource.master.driverClassName}")
	private String masterDriverClassName;
	@Value("${datasource.master.url}")
	private String masterUrl;
	@Value("${datasource.master.username}")
	private String masterUsername;
	@Value("${datasource.master.password}")
	private String masterPassword;

	@Value("${datasource.slaveOne.driverClassName}")
	private String slaveOneDriverClassName;
	@Value("${datasource.slaveOne.url}")
	private String slaveOneUrl;
	@Value("${datasource.slaveOne.username}")
	private String slaveOneUsername;
	@Value("${datasource.slaveOne.password}")
	private String slaveOnePassword;

	/**
	 * 设置路由数据源
	 * 
	 * @return
	 */
	@Bean
	public DataSource routingDataSource() {
		RoutingDataSourceImpl routingDataSource = new RoutingDataSourceImpl();

		DruidDataSource masterDataSource = new DruidDataSource();
		masterDataSource.setDriverClassName(masterDriverClassName);
		masterDataSource.setUrl(masterUrl);
		masterDataSource.setUsername(masterUsername);
		masterDataSource.setPassword(masterPassword);
		DruidDataSource slaveOneDataSource = new DruidDataSource();
		slaveOneDataSource.setDriverClassName(slaveOneDriverClassName);
		slaveOneDataSource.setUrl(slaveOneUrl);
		slaveOneDataSource.setUsername(slaveOneUsername);
		slaveOneDataSource.setPassword(slaveOnePassword);

		Map<Object, Object> targetDataSources = new HashMap<>(2);
		targetDataSources.put(DataSourceUtil.MASTER_DATASOURCE_KEY, masterDataSource);
		targetDataSources.put(DataSourceUtil.SLAVE_ONE_DATASOURCE_KEY, slaveOneDataSource);
		routingDataSource.setTargetDataSources(targetDataSources);
		routingDataSource.setDefaultTargetDataSource(masterDataSource);
		return routingDataSource;
	}

}
