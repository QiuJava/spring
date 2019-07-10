package cn.qj.core.common;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import cn.qj.core.util.DataSourceUtil;

/**
 * 数据源路由控制
 * 
 * @author Qiujian
 * @date 2018/09/25
 */
public class RoutingDataSource extends AbstractRoutingDataSource {

	@Override
	protected Object determineCurrentLookupKey() {
		return DataSourceUtil.getDataSourceKey();
	}

}
