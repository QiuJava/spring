package cn.qj.key.config.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.StringUtils;


/**
 * 数据源路由控制
 * 
 * @author Qiujian
 * @date 2018/09/25
 */
public class RoutingDataSource extends AbstractRoutingDataSource {

	private static final Logger logger = LoggerFactory.getLogger(RoutingDataSource.class);

	@Override
	protected Object determineCurrentLookupKey() {
		String dataSourceKey = DataSourceUtil.getDataSourceKey();
		logger.info("业务方法用的数据库:{}", StringUtils.hasLength(dataSourceKey) ? dataSourceKey : DataSourceUtil.WRITE_KEY);
		return dataSourceKey;
	}

}
