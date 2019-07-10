package cn.loan.core.config.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 路由数据源
 * 
 * @author Qiujian
 * 
 */
public class RoutingDataSource extends AbstractRoutingDataSource {

	@Override
	protected Object determineCurrentLookupKey() {
		return DataSourceUtil.getDataSourceKey();
	}

}
